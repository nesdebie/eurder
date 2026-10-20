package org.example.eurder.service;

import org.assertj.core.api.Assertions;
import org.example.eurder.api.request.PostItemRequest;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.domain.Item;
import org.example.eurder.repository.ItemRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {
    private ItemService service;

    @BeforeEach
    void init() {
        service = new ItemService(new ItemRepository());
    }

    @Test
    void givenValidPostItemRequest_whenCallingCreateRequest_thenAnItemObjectIsReturned() {
        PostItemRequest request = new PostItemRequest(
                "DA PRODUCT",
                "DA PRODUCT IS GOOD",
                9.99,
                10000
        );
        Assertions.assertThat(service.createItem(request)).isInstanceOf(Item.class);
    }

    @Test
    void givenValidPostItemRequestTwice_whenCallingCreateRequest_thenAnExceptionIsThrowned() {
        PostItemRequest request = new PostItemRequest(
                "DA PRODUCT",
                "DA PRODUCT IS GOOD",
                9.99,
                10000
        );
        service.createItem(request);
        Assertions.assertThatThrownBy(() -> service.createItem(request)).isInstanceOf(BadRequestException.class);
    }

    public static Stream<Arguments> badRequestStream() {
        return Stream.of(
                Arguments.of("", "DA PRODUCT IS GOOD", 9.99, 10000),
                Arguments.of(null, "DA PRODUCT IS GOOD", 9.99, 10000),
                Arguments.of("DA PRODUCT", "", 9.99, 10000),
                Arguments.of("DA PRODUCT", null, 9.99, 10000),
                Arguments.of("DA PRODUCT", "DA PRODUCT IS GOOD", -9.99, 10000),
                Arguments.of("DA PRODUCT", "DA PRODUCT IS GOOD", 9.99, 0)
        );
    }

    @ParameterizedTest(name = "Name: {0} | {1} | Price: {2} | Amount: {3}")
    @DisplayName("Create Item with invalid/empty/null field in the request")
    @MethodSource("badRequestStream")
    void givenBadRequest_whenCallingCreateItem_thenAnExceptionIsThrown(
            String name,
            String description,
            double price,
            int amount
    ) {
        Assertions.assertThatThrownBy(() -> service.createItem(new PostItemRequest(
                name,
                description,
                price,
                amount
        ))).isInstanceOf(BadRequestException.class);
    }
}