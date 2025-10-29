-- 更新表模式的 Flyway 迁移脚本
ALTER TABLE book
ADD COLUMN publisher varchar(255);
