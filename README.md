# 算力租赁平台后端

Spring Boot 3.2.x 后端工程骨架，按《算力租赁平台_正式开发版_v1.1.md》初始化。

## 技术栈

- JDK 17
- Spring Boot 3.2.x
- Spring Security 6 + JWT
- MyBatis-Plus
- MySQL 8
- Redis
- RabbitMQ
- Spring Scheduler + Redis Lock
- WebSocket
- Knife4j / springdoc-openapi
- Maven

## 启动前配置

复制 `.env.example` 中的变量到本地运行环境，并替换数据库、Redis、RabbitMQ、JWT 密钥等配置。不要把真实密钥提交到仓库。

默认开发库名：

```text
compute_rental
```

## 常用命令

```bash
mvn compile
mvn clean install -DskipTests
mvn spring-boot:run
```

如后续存在单元测试，可使用：

```bash
mvn test -Dtest=ClassName
```

## API 文档

启动后访问：

```text
http://localhost:8080/doc.html
http://localhost:8080/swagger-ui.html
```

## 包结构

```text
com.compute.rental
├── common      # 统一响应、错误码、异常、分页、工具类
├── config      # MyBatis-Plus、Redis、RabbitMQ、OpenAPI、时区等配置
├── security    # Spring Security 6 + JWT 基础结构
├── websocket   # WebSocket 基础结构
├── scheduler   # Scheduler + Redis Lock 基础结构
└── modules     # 业务模块：user、wallet、product、order、commission、system
```

## SQL 目录

后续建表脚本放在：

```text
src/main/resources/sql
```

当前基础脚本：

```text
src/main/resources/sql/schema.sql
src/main/resources/sql/init-data.sql
```

本地初始化示例：

```bash
mysql -h <host> -P <port> -u <user> -p < src/main/resources/sql/schema.sql
mysql -h <host> -P <port> -u <user> -p compute_rental < src/main/resources/sql/init-data.sql
```

`init-data.sql` 包含 dev 管理员、基础系统配置、三级佣金规则、租赁周期规则和后台菜单。生产环境上线前必须替换默认管理员密码哈希。
