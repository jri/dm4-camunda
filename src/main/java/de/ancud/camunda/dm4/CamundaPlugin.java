package de.ancud.camunda.dm4;

import de.deepamehta.accesscontrol.event.PostLoginUserListener;
import de.deepamehta.core.ChildTopics;
import de.deepamehta.core.Topic;
import de.deepamehta.core.model.TopicModel;
import de.deepamehta.core.osgi.PluginActivator;
import de.deepamehta.core.service.Inject;
import de.deepamehta.core.service.event.PostUpdateTopicListener;
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
public class CamundaPlugin extends PluginActivator implements CamundaService, PostUpdateTopicListener,
                                                                              PostLoginUserListener {

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
            // Here use the DeepaMehta Core Service ("dm4" object) and/or the injected services as provided by the
            // other modules. See /modules/dm4-core/src/main/java/de/deepamehta/core/service/CoreService.java
            // or the ...Service.java interfaces of the other modules.
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
    public void postUpdateTopic(Topic topic, TopicModel newModel, TopicModel oldModel) {
        try {
            if (topic.getTypeUri().equals("dm4.contacts.person")) {
                ChildTopics childs = topic.getChildTopics().getTopic("dm4.contacts.person_name").getChildTopics();
                String firstName = childs.getTopic("dm4.contacts.first_name").getSimpleValue().toString();
                String lastName  = childs.getString("dm4.contacts.last_name");
                logger.info("### Person: \"" + firstName + "\" \"" + lastName + "\"");
                JSONObject json = topic.toJSON()
                    .put("process", 1234);
            }
        } catch (Exception e) {
            throw new RuntimeException("Creating JSON failed", e);
        }
    }

    @Override
    public void postLoginUser(String username) {
        String sessionId = request.getSession(false).getId();
        logger.info("### Session ID: " + sessionId);
        // Here login to the external Camunda application and pass the DeepaMehta session ID.
        // Let the Camunda application pass back the session ID in every request (in the JESSIONID cookie).
    }
}
