package org.example.eurder.api.controller;

import org.assertj.core.api.Assertions;
import org.example.eurder.api.request.PostCustomerRequest;
import org.example.eurder.domain.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerControlerTest {
    @LocalServerPort
    private int port;

    private record ErrorResponse(
            String timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
    }

    private ValidatableResponse postUser(PostCustomerRequest request) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .port(port)
                .when()
                .post("/customer")
                .then();
    }

    @Test
    @DisplayName("Adding a Customer successfully returns the correct response")
    void postUser_givenRepositoryIsEmpty_thenReturnCreatedObject() {
        Customer customer = postUser(new PostCustomerRequest(
                "Sherlock",
                "Holmes",
                "sherlock.holmes@detective.com",
                "0000000000",
                "Baker Street",
                "221B",
                "London",
                "United Kingdom"
        ))
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(Customer.class);

        Assertions.assertThat(customer.getFirstName()).isEqualTo("Sherlock");
    }

    @Test
    @DisplayName("Adding a customer with empty email returns the correct response")
    void postUser_givenEmailEmpty_thenThrowsException() {
        ErrorResponse response = postUser(new PostCustomerRequest(
                "Sherlock",
                "Holmes",
                "",
                "0000000000",
                "Baker Street",
                "221B",
                "London",
                "United Kingdom"
        ))
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().as(ErrorResponse.class);

        Assertions.assertThat(response.message).isEqualTo("Valid Customer's email is required");
    }
}