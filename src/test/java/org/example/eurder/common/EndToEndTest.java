package org.example.eurder.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.customer.domain.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Base class of the end-to-end tests: a real server on a random port, real HTTP calls,
 * real HTTP Basic login. Only the clock is replaced, to control the shipping dates.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(EndToEndTest.TestClockConfig.class)
public abstract class EndToEndTest {

    protected static final ZonedDateTime NOW = ZonedDateTime.of(2026, 11, 9, 10, 0, 0, 0, ZoneId.of("Europe/Brussels"));

    @TestConfiguration
    static class TestClockConfig {
        @Bean
        @Primary
        MutableClock testClock() {
            return new MutableClock(NOW);
        }
    }

    @LocalServerPort
    private int port;
    @Autowired
    protected MutableClock clock;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void resetClock() {
        clock.setTo(NOW);
    }

    protected RequestSpecification anonymous() {
        return RestAssured.given().port(port).contentType(ContentType.JSON);
    }

    protected RequestSpecification as(String email) {
        return anonymous().auth().preemptive().basic(email, TestData.PASSWORD);
    }

    /** The admin account comes from a PostgreSQL-only migration: on H2, each test creates its own. */
    protected String newAdmin() {
        return customerRepository.save(TestData.admin(passwordEncoder.encode(TestData.PASSWORD))).getEmail();
    }

    protected String newCustomer() {
        String email = TestData.uniqueEmail();
        anonymous().body(customerJson(email)).post("/customers")
                .then().statusCode(HttpStatus.CREATED.value());
        return email;
    }

    protected Customer findCustomer(String email) {
        return customerRepository.findByEmail(email).orElseThrow();
    }

    protected static String customerJson(String email) {
        return """
                {
                  "fullName": { "firstName": "John", "lastName": "Doe" },
                  "email": "%s",
                  "password": "%s",
                  "phoneNumber": "+32 470 12 34 56",
                  "address": { "streetName": "Rue de la Loi", "streetNumber": "16", "postalCode": "1000", "city": "Brussels" }
                }
                """.formatted(email, TestData.PASSWORD);
    }

    protected static String itemJson(String name, String price, int amount) {
        return """
                { "name": "%s", "description": "A description", "price": %s, "amount": %d }
                """.formatted(name, price, amount);
    }
}
