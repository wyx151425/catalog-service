package com.polarbookshop.catalogservice.domain;

import java.util.Optional;

/**
 * @Author: WangZhenqi
 * @Description:
 * @Date: Created in 2025-10-22 19:25
 * @Modified By:
 */
public interface BookRepository {

    Iterable<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    Book save(Book book);

    void deleteByIsbn(String isbn);
}
