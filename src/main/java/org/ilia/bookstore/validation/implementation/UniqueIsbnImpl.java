package org.ilia.bookstore.validation.implementation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.entity.Book;
import org.ilia.bookstore.repository.BookRepository;
import org.ilia.bookstore.validation.annotation.UniqueIsbn;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueIsbnImpl implements ConstraintValidator<UniqueIsbn, BookDto> {

    private final BookRepository bookRepository;

    @Override
    public boolean isValid(BookDto bookDto, ConstraintValidatorContext context) {
        Optional<Book> book = bookRepository.findByIsbn(bookDto.getIsbn());
        return book.isEmpty() || book.get().getId().toString().equals(bookDto.getId());
    }
}
