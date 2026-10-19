package org.example.eurder.service;

import org.assertj.core.api.Assertions;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.domain.user.User;
import org.example.eurder.repository.UserRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class UserServiceTest {
    private final UserService service = new UserService(new UserRepository());

    @BeforeEach

    @Test
    void givenValidPostCustomerRequest_whenCallingCreateCustomer_thenACustomerObjectIsReturned() {
        PostUserRequest request = new PostUserRequest(
                "Sherlock",
                "Holmes",
                "sherlock.holmes@detective.com",
                "0000000000",
                "Baker Street",
                "221B",
                "London",
                "United Kingdom"
        );

        Assertions.assertThat(service.createCustomer(request)).isInstanceOf(User.class);
    }

    public static Stream<Arguments> badRequestStream() {
        return Stream.of(
                Arguments.of("", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of(null, "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("", "Holmes", "fake@mail", "0000000000", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", null, "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "", "fake number", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "", "0000000000", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", null, "0000000000", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "", "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", null, "Baker Street", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "", "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", null, "221B", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221very fake", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "", "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", null, "London", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", "", "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", null, "United Kingdom"),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", "London", ""),
                Arguments.of("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", "Baker Street", "221B", "London", null)
        );
    }

    @ParameterizedTest(name = "Name: {0} {1} | Mail: {2} | Phone: {3} | Address: {4} {5}, {6}, {7}")
    @DisplayName("Create Customer with invalid/empty/null field in the request")
    @MethodSource("badRequestStream")
    void givenBadRequest_whenCallingCreateBook_thenAnExceptionIsThrown(
            String firstName,
            String lastName,
            String mail,
            String phone,
            String street,
            String number,
            String city,
            String country
    ) {
        Assertions.assertThatThrownBy(() -> service.createCustomer(new PostUserRequest(
                firstName,
                lastName,
                mail,
                phone,
                street,
                number,
                city,
                country
        ))).isInstanceOf(BadRequestException.class);
    }
}