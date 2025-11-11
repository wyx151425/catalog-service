package com.polarbookshop.catalogservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polarbookshop.catalogservice.domain.Book;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author: WangZhenqi
 * @Description: 用于校验 Spring 上下文的自动生成的测试类，Catalog Service 的集成测试
 * @Date: Created in 2025-10-22 19:45
 * @Modified By:
 */
// 提供测试 Spring Boot 应用的设置，加载完整的 Spring Web 应用上下文以及监听任意端口的 Servlet 容器
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 启用 “integration” profile 以加载 application-integration.yml 中的配置
@ActiveProfiles("integration")
// 激活测试容器的自动启动和清理
@Testcontainers
class CatalogServiceApplicationTests {

    // Customer
    private static KeycloakToken bjornTokens;
    // Customer and Employee
    private static KeycloakToken isabelleTokens;

    // 为了测试而执行 REST 调用的工具
    @Autowired
    private WebTestClient webTestClient;

    // 定义用于测试的 Keycloak 容器
    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:24.0")
            .withRealmImportFile("/test-realm-config.json");

    // 重写 Keycloak Issuer URI 配置，指向测试 Keycloak 实例
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/PolarBookshop");
    }

    @BeforeAll
    static void generateAccessTokens() {
        // 用于调用 Keycloak 的 WebClient
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() + "/realms/PolarBookshop/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        // 认证 Isabelle 并获取访问令牌
        isabelleTokens = authenticateWith("isabelle", "password", webClient);
        // 认证 Bjorn 并获取访问令牌
        bjornTokens = authenticateWith("bjorn", "password", webClient);
    }

    private static KeycloakToken authenticateWith(String username, String password, WebClient webClient) {
        return webClient
                .post()
                // 使用密码授权流程直接向 Keycloak 进行认证
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "polar-test")
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                // 一直阻塞，直到返回结果。这里我们以命令式的方式使用 WebClient，而非反应式
                .block();
    }

    private record KeycloakToken(String accessToken) {
        // 当将 JSON 反序列化为 KeycloakToken 对象时，告知 Jackson 使用该构造器
        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }

    }

    // 标识测试用例
    @Test
    // 空的测试，用来校验应用上下文是否正确加载
    void contextLoads() {
    }

    @Test
    void whenGetRequestWithIdThenBookReturned() {

        var bookIsbn = "1231231230";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");

        Book expectedBook = webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
                });
    }

    @Test
    void whenPostRequestThenBookCreated() {

        var expectedBook = Book.of("1231231231", "Title", "Author", 9.90, "Polarsophia");

        webTestClient
                // 发送 HTTP POST 请求
                .post()
                // 发送请求到 /books 端点
                .uri("/books")
                // 以已认证雇员用户 (Isabelle) 的身份发送添加图书至目录的请求
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                // 向请求体中添加图书
                .bodyValue(expectedBook)
                // 发送请求
                .exchange()
                // 校验 HTTP 响应的状态码为 “201 Created”
                // 图书创建成功，状态码为 201
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook -> {
                    // 校验 HTTP 响应中包含一个非空的响应体
                    assertThat(actualBook).isNotNull();
                    // 校验创建的对象符合预期
                    assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
                });
    }

    @Test
    void whenPostRequestUnauthenticatedThen401() {
        var expectedBook = Book.of("1231231231", "Title", "Author", 9.90, "Polarsophia");

        // 以未认证用户的身份发送添加图书至目录的请求
        webTestClient
                .post()
                .uri("/books")
                .bodyValue(expectedBook)
                .exchange()
                // 图书无法成功创建，因为用户没有认证，状态码为 401
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenPostRequestUnauthorizedThen403() {
        var expectedBook = Book.of("1231231231", "Title", "Author", 9.90, "Polarsophia");

        webTestClient
                .post()
                .uri("/books")
                // 以已认证消费者用户 (Bjorn) 的身份发送添加图书至目录的请求
                .headers(headers -> headers.setBearerAuth(bjornTokens.accessToken()))
                .bodyValue(expectedBook)
                .exchange()
                // 图书无法成功创建，因为用户没有正确的授权 (缺少 “employee” 角色)，状态码为 403
                .expectStatus().isForbidden();
    }

    @Test
    void whenPutRequestThenBookUpdated() {

        var bookIsbn = "1231231232";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");

        Book createdBook = webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();

        var bookToUpdate = new Book(createdBook.id(), createdBook.isbn(), createdBook.title(), createdBook.author(), 7.95,
                createdBook.publisher(), createdBook.createdDate(), createdBook.lastModifiedDate(), createdBook.version());

        webTestClient
                .put()
                .uri("/books/" + bookIsbn)
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.price()).isEqualTo(bookToUpdate.price());
                });
    }

    @Test
    void whenDeleteRequestThenBookDeleted() {

        var bookIsbn = "1231231233";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        assertThat(errorMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found.")
                );
    }
}
