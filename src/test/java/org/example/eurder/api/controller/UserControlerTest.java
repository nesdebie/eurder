package org.example.eurder.api.controller;

import org.assertj.core.api.Assertions;
import org.example.eurder.api.dto.user.UserDto;
import org.example.eurder.api.dto.user.detail.UserDetailsDto;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.domain.user.User;
import org.example.eurder.service.exception.ForbiddenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControlerTest {
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

    private ValidatableResponse postUser(PostUserRequest request) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .port(port)
                .when()
                .post("/user")
                .then();
    }


    @Test
    @DisplayName("Testing postUser")
    void givenValidRequest_whenCallingCreateUser_thenReturnCreatedObject() {
        UserDto customer = postUser(new PostUserRequest(
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
                .as(UserDto.class);

        Assertions.assertThat(customer.firstName()).isEqualTo("Sherlock");
    }

    @Test
    @DisplayName("Testing Bad Request")
    void givenInvalidRequest_whenCallingCreateUser_thenAnExceptionIsThrowned() {
        ErrorResponse response = postUser(new PostUserRequest(
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

    private UserDto[] getUsers() {
        return RestAssured
                .given()
                .baseUri("http://localhost:" + port)
                .header("Authorization", "admin@admin.com")
                .when()
                .get("/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDto[].class);
    }

    @Test
    @DisplayName("Testing getAllUsers")
    void given_whenCallingGetAllUsers_thenReturnAnArrayOfObjects() {
        int size = getUsers().length;
        postUser(new PostUserRequest(
                        "Sherlock",
                        "Holmes",
                        "sherlock.holmes@detective.com",
                        "0000000000",
                        "Baker Street",
                        "221B",
                        "London",
                        "United Kingdom"
                ));
        Assertions.assertThat(getUsers().length).isEqualTo(size + 1);
    }

    private UserDetailsDto getUser(UUID id) {
        return RestAssured
                .given()
                .baseUri("http://localhost:" + port)
                .header("Authorization", "admin@admin.com")
                .when()
                .get("/user/" + id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(UserDetailsDto.class);
    }

    @DisplayName("Testing valid getUser")
    @Test
    void givenValidId_whenCallingGetUser_thenAnObjectIsReturned() {
        UserDto customer = postUser(new PostUserRequest(
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
                .as(UserDto.class);
        Assertions.assertThat(getUser(customer.id()).user().firstName()).isEqualTo("Sherlock");
    }

    private ErrorResponse getUserBadId() {
        return RestAssured
                .given()
                .baseUri("http://localhost:" + port)
                .header("Authorization", "admin@admin.com")
                .when()
                .get("/user/" + UUID.randomUUID())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);
    }

    @DisplayName("Testing Not Found")
    @Test
    void givenInvalidId_whenCallingGetUser_thenAnErrorIsThrowned() {
        Assertions.assertThat(getUserBadId().status()).isEqualTo(HttpStatus.NOT_FOUND.value());
        Assertions.assertThat(getUserBadId().error()).isEqualTo("Not Found");
        Assertions.assertThat(getUserBadId().message()).isEqualTo("User not found");
    }


    @DisplayName("Testing forbidden exceptions")
    @Nested
    class CheckForbiddenException {
        private ErrorResponse getUsersBadCredentials() {
            return RestAssured
                    .given()
                    .baseUri("http://localhost:" + port)
                    .header("Authorization", "client@client.com")
                    .when()
                    .get("/user")
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .extract()
                    .as(ErrorResponse.class);
        }

        @DisplayName("Get all customers")
        @Test
        void givenBadCredentials_whenCallingGetAllUsers_thenAnExceptionIsThrowned() {
            Assertions.assertThat(getUsersBadCredentials().status()).isEqualTo(HttpStatus.FORBIDDEN.value());
            Assertions.assertThat(getUsersBadCredentials().error()).isEqualTo("Forbidden");
            Assertions.assertThat(getUsersBadCredentials().message()).isEqualTo("You don't have permission to View all customers");
        }


        private ErrorResponse getUserBadCredentials(UUID id) {
            return RestAssured
                    .given()
                    .baseUri("http://localhost:" + port)
                    .header("Authorization", "client@client.com")
                    .when()
                    .get("/user/" + id)
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .extract()
                    .as(ErrorResponse.class);
        }

        @DisplayName("Get a single customer")
        @Test
        void givenInvalidCredentials_whenCallingGetUser_thenAnErrorIsThrowned() {
            UserDto customer = postUser(new PostUserRequest(
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
                    .as(UserDto.class);
            Assertions.assertThat(getUserBadCredentials(customer.id()).status()).isEqualTo(HttpStatus.FORBIDDEN.value());
            Assertions.assertThat(getUserBadCredentials(customer.id()).error()).isEqualTo("Forbidden");
            Assertions.assertThat(getUserBadCredentials(customer.id()).message()).isEqualTo("You don't have permission to View a single customer");
        }
    }

    @DisplayName("Testing Unauthorized exceptions")
    @Nested
    class CheckUnAuthorizedException {
        private ErrorResponse getUsersNoCredentials() {
            return RestAssured
                    .given()
                    .baseUri("http://localhost:" + port)
                    .when()
                    .get("/user")
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .as(ErrorResponse.class);
        }

        @DisplayName("Get all customers")
        @Test
        void givenNoCredentials_whenCallingGetAllUsers_thenAnExceptionIsThrowned() {
            Assertions.assertThat(getUsersNoCredentials().status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            Assertions.assertThat(getUsersNoCredentials().error()).isEqualTo("Unauthorized");
            Assertions.assertThat(getUsersNoCredentials().message()).isEqualTo("Unauthorized");
        }

        private ErrorResponse getUserNoCredentials(UUID id) {
            return RestAssured
                    .given()
                    .baseUri("http://localhost:" + port)
                    .when()
                    .get("/user")
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .as(ErrorResponse.class);
        }

        @DisplayName("Get a single customer")
        @Test
        void givenNoCredentials_whenCallingGetUser_thenAnExceptionIsThrowned() {
            UserDto customer = postUser(new PostUserRequest(
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
                    .as(UserDto.class);
            Assertions.assertThat(getUserNoCredentials(customer.id()).status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            Assertions.assertThat(getUserNoCredentials(customer.id()).error()).isEqualTo("Unauthorized");
            Assertions.assertThat(getUserNoCredentials(customer.id()).message()).isEqualTo("Unauthorized");
        }
    }
}