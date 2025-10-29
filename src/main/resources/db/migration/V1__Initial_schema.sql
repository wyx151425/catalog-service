-- 模式初始化的 Flyway 迁移
-- 定义 book 表
CREATE TABLE book
(
    -- 声明 id 字段为表中的主键。数据库会将其生成为数字的序列 (bigserial 类型)
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    author             varchar(255)          NOT NULL,
    -- 限制 isbn 字段是唯一的，UNIQUE 约束确保同一个 ISBN 只能分配给一本书
    isbn               varchar(255) UNIQUE   NOT NULL,
    price              float8                NOT NULL,
    -- NOT NULL 约束确保相关的列必须分配一个值
    title              varchar(255)          NOT NULL,
    -- 实体创建的时间，以时间戳的形式存储
    created_date       timestamp             NOT NULL,
    -- 实体最后更新的时间，以时间戳的形式存储
    last_modified_date timestamp             NOT NULL,
    -- 实体的版本号，存储为整数
    version            integer               NOT NULL
);
