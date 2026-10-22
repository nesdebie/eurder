package org.example.eurder.api.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.Assertions;
import org.example.eurder.api.dto.ItemDto;
import org.example.eurder.api.dto.user.UserDto;
import org.example.eurder.api.request.PostItemRequest;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.api.request.PutItemRequest;
import org.example.eurder.repository.ItemRepository;
import org.example.eurder.service.ItemInventory;
import org.example.eurder.service.exception.ForbiddenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {
    @LocalServerPort
    private int port;

    private record ErrorResponse(
            String timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}

    private PostItemRequest createPost() {
        return new PostItemRequest(
                "Batmobile",
                "Lego Batman",
                19.99,
                15
        );
    }

    private PutItemRequest createPut(UUID id) {
        return new PutItemRequest(
                id,
                "Jokermobile",
                "Lego Joke",
                1.99,
                1
        );
    }

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

    private ValidatableResponse putItem(PutItemRequest request) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "admin@admin.com")
                .body(request)
                .port(port)
                .when()
                .put("/item")
                .then();
    }

    private ItemDto[] getItems() {
        return RestAssured
                .given()
                .baseUri("http://localhost:" + port)
                .when()
                .get("/item")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ItemDto[].class);
    }

    private ItemDto[] getItemsOverview(String filter) {
        String url = "/item/overview" + (filter == null ? "" : "/" + filter);

        return RestAssured
                .given()
                .baseUri("http://localhost:" + port)
                .header("Authorization", "admin@admin.com")
                .when()
                .get(url)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ItemDto[].class);
    }

    @DisplayName("Testing OK")
    @Nested
    class TestingOk {
        @DisplayName("Testing createItem")
        @Test
        void givenValidRequest_whenCallingCreateItem_thenAnObjectIsReturned() {
            ItemDto item = postItem(createPost())
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ItemDto.class);
            Assertions.assertThat(item.name()).isEqualTo("Batmobile");
        }

        @DisplayName("Testing updateItem")
        @Test
        void givenValidId_whenCallingUpdateItem_thenAnObjectIsReturned() {
            ItemDto item = postItem(createPost())
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(ItemDto.class);
            ItemDto updatedItem = putItem(createPut(item.id()))
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(ItemDto.class);
            
            Assertions.assertThat(updatedItem.id()).isEqualTo(item.id());
            Assertions.assertThat(updatedItem.name()).isEqualTo("Jokermobile");
        }

        @DisplayName("Testing getAllItems")
        @Test
        void given_whenCallingGetAllItems_thenAnArrayOfObjectIsReturned() {
            Assertions.assertThat(getItems().length).isEqualTo(3);
        }

        @DisplayName("Testing getItemsOverview no filter")
        @Test
        void givenNoFilter_whenCallingGetItemsOverview_thenAnArrayOfObjectIsReturned() {
            Assertions.assertThat(getItemsOverview(null).length).isEqualTo(3);
        }

        @DisplayName("Testing getItemsOverview low filter")
        @Test
        void givenLowFilter_whenCallingGetItemsOverview_thenAnArrayOfObjectIsReturned() {
            Assertions.assertThat(getItemsOverview("STOCK_LOW").length).isEqualTo(1);
        }

        @DisplayName("Testing getItemsOverview medium filter")
        @Test
        void givenMediumFilter_whenCallingGetItemsOverview_thenAnArrayOfObjectIsReturned() {
            Assertions.assertThat(getItemsOverview("STOCK_MEDIUM").length).isEqualTo(1);
        }

        @DisplayName("Testing getItemsOverview high filter")
        @Test
        void givenHighFilter_whenCallingGetItemsOverview_thenAnArrayOfObjectIsReturned() {
            Assertions.assertThat(getItemsOverview("STOCK_HIGH").length).isEqualTo(1);
        }
    }

    @Nested
    class TestingBadRequest {
        private ErrorResponse getItemsOverviewWrongFilter(String filter) {
            return RestAssured
                    .given()
                    .baseUri("http://localhost:" + port)
                    .header("Authorization", "admin@admin.com")
                    .when()
                    .get("/item/overview/" + filter)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .as(ErrorResponse.class);
        }

        @DisplayName("Testing getItemsOverview wrong filter")
        @Test
        void givenHighFilter_whenCallingGetItemsOverview_thenAnArrayOfObjectIsReturned() {
            String wrong = "WRONG_FILTER";
            Assertions.assertThat(getItemsOverviewWrongFilter(wrong).status).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat(getItemsOverviewWrongFilter(wrong).error).isEqualTo("Bad Request");
            Assertions.assertThat(getItemsOverviewWrongFilter(wrong).message).isEqualTo("Filter %s does not exist".formatted(wrong));
        }

    }

    @DisplayName("Testing Forbidden")
    @Nested
    class TestingForbidden {
        private ErrorResponse getItemsOverviewBadCredentials(String filter) {
            return RestAssured
                    .given()
                    .baseUri("http://localhost:" + port)
                    .header("Authorization", "client@client.com")
                    .when()
                    .get("/item/overview/" + filter)
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .extract()
                    .as(ErrorResponse.class);
        }

        @DisplayName("Testing getItemsOverview")
        @Test
        void givenWrongCredentials_whenCallingGetItemsOverview_thenAnExceptionIsThrown() {
            Assertions.assertThat(getItemsOverviewBadCredentials(null).status()).isEqualTo(HttpStatus.FORBIDDEN.value());
            Assertions.assertThat(getItemsOverviewBadCredentials(null).error()).isEqualTo("Forbidden");
            Assertions.assertThat(getItemsOverviewBadCredentials(null).message()).isEqualTo("You don't have permission to View item's amount");
        }

        private ErrorResponse postItemBadCredentials(PostItemRequest request) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .header("Authorization", "client@client.com")
                    .body(request)
                    .port(port)
                    .when()
                    .post("/item")
                    .then()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .extract()
                    .as(ErrorResponse.class);
        }
        @DisplayName("Testing creatItem")
        @Test
        void givenWrongCredentials_whenCallingCreateItem_thenAnExceptionIsThrown() {
            Assertions.assertThat(postItemBadCredentials(createPost()).status()).isEqualTo(HttpStatus.FORBIDDEN.value());
            Assertions.assertThat(postItemBadCredentials(createPost()).error()).isEqualTo("Forbidden");
            Assertions.assertThat(postItemBadCredentials(createPost()).message()).isEqualTo("You don't have permission to Add item");
        }
    }

    @Nested
    class TestingUnauthorized {
        private ErrorResponse getItemsOverviewNoCredentials(String filter) {
            return RestAssured
                    .given()
                    .baseUri("http://localhost:" + port)
                    .when()
                    .get("/item/overview/" + filter)
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .as(ErrorResponse.class);
        }

        @DisplayName("Testing getItemsOverview")
        @Test
        void givenWrongCredentials_whenCallingGetItemsOverview_thenAnExceptionIsThrown() {
            Assertions.assertThat(getItemsOverviewNoCredentials(null).status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            Assertions.assertThat(getItemsOverviewNoCredentials(null).error()).isEqualTo("Unauthorized");
            Assertions.assertThat(getItemsOverviewNoCredentials(null).message()).isEqualTo("Unauthorized");
        }

        private ErrorResponse postItemNoCredentials(PostItemRequest request) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(request)
                    .port(port)
                    .when()
                    .post("/item")
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .extract()
                    .as(ErrorResponse.class);
        }
        @DisplayName("Testing creatItem")
        @Test
        void givenWrongCredentials_whenCallingCreateItem_thenAnExceptionIsThrown() {
            Assertions.assertThat(postItemNoCredentials(createPost()).status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            Assertions.assertThat(postItemNoCredentials(createPost()).error()).isEqualTo("Unauthorized");
            Assertions.assertThat(postItemNoCredentials(createPost()).message()).isEqualTo("Unauthorized");
        }
    }

    @Nested
    class TestingNotFound {
        @DisplayName("Testing updateItem")
        @Test
        void givenValidId_whenCallingUpdateItem_thenAnObjectIsReturned() {
            UUID randomId = UUID.randomUUID();
            ErrorResponse updatedItem = putItem(createPut(randomId))
                    .assertThat()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .extract()
                    .as(ErrorResponse.class);

            Assertions.assertThat(updatedItem.status).isEqualTo(HttpStatus.NOT_FOUND.value());
            Assertions.assertThat(updatedItem.error).isEqualTo("Not Found");
            Assertions.assertThat(updatedItem.message).isEqualTo("Item with ID %s not found".formatted(randomId));
        }
    }
}