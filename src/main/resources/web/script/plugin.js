dm4c.add_plugin("de.ancud.camunda.dm4", function() {

    dm4c.add_listener("logged_in", function(username) {
        console.log("*** User \"" + username + "\" logged in (session " + js.get_cookie("JSESSIONID") + ")");
        dm4c.get_plugin("de.deepamehta.websockets").create_websocket("ws://localhost:8081", "de.ancud.camunda.dm4");
    })
})
