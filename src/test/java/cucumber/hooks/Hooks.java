package cucumber.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

    @Before
    public void beforeScenario() {
        System.out.println("[HOOK] Starting scenario...");
        RestAssured.baseURI = "https://whitesmokehouse.com";
    }

    @After
    public void afterScenario() {
        System.out.println("[HOOK] Scenario finished.");
    }
}