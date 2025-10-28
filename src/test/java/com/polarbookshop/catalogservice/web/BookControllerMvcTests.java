package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author: WangZhenqi
 * @Description: Web MVC 切片的集成测试
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
// 表明该测试类主要关注 Spring MVC 组件，明确是为了测试 BookController
@WebMvcTest(BookController.class)
class BookControllerMvcTests {

    // 在 mock 环境中测试 Web 层的工具类
    @Autowired
    private MockMvc mockMvc;

    // 添加 mock 的 BookService 到 Spring 应用上下文中
    @MockBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "73737313940";
        given(bookService.viewBookDetails(isbn))
                // 定义 BookService mock bean 的预期行为
                .willThrow(BookNotFoundException.class);
        mockMvc
                // 使用 MockMvc 来执行 HTTP GET 请求并校验结果
                .perform(get("/books/" + isbn))
                // 预期响应的状态码为 “404 Not Found”
                .andExpect(status().isNotFound());
    }
}
