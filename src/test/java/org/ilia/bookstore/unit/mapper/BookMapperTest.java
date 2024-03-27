package org.ilia.bookstore.unit.mapper;

import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.grpc.BookServiceOuterClass.Book;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookMapperTest {

    BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    static List<BookDto> books = List.of(
            new BookDto("4799b50b-934f-4bc8-9a4b-10e26b53a033", "Title", "Author", "9780140447927", 1),
            new BookDto("", "", "", "", 0),
            new BookDto("4799b50b-934f-4bc8-9a4b-10e26b53a033", "", "Author", "", 1),
            new BookDto("", "Title", "", "9780140447927", 0)
    );

    static List<Book> grpcBooks = List.of(
            Book.newBuilder().setId("4799b50b-934f-4bc8-9a4b-10e26b53a033").setTitle("Title").setAuthor("Author").setIsbn("9780140447927").setQuantity(1).build(),
            Book.newBuilder().build(),
            Book.newBuilder().setId("4799b50b-934f-4bc8-9a4b-10e26b53a033").setAuthor("Author").setQuantity(1).build(),
            Book.newBuilder().setTitle("Title").setIsbn("9780140447927").build()
    );

    @ParameterizedTest
    @MethodSource("bookDtoAndGrpcBookProvider")
    void bookDtoToGrpcBook(BookDto bookDto, Book grpcBook) {
        assertEquals(grpcBook, bookMapper.bookDtoToGrpcBook(bookDto));
    }

    @ParameterizedTest
    @MethodSource("bookDtoAndGrpcBookProvider")
    void grpcBookToBookDto(BookDto bookDto, Book grpcBook) {
        assertEquals(bookDto, bookMapper.grpcBookToBookDto(grpcBook));
    }

    static Stream<Arguments> bookDtoAndGrpcBookProvider() {
        return Stream.of(
                Arguments.of(books.get(0), grpcBooks.get(0)),
                Arguments.of(books.get(1), grpcBooks.get(1)),
                Arguments.of(books.get(2), grpcBooks.get(2)),
                Arguments.of(books.get(3), grpcBooks.get(3))
        );
    }
}