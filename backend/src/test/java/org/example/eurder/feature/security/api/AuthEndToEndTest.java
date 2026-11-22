package org.example.eurder.feature.security.api;

import org.example.eurder.common.EndToEndTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Auth")
class AuthEndToEndTest extends EndToEndTest {

    @Test
    @DisplayName("Valid customer credentials return the user and its permissions")
    void givenCustomer_whenGettingMe_thenReturnsEmailAndPermissions() {
        String email = newCustomer();

        as(email).get("/auth/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(email))
                .body("permissions", hasItem("EURDER_ITEMS"))
                .body("permissions", not(hasItem("ADD_ITEM")));
    }

    @Test
    @DisplayName("Valid admin credentials return admin permissions")
    void givenAdmin_whenGettingMe_thenReturnsAdminPermissions() {
        as(newAdmin()).get("/auth/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("permissions", hasItem("ADD_ITEM"));
    }

    @Test
    @DisplayName("Wrong password from the browser: 401 without Basic challenge")
    void givenWrongPasswordFromAjax_whenGettingMe_thenUnauthorizedWithoutChallenge() {
        String email = newCustomer();

        anonymous().auth().preemptive().basic(email, "wrong-password")
                .header("X-Requested-With", "XMLHttpRequest")
                .get("/auth/me")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .header("WWW-Authenticate", nullValue());
    }

    @Test
    @DisplayName("Wrong password from another client: 401 with Basic challenge")
    void givenWrongPassword_whenGettingMe_thenUnauthorizedWithChallenge() {
        String email = newCustomer();

        anonymous().auth().preemptive().basic(email, "wrong-password")
                .get("/auth/me")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .header("WWW-Authenticate", equalTo("Basic realm=\"Eurder\""));
    }
}
