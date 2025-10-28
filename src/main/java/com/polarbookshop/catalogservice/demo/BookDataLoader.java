package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: WangZhenqi
 * @Description: 当 testdata profile 处于激活状态时加载图书的测试数据
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
@Component
// 将该类分配给 testdata profile，它仅在 testdata profile 处于激活状态时才会注册
@Profile("testdata")
public class BookDataLoader {

	private final BookRepository bookRepository;

	public BookDataLoader(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

    // 当应用发送 ApplicationReadyEvent 事件时会触发测试数据的生成，也就是当应用启动阶段完成时
	@EventListener(ApplicationReadyEvent.class)
	public void loadBookTestData() {
        bookRepository.deleteAll();
		var book1 = Book.of("1234567891", "Northern Lights", "Lyra Silverstar", 9.90, "Polarsophia");
		var book2 = Book.of("1234567892", "Polar Journey", "Iorek Polarson", 12.90, "Polarsophia");
		bookRepository.saveAll(List.of(book1, book2));
	}
}
