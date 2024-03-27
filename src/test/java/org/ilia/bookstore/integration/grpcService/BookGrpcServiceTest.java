package org.ilia.bookstore.integration.grpcService;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.integration.IntegrationTestBase;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.grpc.BookServiceGrpc;
import org.ilia.grpc.BookServiceOuterClass;
import org.junit.jupiter.api.Test;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookGrpcServiceTest extends IntegrationTestBase {

    @Autowired
    BookMapper bookMapper;

    @LocalRunningGrpcPort
    int port;

    List<BookDto> allBooks = List.of(
            new BookDto("4799b50b-934f-4bc8-9a4b-10e26b53a033", "The Idiot", "Fyodor Dostoevsky", "9780140447927", 1),
            new BookDto("eccc40cd-082e-49af-b5d9-c4dbef1d30b6", "1984", "George Orwell", "9780451524935", 1),
            new BookDto("1bf05418-1b67-4a1e-bdc4-9801b2c3a84f", "The Three-Body Problem", "Liu Cixin", "9780765382030", 1),
            new BookDto("6783b7ab-6c36-42e3-8ed4-191dd0c0e387", "American Dirt", "Jeanine Cummins", "9781250209764", 1),
            new BookDto("20404a4a-b8e0-4f86-ae36-64956b9f6c0c", "Fahrenheit 451", "Ray Bradbury", "9781451673319", 1)
    );

    @Test
    void createInvalidBook() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
        assertThrows(StatusRuntimeException.class, () -> BookServiceGrpc.newBlockingStub(channel)
                .createBook(BookServiceOuterClass.CreateBookRequest.newBuilder()
                        .build()));
        channel.shutdown();
    }

    @Test
    void findBookById() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
        BookServiceOuterClass.Book response = BookServiceGrpc.newBlockingStub(channel)
                .findBookById(BookServiceOuterClass.FindBookByIdRequest.newBuilder()
                        .setId("20404a4a-b8e0-4f86-ae36-64956b9f6c0c")
                        .build())
                .getBook();
        assertEquals(allBooks.get(4), bookMapper.grpcBookToBookDto(response));
        channel.shutdown();
    }

    @Test
    void deleteBook() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
        BookServiceGrpc.BookServiceBlockingStub stub = BookServiceGrpc.newBlockingStub(channel);
        List<BookDto> beforeDeleting = stub
                .findAllBooks(Empty.newBuilder().build())
                .getBooksList().stream()
                .map(bookMapper::grpcBookToBookDto)
                .toList();
        assertThat(beforeDeleting).hasSameElementsAs(allBooks);

        BookServiceOuterClass.DeleteBookByIdResponse responseAfterDeleting = stub
                .deleteBook(BookServiceOuterClass.DeleteBookByIdRequest.newBuilder()
                        .setId("4799b50b-934f-4bc8-9a4b-10e26b53a033")
                        .build());
        assertTrue(responseAfterDeleting.getIsDeleted());

        List<BookDto> afterDeleting = stub.findAllBooks(Empty.newBuilder().build())
                .getBooksList().stream()
                .map(bookMapper::grpcBookToBookDto)
                .toList();
        ArrayList<BookDto> copyAllBooks = new ArrayList<>(allBooks);
        copyAllBooks.remove(0);
        assertThat(afterDeleting).hasSameElementsAs(copyAllBooks);
        channel.shutdown();
    }
}
