package cucumber.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
            "cucumber.steps",
            "cucumber.hooks"
        },
        plugin = {
            "pretty",
            "html:target/cucumber-report.html",
            "json:target/cucumber-report.json"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}