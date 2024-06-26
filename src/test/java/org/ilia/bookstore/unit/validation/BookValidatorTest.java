package org.ilia.bookstore.unit.validation;

import org.ilia.bookstore.validation.BookValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookValidatorTest {

    BookValidator bookValidator = new BookValidator(null);

    @ParameterizedTest
    @MethodSource("uuidProvider")
    void testValidateUUID(String uuid, boolean isValid) {
        Optional<String> error = bookValidator.validateUUID(uuid);
        assertEquals(isValid, error.isEmpty());
    }

    static Stream<Arguments> uuidProvider() {
        return Stream.of(
                Arguments.of("550e8400-e29b-41d4-a716-446655440000", true),
                Arguments.of("538a7f72-63be-4f9d-8436-7f2cc4784c95", true),
                Arguments.of("invalid-uuid-format", false),
                Arguments.of("something", false),
                Arguments.of("", false)
        );
    }
}
