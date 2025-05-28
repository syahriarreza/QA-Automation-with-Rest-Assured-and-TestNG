package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.context.TestContext;
import cucumber.dto.ObjectRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RequiredArgsConstructor
public class ObjectSteps {

    private final TestContext testContext;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ObjectRequest objectRequest;
    private String objectId;

    @Given("I prepare a new object with name {string}")
    public void i_prepare_a_new_object(String objectName) {
        objectRequest = new ObjectRequest();
        objectRequest.setName(objectName + " " + UUID.randomUUID());
        testContext.setRequestBody(objectRequest);
    }

    @And("I save the object ID")
    public void i_save_the_object_id() {
        Response response = testContext.getResponse();
        objectId = response.jsonPath().getString("data.id");
        assertThat("Object ID should not be null", objectId, not(emptyOrNullString()));
        testContext.setObjectId(objectId);
    }

    @When("I update the object name to {string}")
    public void i_update_the_object(String newName) {
        objectRequest.setName(newName);
        String token = testContext.getToken();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(objectRequest)
                .when()
                .put("/webhook/api/objects/" + testContext.getObjectId())
                .then()
                .extract()
                .response();

        testContext.setLastResponse(response);
    }

    @When("I delete the object")
    public void i_delete_the_object() {
        String token = testContext.getToken();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/webhook/api/objects/" + testContext.getObjectId())
                .then()
                .extract()
                .response();

        testContext.setLastResponse(response);
    }

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(int statusCode) {
    // testContext.getLastResponse().then().statusCode(statusCode);
    // }
}