package cucumber.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.context.TestContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;

@RequiredArgsConstructor
public class ObjectSteps {

    private final TestContext testContext;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Object> objectPayload = new HashMap<>();
    private String objectId;

    @Given("I prepare a new object with name {string}")
    public void i_prepare_a_new_object(String objectName) {
        objectPayload.clear();
        objectPayload.put("name", objectName);
    }

    @And("I save the object ID")
    public void i_save_the_object_id() {
        Response response = testContext.getResponse();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.getBody().asString());
            objectId = jsonNode.get("data").get("id").asText();
            testContext.setObjectId(objectId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse response to get object ID", e);
        }
        assertNotNull(objectId, "Object ID should not be null");
    }

    @When("I update the object name to {string}")
    public void i_update_the_object(String newName) {
        objectPayload.put("name", newName);
        String token = testContext.getToken();
        String id = testContext.getObjectId();
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(objectPayload)
                .when()
                .put("/webhook/api/objects/" + id)
                .then()
                .extract()
                .response();
        testContext.setLastResponse(response);
    }

    @When("I delete the object")
    public void i_delete_the_object() {
        String token = testContext.getToken();
        String id = testContext.getObjectId();
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/webhook/api/objects/" + id)
                .then()
                .extract()
                .response();
        testContext.setLastResponse(response);
    }
}
