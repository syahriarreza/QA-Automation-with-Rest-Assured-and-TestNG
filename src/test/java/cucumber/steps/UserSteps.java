package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.context.TestContext;
import cucumber.dto.UserRequest;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import cucumber.dto.LoginResponse;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    private final TestContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserRequest request;

    public UserSteps(TestContext context) {
        this.context = context;
    }

    @Given("I prepare a user registration with email {string} and password {string}")
    public void i_prepare_a_user_registration(String email, String password) {
        // email.split("@");
        // String[] parts = email.split("@");
        // String username = parts[0];
        // String domain = parts[1];
        // username = username + "_" + UUID.randomUUID();
        // email = username + "@" + domain;

        request = new UserRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setFull_name("Baba QA " + UUID.randomUUID());
        request.setDepartment("Technology");
        request.setPhone_number("081234567890");
        context.setRequestBody(request);
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
        String token = context.getToken();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(context.getBaseUrl() + path)
                .then()
                .extract()
                .response();

        if (token != null) {
            response.then().header("Authorization", not(emptyOrNullString()));
        }

        System.out.println("@@@@@@ FULL URL = " + context.getBaseUrl() + path);
        System.out.println("@@@@@@ TOKEN = " + token);
        System.out.println("@@@@@@ REQUEST BODY = " + context.getRequestBody());
        System.out.println("@@@@@@ RESPONSE BODY = " + response.getBody().asString());
        context.setLastResponse(response);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatusCode) {
        assertThat(context.getLastResponse().getStatusCode(), is(expectedStatusCode));
    }

    @Then("the response should contain email {string}")
    public void the_response_should_contain_email(String expectedEmail) {
        String actualEmail = context.getLastResponse().jsonPath().getString("email");
        assertThat(actualEmail, equalTo(expectedEmail));
    }

    @Then("the response should contain a valid token")
    public void the_response_should_contain_a_valid_token() {
        LoginResponse loginResponse = context.getLastResponse().as(LoginResponse.class);
        assertThat(loginResponse.getToken(), not(emptyOrNullString()));
        context.setToken(loginResponse.getToken());
    }
}