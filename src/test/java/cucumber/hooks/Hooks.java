package cucumber.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before
    public void beforeScenario() {
        System.out.println("🚀 [HOOK] Starting scenario...");
    }

    @After
    public void afterScenario() {
        System.out.println("✅ [HOOK] Scenario finished.");
    }
}