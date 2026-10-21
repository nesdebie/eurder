package org.example.eurder.api.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.example.eurder.api.dto.ItemDto;
import org.example.eurder.api.request.PostEurderRequest;
import org.example.eurder.api.request.PostItemRequest;
import org.example.eurder.api.request.PutItemRequest;
import org.example.eurder.domain.Eurder;
import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.User;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EurderControllerTest {
    @LocalServerPort
    private int port;
    private UUID id;

    private record ErrorResponse(
            String timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}

    private ValidatableResponse postItem(PostItemRequest request) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "admin@admin.com")
                .body(request)
                .port(port)
                .when()
                .post("/item")
                .then();
    }
    private PostItemRequest createPost() {
        return new PostItemRequest(
                "Jokermobile",
                "Lego Joke",
                1.99,
                1
        );
    }
    @BeforeEach
    void init() {
        id = postItem(createPost()).assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDto.class).id();
    }
    @DisplayName("Testing OK")
    @Nested
    class TestingOk {
        private ValidatableResponse postEurder(PostEurderRequest request) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .header("Authorization", "client@client.com")
                    .body(request)
                    .port(port)
                    .when()
                    .post("/eurder")
                    .then();
        }
        @Test
        void givenExistingItem_whenPostEurder_thenCreated() {
            PostEurderRequest request = new PostEurderRequest(
                    List.of(new PostEurderRequest.OrderLineDto(id, 5))
            );

            postEurder(request)
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value());
        }
    }
}