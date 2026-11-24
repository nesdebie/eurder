package org.example.eurder.feature.customer.api;

import org.example.eurder.common.EndToEndTest;
import org.example.eurder.common.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@DisplayName("My profile (/customers/me)")
class CustomerProfileEndToEndTest extends EndToEndTest {

    private static String profileJson(String email, String city) {
        return """
                {
                  "fullName": { "firstName": "Jane", "lastName": "Smith" },
                  "email": "%s",
                  "phoneNumber": "+32 471 00 00 00",
                  "address": { "streetName": "Rue Neuve", "streetNumber": "1", "postalCode": "1000", "city": "%s" }
                }
                """.formatted(email, city);
    }

    @Test
    @DisplayName("A customer sees their own profile")
    void givenCustomer_whenGettingMe_thenReturnsOwnProfile() {
        String email = newCustomer();

        as(email).get("/customers/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(email))
                .body("fullName.firstName", equalTo("John"))
                .body("address.city", equalTo("Brussels"));
    }

    @Test
    @DisplayName("A customer updates every field, keeping the same email")
    void givenCustomer_whenUpdatingProfile_thenAllFieldsAreChanged() {
        String email = newCustomer();

        as(email).body(profileJson(email, "Ghent")).put("/customers/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("fullName.firstName", equalTo("Jane"))
                .body("fullName.lastName", equalTo("Smith"))
                .body("phone", equalTo("+32 471 00 00 00"))
                .body("address.city", equalTo("Ghent"));

        as(email).get("/customers/me").then().body("address.city", equalTo("Ghent"));
    }

    @Test
    @DisplayName("After changing the email, the customer signs in with the new one (same password)")
    void givenNewEmail_whenUpdatingProfile_thenNewEmailIsTheLogin() {
        String oldEmail = newCustomer();
        String newEmail = TestData.uniqueEmail();

        as(oldEmail).body(profileJson(newEmail, "Brussels")).put("/customers/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(newEmail));

        as(newEmail).get("/customers/me").then().statusCode(HttpStatus.OK.value());
        as(oldEmail).get("/customers/me").then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("The email of another account is refused")
    void givenEmailOfAnotherCustomer_whenUpdatingProfile_thenConflict() {
        String me = newCustomer();
        String someoneElse = newCustomer();

        as(me).body(profileJson(someoneElse, "Brussels")).put("/customers/me")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("detail", equalTo("Email " + someoneElse + " is already in use"));

        as(me).get("/customers/me").then().body("email", equalTo(me));
    }

    @Test
    @DisplayName("My own email written with capitals is not a conflict")
    void givenOwnEmailWithCapitals_whenUpdatingProfile_thenAccepted() {
        String email = newCustomer();

        as(email).body(profileJson(email.toUpperCase(), "Brussels")).put("/customers/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(email));
    }

    @Test
    @DisplayName("Invalid data is refused field by field")
    void givenInvalidData_whenUpdatingProfile_thenBadRequest() {
        String email = newCustomer();

        as(email).body(profileJson("not-an-email", "")).put("/customers/me")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasKey("email"))
                .body("errors", hasKey("address.city"));
    }

    @Test
    @DisplayName("An admin has no customer profile to manage")
    void givenAdmin_whenGettingMe_thenForbidden() {
        as(newAdmin()).get("/customers/me").then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Anonymous users cannot see or change a profile")
    void givenAnonymous_whenUpdatingProfile_thenUnauthorized() {
        anonymous().body(profileJson(TestData.uniqueEmail(), "Brussels")).put("/customers/me")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
