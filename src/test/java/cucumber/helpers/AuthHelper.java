package cucumber.helpers;

import cucumber.context.TestContext;
import cucumber.dto.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthHelper {

    public static void loginAndSetToken(TestContext testContext) {
        if (testContext.getToken() == null) {
            LoginRequest loginRequest = new LoginRequest("baba@example.com", "@dmin123");

            Response response = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(loginRequest)
                    .when()
                    .post("/webhook/api/login");

            response.then().statusCode(200);
            String token = response.jsonPath().getString("token");
            testContext.setToken(token);
        }
    }
}