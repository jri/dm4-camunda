dm4c.add_plugin("de.ancud.camunda.dm4", function() {

    dm4c.add_listener("logged_in", function(username) {
        //
        console.log("*** User \"" + username + "\" logged in (session " + js.get_cookie("JSESSIONID") + ")");
        //
        dm4c.get_plugin("de.deepamehta.websockets").create_websocket(
            "ws://localhost:8081", "de.ancud.camunda.dm4", processMessage
        );
    });

    function processMessage(message) {
        switch (message.action) {
        case "showTopic":
            console.log("showTopic", message.topicId);
            // Here use dm4c API to interact with the DeepaMehta webclient.
            // See /modules/dm4-webclient/src/main/resources/web/script/webclient.js
            break;
        default:
            throw "Action \"" + message.action + "\" is not handled";
        }
    }
});
