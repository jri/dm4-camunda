package de.ancud.camunda.dm4;

import de.deepamehta.accesscontrol.event.PostLoginUserListener;
import de.deepamehta.core.osgi.PluginActivator;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import java.util.logging.Logger;



@Path("/camunda")
@Consumes("application/json")
@Produces("application/json")
public class CamundaPlugin extends PluginActivator implements CamundaService, PostLoginUserListener {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    @Context
    private HttpServletRequest request;

    private static Logger logger = Logger.getLogger(CamundaPlugin.class.getName());

    // -------------------------------------------------------------------------------------------------- Public Methods

    // *** CamundaService ***

    @GET
    @Path("/hello")
    @Override
    public String hello() {
        return "*** DeepaMehta 4 Camunda ***\n";
    }

    // *** Listeners ***

    @Override
    public void postLoginUser(String username) {
        String sessionId = request.getSession(false).getId();
        logger.info("### Session ID: " + sessionId);
        // login to Camunda application and pass the DeepaMehta session ID.
        // Let the Camunda application pass back the session ID in every request (in the JESSIONID cookie).
    }
}
