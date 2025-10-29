package com.polarbookshop.catalogservice.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author: WangZhenqi
 * @Description: 访问图书的 Repository 接口，领域层用于访问数据的抽象
 * @Date: Created in 2025-10-22 19:25
 * @Modified By:
 */
// 构造型注解，表明该类是由 Spring 管理的资源库
@Repository
// 扩展提供 CRUD 操作的资源库，声明所管理的实体 (Book) 及其主键的类型 (Long)
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    // 标记该操作将会修改数据库状态
    @Modifying
    // 标识该方法要在事务中执行
    @Transactional
    // 声明 Spring Data 在实现该方法时所使用的查询
    @Query("delete from Book where isbn = :isbn")
    void deleteByIsbn(String isbn);
}
