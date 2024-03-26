package org.ilia.bookstore.mapper;

import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.entity.Book;
import org.ilia.grpc.BookServiceOuterClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring"
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
//        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface BookMapper {

    BookDto bookToBookDto(Book book);

    Book bookDtoToBook(BookDto bookDto);

    Book copyBookDtoToBook(BookDto bookDto, @MappingTarget Book book);

    BookServiceOuterClass.Book bookDtoToGrpcBook(BookDto bookDto);

    BookDto grpcBookToBookDto(BookServiceOuterClass.Book book);

    @Mapping(target = "id", ignore = true)
    BookDto grpcBookWithoutIdToBookDto(BookServiceOuterClass.BookWithoutId book);
}
