package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.dto.UserRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserSteps {

    private UserRequest user;
    private Response response;

    @Given("I prepare a user registration with email {string} and password {string}")
    public void i_prepare_a_user_registration(String email, String password) {
        user = new UserRequest();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName("Baba QA");
        user.setDepartment("QA");
        user.setPhoneNumber("08123456789");
    }

    @When("I send a POST request to {string}")
    public void i_send_post_request(String endpoint) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        response = given()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(endpoint);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the response should contain email {string}")
    public void the_response_should_contain_email(String expectedEmail) {
        String actualEmail = response.jsonPath().getString("email");
        assertThat(actualEmail, equalTo(expectedEmail));
    }

    @Given("I have registered user {string} with password {string}")
    public void i_have_registered_user(String email, String password) {
        user = new UserRequest();
        user.setEmail(email);
        user.setPassword(password);
    }

    @Then("the response should contain a valid token")
    public void the_response_should_contain_a_valid_token() {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/login-schema.json"));
        String token = response.jsonPath().getString("token");
        assertThat(token, not(emptyOrNullString()));
    }
}