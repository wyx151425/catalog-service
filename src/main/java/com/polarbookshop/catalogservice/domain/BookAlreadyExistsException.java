package com.polarbookshop.catalogservice.domain;

/**
 * @Author: WangZhenqi
 * @Description:
 * @Date: Created in 2025-10-22 19:42
 * @Modified By:
 */
public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("A book with ISBN " + isbn + " already exists.");
    }
}
