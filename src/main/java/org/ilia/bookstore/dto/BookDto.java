package org.ilia.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.UUID;
import org.ilia.bookstore.validation.annotation.UniqueIsbn;
import org.ilia.bookstore.validation.groups.UpdateBook;

@Value
@UniqueIsbn
public class BookDto {

    @UUID(message = "Enter valid UUID", groups = UpdateBook.class)
    String id;

    @NotBlank(message = "Enter title")
    @Size(max = 64, message = "Title is too long")
    String title;

    @NotBlank(message = "Enter author")
    @Size(max = 64, message = "Author name is too long")
    String author;

    @ISBN(message = "Enter valid ISBN")
    String isbn;

    @PositiveOrZero(message = "Quantity can't be negative")
    Integer quantity;
}
