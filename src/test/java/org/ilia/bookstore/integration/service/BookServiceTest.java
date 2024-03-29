package org.ilia.bookstore.integration.service;

import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.entity.Book;
import org.ilia.bookstore.integration.IntegrationTestBase;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookServiceTest extends IntegrationTestBase {

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;

    @Test
    void create() {
        assertThat(bookService.findAll()).hasSameElementsAs(ALL_BOOKS_IN_DB);

        Book forCreation = new Book(null, "Title", "Author", "9789690649799", 1);
        BookDto savedBook = bookService.create(bookMapper.bookToBookDto(forCreation));
        forCreation.setId(UUID.fromString(savedBook.getId()));

        assertThat(savedBook).isEqualTo(bookMapper.bookToBookDto(forCreation));
        assertThat(bookService.findAll()).contains(savedBook);
    }

    @Test
    void findAll() {
        assertThat(bookService.findAll()).hasSameElementsAs(ALL_BOOKS_IN_DB);
    }

    @ParameterizedTest
    @MethodSource("findByIdProvider")
    void findById(UUID id, Optional<BookDto> expectedBook) {
        Optional<BookDto> maybeBook = bookService.findById(id);
        assertEquals(expectedBook.isPresent(), maybeBook.isPresent());
        assertEquals(maybeBook, expectedBook);
    }

    public static Stream<Arguments> findByIdProvider() {
        return Stream.of(
                Arguments.of(UUID.fromString("eccc40cd-082e-49af-b5d9-c4dbef1d30b6"), Optional.of(ALL_BOOKS_IN_DB.get(1))),
                Arguments.of(UUID.fromString("261eb58c-822e-4bbb-afff-762aa4ed10a0"), Optional.empty())
        );
    }

    @ParameterizedTest
    @MethodSource("findByTitleProvider")
    void findByTitle(String title, List<BookDto> expectedBooks) {
        List<BookDto> bookFromService = bookService.findByTitle(title);
        assertThat(bookFromService).hasSameElementsAs(expectedBooks);
    }

    public static Stream<Arguments> findByTitleProvider() {
        return Stream.of(
                Arguments.of("American Dirt", List.of(ALL_BOOKS_IN_DB.get(3))),
                Arguments.of("Some name", Collections.emptyList())
        );
    }

    @Test
    void update() {
        Optional<BookDto> bookBeforeUpdate = bookService.findById(UUID.fromString(ALL_BOOKS_IN_DB.get(0).getId()));
        assertTrue(bookBeforeUpdate.isPresent());
        assertThat(bookBeforeUpdate.get()).isEqualTo(ALL_BOOKS_IN_DB.get(0));

        Book bookToUpdate = bookMapper.bookDtoToBook(ALL_BOOKS_IN_DB.get(0));
        bookToUpdate.setTitle("Not Idiot");
        Optional<BookDto> updatedBook = bookService.update(bookMapper.bookToBookDto(bookToUpdate));

        assertTrue(updatedBook.isPresent());
        assertThat(updatedBook.get()).isEqualTo(bookMapper.bookToBookDto(bookToUpdate));
        assertThat(bookService.findAll()).doesNotContain(ALL_BOOKS_IN_DB.get(0));
    }

    @Test
    void delete() {
        UUID uuidForDelete = UUID.fromString(ALL_BOOKS_IN_DB.get(1).getId());

        Optional<BookDto> bookForDeleting = bookService.findById(uuidForDelete);
        assertTrue(bookForDeleting.isPresent());
        assertThat(bookForDeleting.get()).isEqualTo(ALL_BOOKS_IN_DB.get(1));

        boolean isDeleted = bookService.delete(uuidForDelete);
        assertTrue(isDeleted);

        List<BookDto> allBooksAfterDeleting = bookService.findAll();
        assertThat(allBooksAfterDeleting).doesNotContain(ALL_BOOKS_IN_DB.get(1));
    }
}
