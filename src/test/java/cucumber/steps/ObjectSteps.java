package cucumber.steps;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ObjectSteps {

    private Map<String, Object> objectData = new HashMap<>();
    private String objectId;
    private Response response;

    @Given("I prepare a new object with name {string}")
    public void i_prepare_a_new_object(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("year", 2024);
        data.put("price", 1999.99);
        data.put("cpu_model", "Intel Core i9");
        data.put("hard_disk_size", "1 TB");
        data.put("capacity", "16 GB");
        data.put("screen_size", "16 Inch");
        data.put("color", "silver");

        objectData.put("name", name);
        objectData.put("data", data);
    }

    // @When("I send a POST request to {string}")
    // public void i_send_post_to(String endpoint) {
    // response = given()
    // .header("Content-Type", "application/json")
    // .body(objectData)
    // .when()
    // .post(endpoint);
    // }

    @Then("I save the object ID")
    public void i_save_the_object_id() {
        objectId = response.jsonPath().getString("id");
        assertThat(objectId, not(emptyOrNullString()));
    }

    @When("I update the object name to {string}")
    public void i_update_the_object(String newName) {
        objectData.put("name", newName);

        response = given()
                .header("Content-Type", "application/json")
                .body(objectData)
                .when()
                .put("/webhook/api/objects/" + objectId);
    }

    @When("I delete the object")
    public void i_delete_the_object() {
        response = given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/webhook/api/objects/" + objectId);
    }

    // @Then("the response status should be {int}")
    // public void the_response_status_should_be(int expectedStatusCode) {
    // response.then().statusCode(expectedStatusCode);
    // }
}