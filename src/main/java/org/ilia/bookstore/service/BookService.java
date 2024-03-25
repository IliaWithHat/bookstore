package org.ilia.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.ilia.bookstore.mapper.BookMapper;
import org.ilia.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
}

