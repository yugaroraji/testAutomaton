package org.example;
import io.restassured.*;
//import org.json.JSONObject;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;

public class TestGitAPIs {


    String public_Token = "Bearer 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";
    String public_Token_nopermission = "Bearer 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";
    String invalid_token = "Bearer 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a";

    @Test(priority = 1,enabled = true,description = "Users API with No token provided")
    public void noTokenUserAPI(){
        RestAssured.baseURI ="https://api.github.com";
        Response usersResponse = given().log().uri()
                        .header("Accept","application/vnd.github+json")
                        .header("X-GitHub-Api-Version","2022-11-28")
                .when()
                .get("user").then().extract().response();
        JsonPath jp = new JsonPath(usersResponse.toString());
        int statusCodeCheck = usersResponse.statusCode();
        System.out.println(statusCodeCheck);
        Assert.assertEquals(statusCodeCheck, 401);

    }
    @Test(priority = 2,enabled = true,description = "Users API with Wrong/Invalid token provided")
    public void invalidTokenUserAPI(){
        RestAssured.baseURI ="https://api.github.com";
        Response usersResponse_invalid = given().log().uri()
                .header("Accept","application/vnd.github+json")
                .header("X-GitHub-Api-Version","2022-11-28")
                .header("Authorization",invalid_token)
                .when()
                .get("user").then().extract().response();
        JsonPath jp = new JsonPath(usersResponse_invalid.asString());
        int statusCodeCheck = usersResponse_invalid.statusCode();
        System.out.println("Status Code for API is: "+statusCodeCheck);
        String respPrint = jp.getString("");
        System.out.println("Complete Response: "+respPrint);
        String errorMessage = jp.getString("message");
        String docUrl = jp.getString("documentation_url");
        Assert.assertEquals(statusCodeCheck, 401);
        System.out.println(errorMessage);
        System.out.println(docUrl);
        Assert.assertEquals(errorMessage,"Bad credentials");

    }
    @Test(priority = 3,enabled = true,description = "Users API with Forbidden request token provided")
    public void invalidTokenUserForbidden(){
        RestAssured.baseURI ="http://api.github.com";
        Response usersResponse_invalid = given().log().uri()
                .header("Accept","application/vnd.github+json")
                .header("X-GitHub-Api-Version","2022-11-28")
                .header("Authorizatio",public_Token_nopermission)
                .when()
                .get("user").then().extract().response();
        JsonPath jp = new JsonPath(usersResponse_invalid.asString());
        int statusCodeCheck = usersResponse_invalid.statusCode();
        System.out.println("Status Code for API is: "+statusCodeCheck);
        String respPrint = jp.getString("");
        System.out.println("Complete Response: "+respPrint);
        String errorMessage = jp.getString("message");
        String docUrl = jp.getString("documentation_url");
        Assert.assertEquals(statusCodeCheck, 403);
        System.out.println(errorMessage);
        System.out.println(docUrl);
       // Assert.assertEquals(errorMessage,"Bad credentials");

    }


    @Test(priority = 4,enabled = true,description = "Users API validation with Valid token provided")
    public void validUserTokenUserAPI(){
        RestAssured.baseURI ="https://api.github.com";
        Response usersResponseValid = given().log().uri()
                .header("Accept","application/vnd.github+json")
                .header("X-GitHub-Api-Version","2022-11-28")
                .header("Authorization",public_Token)
                .when()
                .get("user").then().extract().response();
        JsonPath jp = new JsonPath(usersResponseValid.asString());
        int statusCodeCheck = usersResponseValid.statusCode();
        System.out.println("Status Code for API is: "+statusCodeCheck);
        String respPrint = jp.getString("");
        System.out.println("Complete Response: "+respPrint);
        String login = jp.getString("login");
       String avatar_url = jp.getString("avatar_url");
       String url = jp.getString("url");
       String html_url = jp.getString("html_url");
       int id = jp.getInt("id");
        System.out.println(login);
        System.out.println(avatar_url);
        System.out.println(url);
        System.out.println(html_url);
        System.out.println(id);
        Assert.assertEquals(statusCodeCheck, 200);

    }

    @Test(priority = 5,enabled = true,description = "Update users BIO in API PATCH reqeust with Valid token provided")
    public void updateValidUserTokenUserAPI(){
       // RestAssured.baseURI ="https://api.github.com/user";
        Response usersResponseValid = given().log().uri()
                .header("Accept","application/vnd.github+json")
                .header("X-GitHub-Api-Version","2022-11-28")
                .header("Authorization",public_Token)
                .contentType(ContentType.JSON)
                .body("{\"bio\": \"Please Check Your BIO is updated\"}")
                .when()
                .patch("https://api.github.com/user").then().log().all()
                .extract().response();
        JsonPath jp = new JsonPath(usersResponseValid.asString());
        int statusCodeCheck = usersResponseValid.statusCode();
        System.out.println("Status Code for API is: "+statusCodeCheck);
        String respPrint = jp.getString("");

    }
}
