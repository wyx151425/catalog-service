package com.polarbookshop.catalogservice.domain;

import org.springframework.stereotype.Service;

/**
 * @Author: WangZhenqi
 * @Description: 实现应用的用例
 * @Date: Created in 2025-10-22 19:24
 * @Modified By:
 */
// 构造性注解，标注该类将会是由 Spring 管理的服务
@Service
public class BookService {

    private final BookRepository bookRepository;

    // 通过构造器自动装配提供的 BookRepository
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn) {
        // 当试图查看一本不存在的图书时，会抛出一个专门的异常
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        // 当多次添加同一本书到目录中时，会抛出一个专门的异常
        if (bookRepository.existsByIsbn(book.isbn())) {
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    // 当编辑图书的时候，除了 ISBN 之外，Book 的其他字段均可更新，
                    // 因为 ISBN 是实体标识符，所以不能修改（添加此注解时还没有id、createdDate、lastModifiedDate、version）
                    var bookToUpdate = new Book(
                            existingBook.id(),
                            existingBook.isbn(),
                            book.title(),
                            book.author(),
                            book.price(),
                            book.publisher(),
                            existingBook.createdDate(),
                            existingBook.lastModifiedDate(),
                            existingBook.version()
                    );
                    return bookRepository.save(bookToUpdate);
                })
                // 当试图修改一本尚不存在的图书细节时，创建一本新的图书
                .orElseGet(() -> addBookToCatalog(book));
    }
}
