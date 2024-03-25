package org.ilia.bookstore.mapper;

import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    //TODO write request response mapper

    BookDto toBookDto(Book book);

    Book toBook(BookDto bookDto);
}
