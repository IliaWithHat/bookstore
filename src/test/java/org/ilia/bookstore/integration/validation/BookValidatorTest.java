package org.ilia.bookstore.integration.validation;

import jakarta.validation.groups.Default;
import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.integration.IntegrationTestBase;
import org.ilia.bookstore.validation.BookValidator;
import org.ilia.bookstore.validation.groups.UpdateBook;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BookValidatorTest extends IntegrationTestBase {

    @Autowired
    BookValidator bookValidator;

    static final String ISBN_ALREADY_EXIST = "This ISBN number already exist";
    static final String INVALID_UUID = "Enter valid UUID";
    static final String BLANK_TITLE = "Enter title";
    static final String TOO_LONG_TITLE = "Title is too long";
    static final String BLANK_AUTHOR = "Enter author";
    static final String TOO_LONG_AUTHOR = "Author name is too long";
    static final String INVALID_ISBN = "Enter valid ISBN";
    static final String NEGATIVE_QUANTITY = "Quantity can't be negative";

    static Class<?>[] createBookGroup = new Class<?>[]{Default.class};
    static Class<?>[] updateBookGroup = new Class<?>[]{Default.class, UpdateBook.class};

    @ParameterizedTest
    @MethodSource("bookDtoProvider")
    void validateBookDto(BookDto bookDto, Class<?>[] groups, List<String> expectedErrors) {
        List<String> errors = bookValidator.validateBookDto(bookDto, groups);
        assertThat(errors).hasSameElementsAs(expectedErrors);
    }

    static Stream<Arguments> bookDtoProvider() {
        return Stream.of(
                Arguments.of(
                        new BookDto("", "123", "123", "9780765386694", 1),
                        createBookGroup,
                        Collections.emptyList()),
                Arguments.of(
                        new BookDto("", "123", "123", "9781451673319", 1),
                        createBookGroup,
                        List.of(ISBN_ALREADY_EXIST)),
                Arguments.of(
                        new BookDto("", "", "", "", -1),
                        createBookGroup,
                        List.of(BLANK_TITLE, BLANK_AUTHOR, INVALID_ISBN, NEGATIVE_QUANTITY)),
                Arguments.of(
                        new BookDto("", "jR#p7L@6*dQfG2ZsVz9T!nXbMk3yDmCvEoFqW4lN5uY8A1IgO0hKcHwSePtRx1234", "jR#p7L@6*dQfG2ZsVz9T!nXbMk3yDmCvEoFqW4lN5uY8A1IgO0hKcHwSePtRx1234", "9780765386694", 1),
                        createBookGroup,
                        List.of(TOO_LONG_TITLE, TOO_LONG_AUTHOR)),
                Arguments.of(
                        new BookDto("a49d293a-36c2-42c4-99de-9cf71bf5791d", "123", "123", "9780765386694", 1),
                        updateBookGroup,
                        Collections.emptyList()),
                Arguments.of(
                        new BookDto("a49d293a-36c2-42c4-99de-9cf71bf5791d", "123", "123", "9781451673319", 1),
                        updateBookGroup,
                        List.of(ISBN_ALREADY_EXIST)),
                Arguments.of(
                        new BookDto("20404a4a-b8e0-4f86-ae36-64956b9f6c0c", "123", "123", "9781451673319", 1),
                        updateBookGroup,
                        Collections.emptyList()),
                Arguments.of(
                        new BookDto("", "", "", "", -1),
                        updateBookGroup,
                        List.of(INVALID_UUID, BLANK_TITLE, BLANK_AUTHOR, INVALID_ISBN, NEGATIVE_QUANTITY)),
                Arguments.of(
                        new BookDto("", "jR#p7L@6*dQfG2ZsVz9T!nXbMk3yDmCvEoFqW4lN5uY8A1IgO0hKcHwSePtRx1234", "jR#p7L@6*dQfG2ZsVz9T!nXbMk3yDmCvEoFqW4lN5uY8A1IgO0hKcHwSePtRx1234", "9780765386694", 1),
                        updateBookGroup,
                        List.of(INVALID_UUID, TOO_LONG_TITLE, TOO_LONG_AUTHOR))
        );
    }
}
