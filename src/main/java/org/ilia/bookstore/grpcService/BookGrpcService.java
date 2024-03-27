package org.ilia.bookstore.grpcService;

import com.google.protobuf.Empty;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.bookstore.service.BookService;
import org.ilia.bookstore.validation.BookValidator;
import org.ilia.bookstore.validation.groups.UpdateBook;
import org.ilia.grpc.BookServiceGrpc.BookServiceImplBase;
import org.ilia.grpc.BookServiceOuterClass;
import org.ilia.grpc.BookServiceOuterClass.*;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class BookGrpcService extends BookServiceImplBase {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final BookValidator bookValidator;

    public static final String BOOK_NOT_FOUND = "Book not found";

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<CreateBookResponse> responseObserver) {
        BookDto bookFromRequest = bookMapper.grpcBookWithoutIdToBookDto(request.getBook());

        List<String> errors = bookValidator.validateBookDto(bookFromRequest, Default.class);
        if (!errors.isEmpty()) {
            handleError(responseObserver, Code.INVALID_ARGUMENT, String.join(", ", errors));
            return;
        }

        responseObserver.onNext(CreateBookResponse.newBuilder()
                .setBook(bookMapper.bookDtoToGrpcBook(bookService.create(bookFromRequest)))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void findAllBooks(Empty request, StreamObserver<FindAllBooksResponse> responseObserver) {
        List<BookServiceOuterClass.Book> books = bookService.findAll().stream()
                .map(bookMapper::bookDtoToGrpcBook)
                .toList();
        responseObserver.onNext(FindAllBooksResponse.newBuilder()
                .addAllBooks(books)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void findBookById(FindBookByIdRequest request, StreamObserver<FindBookByIdResponse> responseObserver) {
        bookValidator.validateUUID(request.getId())
                .ifPresent(error -> handleError(responseObserver, Code.INVALID_ARGUMENT, error));

        Optional<BookDto> maybeBook = bookService.findById(UUID.fromString(request.getId()));
        if (maybeBook.isPresent()) {
            responseObserver.onNext(FindBookByIdResponse.newBuilder()
                    .setBook(bookMapper.bookDtoToGrpcBook(maybeBook.get()))
                    .build());
            responseObserver.onCompleted();
        } else {
            handleError(responseObserver, Code.NOT_FOUND, BOOK_NOT_FOUND);
        }
    }

    @Override
    public void findBooksByTitle(FindBookByTitleRequest request, StreamObserver<FindBookByTitleResponse> responseObserver) {
        List<BookServiceOuterClass.Book> books = bookService.findByTitle(request.getTitle()).stream()
                .map(bookMapper::bookDtoToGrpcBook)
                .toList();
        responseObserver.onNext(FindBookByTitleResponse.newBuilder()
                .addAllBooks(books)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {
        BookDto bookFromRequest = bookMapper.grpcBookToBookDto(request.getBook());

        List<String> errors = bookValidator.validateBookDto(bookFromRequest, UpdateBook.class, Default.class);
        if (!errors.isEmpty()) {
            handleError(responseObserver, Code.INVALID_ARGUMENT, String.join(", ", errors));
            return;
        }

        Optional<BookDto> updatedBook = bookService.update(bookFromRequest);
        if (updatedBook.isPresent()) {
            responseObserver.onNext(UpdateBookResponse.newBuilder()
                    .setBook(bookMapper.bookDtoToGrpcBook(updatedBook.get()))
                    .build());
            responseObserver.onCompleted();
        } else {
            handleError(responseObserver, Code.NOT_FOUND, BOOK_NOT_FOUND);
        }
    }

    @Override
    public void deleteBook(DeleteBookByIdRequest request, StreamObserver<DeleteBookByIdResponse> responseObserver) {
        bookValidator.validateUUID(request.getId())
                .ifPresent(error -> handleError(responseObserver, Code.INVALID_ARGUMENT, error));

        if (bookService.delete(UUID.fromString(request.getId()))) {
            responseObserver.onNext(DeleteBookByIdResponse.newBuilder()
                    .setIsDeleted(true)
                    .build());
            responseObserver.onCompleted();
        } else {
            handleError(responseObserver, Code.NOT_FOUND, BOOK_NOT_FOUND);
        }
    }

    private void handleError(StreamObserver<?> responseObserver, Code code, String message) {
        Status status = Status.newBuilder()
                .setCode(code.getNumber())
                .setMessage(message)
                .build();
        responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
}
