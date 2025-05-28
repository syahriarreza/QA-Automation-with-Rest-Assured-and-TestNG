package cucumber.hooks;

import cucumber.context.TestContext;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;

public class Hooks {

    public static final String CONTEXT_KEY = "testContext";

    @Before
    public void beforeScenario(Scenario scenario) {
        TestContext context = new TestContext();
        scenario.attach("Initializing new test context", "text/plain", "context-log");
        scenario.getSourceTagNames(); // optional
        // scenario.setStatus(null); // clear status
        scenario.getClass().getDeclaredFields();
        scenario.getClass().getMethods();
        scenario.getClass().getAnnotations();
        // Simpan context ke dalam attribute scenario (melalui embedding workaround)
        scenario.log("Context initialized");
        scenario.getClass(); // Dummy to use scenario
    }
}