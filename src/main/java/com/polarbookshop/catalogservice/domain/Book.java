package com.polarbookshop.catalogservice.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.Instant;

/**
 * @Author: WangZhenqi
 * @Description: Book record 定义了应用的领域实体
 * @Date: Created in 2025-10-22 19:22
 * @Modified By:
 */
// 领域模型使用 record 的形式实现的，它是不可变的对象
public record Book(

        @Id
        Long id,

        // 图书的唯一标识符
        @NotBlank(message = "The book ISBN must be defined.")
        // 注解所标注的元素必须匹配给定的正则表达式（标准的 ISBN 格式）
        @Pattern(
                regexp = "^([0-9]{10}|[0-9]{13})$",
                message = "The ISBN format must be valid."
        )
        String isbn,

        // 注解所标注的元素不能为空，并且至少包含一个非空格的字符
        @NotBlank(message = "The book title must be defined.")
        String title,

        @NotBlank(message = "The book author must be defined.")
        String author,

        // 注解所标注的元素不能为空，并且值要大于零
        @NotNull(message = "The book price must be defined.")
        @Positive(message = "The book price must be greater than zero.")
        Double price,

        String publisher,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @Version
        int version

) {
    public static Book of(String isbn, String title, String author, Double price, String publisher) {
        return new Book(null, isbn, title, author, price, publisher, null, null, 0);
    }
}
