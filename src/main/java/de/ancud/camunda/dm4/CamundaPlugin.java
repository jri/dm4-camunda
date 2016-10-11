package de.ancud.camunda.dm4;

import de.deepamehta.accesscontrol.event.PostLoginUserListener;
import de.deepamehta.core.osgi.PluginActivator;
import de.deepamehta.core.service.Inject;
import de.deepamehta.websockets.WebSocketsService;

import org.codehaus.jettison.json.JSONObject;

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

    @Inject
    private WebSocketsService wsService;

    @Context
    private HttpServletRequest request;

    private static Logger logger = Logger.getLogger(CamundaPlugin.class.getName());

    // -------------------------------------------------------------------------------------------------- Public Methods

    // *** CamundaService ***

    @GET
    @Path("/hello")
    @Override
    public String hello() {
        try {
            wsService.sendMessage(getUri(), new JSONObject()
                .put("action", "showTopic")
                .put("topicId", 1234)
                .toString()
            );
            return "*** DeepaMehta 4 Camunda ***\n";
        } catch (Exception e) {
            throw new RuntimeException("Sending a WebSocket message failed", e);
        }
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
