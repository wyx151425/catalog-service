package com.polarbookshop.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @Author: WangZhenqi
 * @Description: Catalog Service 的引导类
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
// 定义 Spring 配置类并触发组件扫描和 Spring Boot的自动配置
@SpringBootApplication
@ConfigurationPropertiesScan
public class CatalogServiceApplication {

    // 用来启动应用的方法。它会注册当前类并在应用的引导阶段运行
    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }
}
