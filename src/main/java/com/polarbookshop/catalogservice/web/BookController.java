package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: WangZhenqi
 * @Description: 定义 REST 端点的处理器
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
// 构造型注解，标记该类是一个 Spring 组件，并且会作为 REST 端点的处理器源
@RestController
// 定义该类所提供的处理器对应的根路径映射 URI (/books)
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 将 HTTP GET 请求映射到特定的处理器方法上
    @GetMapping
    public Iterable<Book> get() {
        return bookService.viewBookList();
    }

    // 将一个 URI 模板变量附加到根路径映射 URI 上 (/books/{isbn})
    @GetMapping("{isbn}")
    // @PathVariable 会将一个方法参数绑定到 URI 模板变量上 ({isbn})
    public Book getByIsbn(@PathVariable String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    // 将 HTTP POST 请求映射到特定的处理器方法上
    @PostMapping
    // 如果图书创建成功的话，返回状态码 201
    @ResponseStatus(HttpStatus.CREATED)
    // @Valid 校验请求体中传递过来的图书
    // @RequestBody 会将一个方法参数绑定到 Web 请求的请求体上
    public Book post(@Valid @RequestBody Book book) {
        return bookService.addBookToCatalog(book);
    }

    // 映射 HTTP DELETE 请求到特定的处理器方法上
    @DeleteMapping("{isbn}")
    // 如果图书删除成功的话，返回状态码 204
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    // 映射 HTTP PUT 请求到特定的处理器方法上
    @PutMapping({"{isbn}"})
    public Book put(@PathVariable String isbn, @Valid @RequestBody Book book) {
        return bookService.editBookDetails(isbn, book);
    }
}
