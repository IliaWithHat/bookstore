package org.ilia.bookstore.grpc;

import io.grpc.stub.StreamObserver;
import org.ilia.grpc.BookServiceGrpc.BookServiceImplBase;
import org.ilia.grpc.BookServiceOuterClass.*;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class BookController extends BookServiceImplBase {

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<CreateBookResponse> responseObserver) {
        super.createBook(request, responseObserver);
    }

    @Override
    public void findBookById(FindBookByIdRequest request, StreamObserver<FindBookByIdResponse> responseObserver) {
        super.findBookById(request, responseObserver);
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {
        super.updateBook(request, responseObserver);
    }

    @Override
    public void deleteBook(DeleteBookByIdRequest request, StreamObserver<DeleteBookByIdResponse> responseObserver) {
        super.deleteBook(request, responseObserver);
    }
}
