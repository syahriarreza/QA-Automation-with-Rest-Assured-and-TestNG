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
import cucumber.client.Endpoints;

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
        objectRequest.setData(new ObjectRequest.Data());
        objectRequest.getData().setYear(2024);
        objectRequest.getData().setPrice(1000.0);
        objectRequest.getData().setCpu_model("Intel Core i7");
        objectRequest.getData().setHard_disk_size("1 TB");
        objectRequest.getData().setCapacity("16 GB");
        objectRequest.getData().setScreen_size("15.6 inches");
        objectRequest.getData().setColor("Silver");
        testContext.setRequestBody(objectRequest);
    }

    @And("I save the object ID")
    public void i_save_the_object_id() {
        Response response = testContext.getLastResponse();
        objectId = response.jsonPath().getString("id");
        assertThat("Object ID should not be null", objectId, not(emptyOrNullString()));
        testContext.setObjectId(objectId);
    }

    @When("I update the object name to {string}")
    public void i_update_the_object(String newName) {
        objectRequest.setName(newName);
        String token = testContext.getToken();
        Response response = Endpoints.updateObject(objectRequest, token, testContext.getObjectId());
        testContext.setLastResponse(response);
    }

    @When("I delete the object")
    public void i_delete_the_object() {
        String token = testContext.getToken();
        Response response = Endpoints.deleteObject(token, testContext.getObjectId());
        testContext.setLastResponse(response);
    }

    @When("I add the object")
    public void i_add_the_object() {
        ObjectRequest objectRequest = (ObjectRequest) testContext.getRequestBody();
        String token = testContext.getToken();
        Response response = Endpoints.createObject(objectRequest, token);
        testContext.setLastResponse(response);
    }
}