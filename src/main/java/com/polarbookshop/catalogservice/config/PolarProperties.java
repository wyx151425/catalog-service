package com.polarbookshop.catalogservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: WangZhenqi
 * @Description: 在 Spring Bean 中定义自定义属性
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
// 标记该类作为前缀为 “polar” 的配置属性的源
@ConfigurationProperties(prefix = "polar")
public class PolarProperties {

	/**
	 * A message to welcome users.
	 */
    // 自定义 polar.greeting (前缀+字段名) 属性将会被解析为 String 字段
	private String greeting;

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

}
