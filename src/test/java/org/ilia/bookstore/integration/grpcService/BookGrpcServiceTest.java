package org.ilia.bookstore.integration.grpcService;

import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.integration.GrpcServerTestBase;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.grpc.BookServiceGrpc;
import org.ilia.grpc.BookServiceOuterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookGrpcServiceTest extends GrpcServerTestBase {

    @Autowired
    private BookMapper bookMapper;

    private BookServiceGrpc.BookServiceBlockingStub stub;

    @BeforeEach
    public void setUp() {
        stub = BookServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void createInvalidBook() {
        assertThrows(StatusRuntimeException.class, () -> stub
                .createBook(BookServiceOuterClass.CreateBookRequest.newBuilder()
                        .build()));
    }

    @Test
    void findAll() {
        List<BookDto> booksFromResponse = stub
                .findAllBooks(Empty.newBuilder().build())
                .getBooksList().stream()
                .map(bookMapper::grpcBookToBookDto)
                .toList();
        assertThat(booksFromResponse).hasSameElementsAs(ALL_BOOKS_IN_DB);
    }

    @Test
    void findBookById() {
        BookServiceOuterClass.Book response = stub
                .findBookById(BookServiceOuterClass.FindBookByIdRequest.newBuilder()
                        .setId("20404a4a-b8e0-4f86-ae36-64956b9f6c0c")
                        .build())
                .getBook();
        assertThat(bookMapper.grpcBookToBookDto(response)).isEqualTo(ALL_BOOKS_IN_DB.get(4));
    }

    @Test
    void findBooksByTitle() {
        List<BookDto> bookDtoFromRequest = stub
                .findBooksByTitle(BookServiceOuterClass.FindBookByTitleRequest.newBuilder()
                        .setTitle("The Three-Body Problem")
                        .build())
                .getBooksList().stream()
                .map(bookMapper::grpcBookToBookDto)
                .toList();
        assertThat(bookDtoFromRequest).contains(ALL_BOOKS_IN_DB.get(2));
    }
}
