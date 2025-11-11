package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author: WangZhenqi
 * @Description: 配置安全策略和 JWT 认证
 * @Date: Created in 2025-11-06 21:21
 * @Modified By:
 */
// 为 Spring Security 启用 Spring MVC 支持
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(authorize -> authorize
                        // 允许用户未经认证即可获取问候信息和图书列表
                        .mvcMatchers(HttpMethod.GET, "/", "/books/**").permitAll()
//                        // 任何其他请求均需要认证
//                        .anyRequest().authenticated())
                        // 所有其他的请求不仅需要认证，还需要 “employee” 角色 (它与 ROLE_employee 授权具有相同的语义)
                        .anyRequest().authenticated())
                // 使用基于 JWT (即 JWT 认证) 的默认配置启用 OAuth2 资源服务器
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // 每个请求必须包含访问令牌，所以没有必要在不同的请求间保持用户会话。我们希望它是无状态的
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 因为认证策略是无状态的，并不涉及基于浏览器的客户端，所以我们可以安全地禁用 CSRF 防护
                .csrf(AbstractHttpConfigurer::disable)
                .build();

    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // 定义将 claim 映射到 GrantedAuthority 的转换器
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 对每个用户角色使用 ROLE_ 前缀
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // 从 roles claim 中提取角色列表
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        // 定义转换 JWT 的策略，我们只自定义了如何构建授权
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
