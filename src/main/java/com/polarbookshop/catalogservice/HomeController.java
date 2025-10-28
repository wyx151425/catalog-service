package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.config.PolarProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: WangZhenqi
 * @Description: 定义了一个 GET 端点来返回欢迎消息的控制器
 * @Date: Created in 2025-09-03 11:13
 * @Modified By:
 */
// 标记该类将会定义 REST/HTTP 端点的处理器
@RestController
public class HomeController {

    // 通过构造器自动装配注入的 bean 来访问自定义属性
    private final PolarProperties polarProperties;

    public HomeController(PolarProperties polarProperties) {
        this.polarProperties = polarProperties;
    }

    // 处理对根端点的 GET 请求
    @GetMapping("/")
    public String getGreeting() {
        // 使用来自配置数据 bean 的欢迎消息
        return polarProperties.getGreeting();
    }
}
