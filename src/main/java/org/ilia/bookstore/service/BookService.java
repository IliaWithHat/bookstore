package org.ilia.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.ilia.bookstore.dto.BookDto;
import org.ilia.bookstore.entity.Book;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookDto create(BookDto bookDto) {
        return Optional.of(bookDto)
                .map(bookMapper::bookDtoToBook)
                .map(bookRepository::save)
                .map(bookMapper::bookToBookDto)
                .orElseThrow();
    }

    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    public Optional<BookDto> findById(UUID id) {
        return bookRepository.findById(id)
                .map(bookMapper::bookToBookDto);
    }

    public Optional<BookDto> update(BookDto bookDto) {
        return bookRepository.findById(UUID.fromString(bookDto.getId()))
                .map(book -> bookMapper.copyBookDtoToBook(bookDto, book))
                .map(bookRepository::save)
                .map(bookMapper::bookToBookDto);
    }

    public boolean delete(UUID id) {
        Optional<Book> maybeBook = bookRepository.findById(id);
        if (maybeBook.isPresent()) {
            bookRepository.delete(maybeBook.get());
            return true;
        } else {
            return false;
        }
    }
}
