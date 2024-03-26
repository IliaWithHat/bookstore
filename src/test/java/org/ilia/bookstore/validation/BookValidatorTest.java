package org.ilia.bookstore.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookValidatorTest {

    @Autowired
    private BookValidator bookValidator;

    @Test
    void validateBookDto() {
    }

    @ParameterizedTest
    @MethodSource("uuidProvider")
    public void testValidateUUID(String uuid, boolean isValid) {
        Optional<String> result = bookValidator.validateUUID(uuid);
        assertEquals(isValid, result.isEmpty());
    }

    static Stream<Object[]> uuidProvider() {
        return Stream.of(
                new Object[]{"550e8400-e29b-41d4-a716-446655440000", true},
                new Object[]{"538a7f72-63be-4f9d-8436-7f2cc4784c95", true},
                new Object[]{"invalid-uuid-format", false},
                new Object[]{"something", false},
                new Object[]{"", false}
        );
    }
}