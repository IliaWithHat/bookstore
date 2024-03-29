package org.ilia.bookstore.integration;

import org.ilia.bookstore.dto.BookDto;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1");

    public final static List<BookDto> ALL_BOOKS_IN_DB = List.of(
            new BookDto("4799b50b-934f-4bc8-9a4b-10e26b53a033", "The Idiot", "Fyodor Dostoevsky", "9780140447927", 1),
            new BookDto("eccc40cd-082e-49af-b5d9-c4dbef1d30b6", "1984", "George Orwell", "9780451524935", 1),
            new BookDto("1bf05418-1b67-4a1e-bdc4-9801b2c3a84f", "The Three-Body Problem", "Liu Cixin", "9780765382030", 1),
            new BookDto("6783b7ab-6c36-42e3-8ed4-191dd0c0e387", "American Dirt", "Jeanine Cummins", "9781250209764", 1),
            new BookDto("20404a4a-b8e0-4f86-ae36-64956b9f6c0c", "Fahrenheit 451", "Ray Bradbury", "9781451673319", 1)
    );

    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
