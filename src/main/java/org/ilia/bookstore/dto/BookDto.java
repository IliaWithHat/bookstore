package org.ilia.bookstore.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class BookDto {

    UUID id;

    String title;

    String author;

    String isbn;

    Integer quantity;
}
