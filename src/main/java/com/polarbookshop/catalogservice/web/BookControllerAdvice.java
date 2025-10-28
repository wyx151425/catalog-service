package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: WangZhenqi
 * @Description: 定义如何处理异常的通知类
 * @Date: Created in 2025-10-22 20:10
 * @Modified By:
 */
// 标记将该类作为中心化的异常处理器
@RestControllerAdvice
public class BookControllerAdvice {

    // 定义该处理器必须要执行的异常
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bookNotFoundHandler(BookNotFoundException ex) {
        // HTTP 响应体中将会包含的信息
        return ex.getMessage();
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    // 定义抛出异常时所创建的 HTTP 响应的状态码
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            // 处理 Book 校验失败时所抛出的异常
            MethodArgumentNotValidException ex
    ) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            // 提供有意义的错误信息来说明哪个 Book 字段非法，而不是仅仅返回一条空消息
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
