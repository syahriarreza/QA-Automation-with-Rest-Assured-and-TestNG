package cucumber.runner;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import io.restassured.RestAssured;
import cucumber.helpers.ConfigManager;
import cucumber.helpers.GenerateReport;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features", glue = {
                "cucumber.steps",
                "cucumber.hooks"
}, plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber-report.json"
}, monochrome = true)

public class TestRunner extends AbstractTestNGCucumberTests {
        @BeforeSuite
        public void before_suite() {
                RestAssured.baseURI = ConfigManager.getBaseUrl();
        }

        @AfterSuite
        public void after_suite() {
                GenerateReport.generateReport();
        }
}