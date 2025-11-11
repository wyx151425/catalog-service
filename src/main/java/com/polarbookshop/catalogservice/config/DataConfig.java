package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @Author: WangZhenqi
 * @Description: 通过注解配置启用 JDBC 审计
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
// 表明该类是 Spring 的配资源
@Configuration
// 为持久化实体启用审计
// 在 Spring Data JDBC 中启用实体审计
@EnableJdbcAuditing
public class DataConfig {

    @Bean
    AuditorAware<String> auditorAware() {
        // 返回当前已认证用户，以便于进行审计
        return () -> Optional
                // 从 SecurityContextHolder 中为当前已认证用户提取 SecurityContext 对象
                .ofNullable(SecurityContextHolder.getContext())
                // 从 SecurityContext 中为当前已认证用户提取 Authentication 对象
                .map(SecurityContext::getAuthentication)
                // 处理用户未经认证但尝试操作数据的场景。因为我们保护了所有的端点，所以这种情况永远不应发生，但是为了完整性，我们包含了这种情况
                .filter(Authentication::isAuthenticated)
                // 从 Authentication 对象中为当前已认证用户提取用户名
                .map(Authentication::getName);
    }
}
