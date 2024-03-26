package org.ilia.bookstore.grpcService;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.bookstore.service.BookService;
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

    //TODO add EVERYWHERE validation dto

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<CreateBookResponse> responseObserver) {
        BookDto bookDto = bookMapper.grpcBookWithoutIdToBookDto(request.getBook());
        Optional<BookDto> savedBook = bookService.create(bookDto);
        if (savedBook.isPresent()) {
            responseObserver.onNext(CreateBookResponse.newBuilder()
                    .setId(savedBook.get().getId().toString())
                    .build());
            responseObserver.onCompleted();
        } else {
            //TODO exception
        }
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
        Optional<BookDto> maybeBook = bookService.findById(UUID.fromString(request.getId()));
        if (maybeBook.isPresent()) {
            responseObserver.onNext(FindBookByIdResponse.newBuilder()
                    .setBook(bookMapper.bookDtoToGrpcBook(maybeBook.get()))
                    .build());
            responseObserver.onCompleted();
        } else {
            //TODO exception
        }
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {
        BookDto bookDto = bookMapper.grpcBookToBookDto(request.getBook());
        Optional<BookDto> updatedBook = bookService.update(bookDto);
        if (updatedBook.isPresent()) {
            responseObserver.onNext(UpdateBookResponse.newBuilder()
                    .setBook(bookMapper.bookDtoToGrpcBook(updatedBook.get()))
                    .build());
            responseObserver.onCompleted();
        } else {
            //TODO exception
        }
    }

    @Override
    public void deleteBook(DeleteBookByIdRequest request, StreamObserver<DeleteBookByIdResponse> responseObserver) {
        boolean isDeleted = bookService.delete(UUID.fromString(request.getId()));
        if (isDeleted) {
            responseObserver.onNext(DeleteBookByIdResponse.newBuilder()
                    .setIsDeleted(true)
                    .build());
            responseObserver.onCompleted();
        } else {
            //TODO exception
        }
    }
}
