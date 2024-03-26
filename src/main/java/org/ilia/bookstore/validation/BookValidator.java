package org.ilia.bookstore.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.ilia.bookstore.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private final Validator validator;

    public List<String> validateBookDto(BookDto bookDto, Class<?>... groups) {
        return validator.validate(bookDto, groups).stream()
                .map(ConstraintViolation::getMessage)
                .toList();
    }

    public Optional<String> validateUUID(String uuid) {
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return Optional.of("Enter valid UUID");
        }
        return Optional.empty();
    }
}
