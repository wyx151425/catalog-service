package com.polarbookshop.catalogservice.domain;

/**
 * @Author: WangZhenqi
 * @Description: 当图书无法找到时所抛出的异常
 * @Date: Created in 2025-10-22 19:42
 * @Modified By:
 */
public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String isbn) {
        super("The book with ISBN " + isbn + " was not found.");
    }
}
