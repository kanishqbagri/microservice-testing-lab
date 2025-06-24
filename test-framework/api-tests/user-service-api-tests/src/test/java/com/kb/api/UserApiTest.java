package com.kb.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
    }

    @Test
    @Order(1)
    void shouldRegisterUserSuccessfully() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "password123"
                }
                """)
        .when()
            .post("/api/users/register")
        .then()
            .statusCode(200)
            .body("email", equalTo("alice@example.com"))
            .body("name", equalTo("Alice"))
            .body("id", notNullValue());
    }

    @Test
    @Order(2)
    void shouldRejectDuplicateEmail() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "password123"
                }
                """)
        .when()
            .post("/api/users/register")
        .then()
            .statusCode(400)
            .body(containsString("Email already registered"));
    }
}
