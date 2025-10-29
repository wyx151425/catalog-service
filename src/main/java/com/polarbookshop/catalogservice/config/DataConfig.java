package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

/**
 * @Author: WangZhenqi
 * @Description: 通过注解配置启用 JDBC 审计
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
// 表明该类是 Spring 的配资源
@Configuration
// 为持久化实体启用审计
@EnableJdbcAuditing
public class DataConfig {
}
