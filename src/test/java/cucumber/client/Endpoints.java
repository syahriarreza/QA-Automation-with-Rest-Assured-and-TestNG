package cucumber.client;

import cucumber.dto.ObjectRequest;
import cucumber.dto.UserRequest;
import cucumber.helpers.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Endpoints {

    public static Response register(UserRequest user) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/webhook/api/register");
    }

    public static Response login(UserRequest login) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("/webhook/api/login");
    }

    public static Response createObject(ObjectRequest object, String token) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(object)
                .when()
                .post("/webhook/api/objects");
    }

    public static Response updateObject(ObjectRequest object, String token, String id) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(object)
                .when()
                .put("/webhook/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/" + id);
    }

    public static Response deleteObject(String token, String id) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/" + id);
    }
}