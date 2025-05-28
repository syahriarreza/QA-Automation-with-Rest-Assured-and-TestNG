package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.client.Endpoints;
import cucumber.context.TestContext;
import cucumber.dto.UserRequest;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
        i_register_the_user();
        the_response_status_should_be(200);
    }

    @When("I register the user")
    public void i_register_the_user() {
        Response response = Endpoints.register((UserRequest) context.getRequestBody());
        context.setLastResponse(response);
    }

    @When("I send a POST request to {string}")
    public void i_send_post_request(String path) {
        Object body = context.getRequestBody();
        String token = context.getToken();

        RequestSpecification resSpec = given();

        if (token != null) {
            resSpec.header("Authorization", "Bearer " + token);
        }

        Response response = resSpec
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path)
                .then()
                .extract()
                .response();

        System.out.println("\t@@@@@@ POST URL = " + path);
        System.out.println("\t@@@@@@ TOKEN = " + token);
        System.out.println("\t@@@@@@ REQUEST BODY = " + context.getRequestBody());
        System.out.println("\t@@@@@@ RESPONSE BODY = " + response.getBody().asString());
        context.setLastResponse(response);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatusCode) {
        try {
            assertThat(context.getLastResponse().getStatusCode(), is(expectedStatusCode));
        } catch (AssertionError e) {
            System.out.println("\\t@@ ERROOOOOOR Response Body: " + context.getLastResponse().getBody().asString());
            throw e;
        }
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

    @Given("I am logged in as a valid user with email {string} and password {string}")
    public void i_am_logged_in_as_a_valid_user_with_email_and_password(String email, String password) {
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Response response = Endpoints.login(loginRequest);
        assertThat("Login should be successful", response.getStatusCode(), equalTo(200));

        String token = response.jsonPath().getString("token");
        assertThat("Token should not be null", token, not(emptyOrNullString()));

        context.setToken(token);
    }

    @And("I have a valid authentication token")
    public void i_have_a_valid_authentication_token() {
        String token = context.getToken();
        assertThat("Authentication token should not be null or empty", token, not(emptyOrNullString()));
    }

    @When("I login")
    public void i_login() {
        UserRequest loginRequest = (UserRequest) context.getRequestBody();
        Response response = Endpoints.login(loginRequest);
        context.setLastResponse(response);
    }
}