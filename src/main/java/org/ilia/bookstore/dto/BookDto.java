package org.ilia.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.UUID;
import org.ilia.bookstore.validation.groups.UpdateBook;

@Value
public class BookDto {

    @UUID(message = "Enter valid UUID", groups = UpdateBook.class)
    String id;

    @NotBlank(message = "Enter title")
    String title;

    @NotBlank(message = "Enter author")
    String author;

    @ISBN(message = "Enter valid ISBN")
    String isbn;

    @PositiveOrZero(message = "Quantity can't be negative")
    Integer quantity;
}
