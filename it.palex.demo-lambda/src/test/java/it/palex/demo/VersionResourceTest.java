package it.palex.demo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class VersionResourceTest {
    @Test
    void testVersionEndpoint() {
        given()
          .when().get("/version")
          .then()
             .statusCode(200)
             .body(is("1.0.0"));
    }

}