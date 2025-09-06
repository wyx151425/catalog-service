package com.polarbookshop.catalogservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: WangZhenqi
 * @Description:
 * @Date: Created in 2025-09-03 11:13
 * @Modified By:
 */
@RestController
public class HomeController {

    @GetMapping(value = "/")
    public String getGreeting() {
        return "Welcome to the book catalog!";
    }
}
