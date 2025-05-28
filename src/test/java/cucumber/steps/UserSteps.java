package cucumber.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    private final TestContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserSteps(TestContext context) {
        this.context = context;
    }

    @Given("I prepare a user registration with email {string} and password {string}")
    public void i_prepare_a_user_registration(String email, String password) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        context.setRequestBody(requestBody);
    }

    @Given("I have registered user {string} with password {string}")
    public void i_have_registered_user(String email, String password) {
        i_prepare_a_user_registration(email, password);
        i_send_post_request("/webhook/api/register");
        the_response_status_should_be(200);
    }

    @When("I send a POST request to {string}")
    public void i_send_post_request(String path) {
        Object body = context.getRequestBody();
        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(context.getBaseUrl() + path)
                .then()
                .extract()
                .response();

        System.out.println("@@@@@@@@ FULL URL = " + context.getBaseUrl() + path);
        System.out.println("@@@@@@@@ RESPONSE BODY = " + response.getBody().asString());
        context.setLastResponse(response);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatusCode) {
        assertThat(context.getLastResponse().getStatusCode(), is(expectedStatusCode));
    }

    @And("the response should contain email {string}")
    public void the_response_should_contain_email(String expectedEmail) throws IOException {
        JsonNode root = objectMapper.readTree(context.getLastResponse().getBody().asString());
        assertThat(root.path("email").asText(), equalTo(expectedEmail));
    }

    @Then("the response should contain a valid token")
    public void the_response_should_contain_a_valid_token() throws IOException {
        JsonNode root = objectMapper.readTree(context.getLastResponse().getBody().asString());
        String token = root.path("token").asText();
        assertThat(token, not(isEmptyOrNullString()));
        context.setToken(token);
    }
}