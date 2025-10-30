## 新镜像基于 Ubuntu 官方镜像，版本为 22.04
#FROM ubuntu:22.04
## 使用熟悉的 bash 命令安装 JRE
#RUN apt-get update && apt-get install -y default-jre
## 为运行的容器定义执行入口点
#ENTRYPOINT ["java", "--version"]

## Ubuntu 基础镜像，预先安装了 Eclipse Temurin 发行版
#FROM eclipse-temurin:17
## 将当前的工作目录变更为 “workspace”
#WORKDIR workspace
## 构建参数，声明了项目中应用 JAR 文件的位置
#ARG JAR_FILE=build/libs/*.jar
## 将应用的 JAR 文件从本地机器复制到镜像中
#COPY ${JAR_FILE} catalog-service.jar
## 设置容器的入口点以运行应用
#ENTRYPOINT ["java", "-jar", "catalog-service.jar"]

# 用于第一阶段的 OpenJDK 基础镜像
FROM eclipse-temurin:17 AS builder
WORKDIR workspace
# 构建参数，声明了项目中应用 JAR 文件的位置
ARG JAR_FILE=build/libs/*.jar
# 将应用的 JAR 文件从本地机器复制到镜像的 “workspace” 目录中
COPY ${JAR_FILE} catalog-service.jar
# 从使用分层 JAR 模式的归档文件中抽取各个层
RUN java -Djarmode=layertools -jar catalog-service.jar extract

# 用于第而阶段的 OpenJDK 基础镜像
FROM eclipse-temurin:17
# 创建 “spring” 用户
RUN useradd spring
# 将 “spring” 配置为当前用户
USER spring
WORKDIR workspace
# 将第一阶段的每个 JAR 层复制到第二阶段的 “workspace” 目录中
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
# 使用 Spring Boot Launcher 启动应用，此时应用位于层中，而不再是 uber-JAR
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
