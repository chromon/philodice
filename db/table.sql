CREATE DATABASE philodice;

USE philodice;

# 链接表
CREATE TABLE links (
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT "自增主键",
    long_link      VARCHAR(1024) NOT NULL UNIQUE COMMENT "长链接",
    short_link     VARCHAR(20)   NOT NULL UNIQUE COMMENT "短链接",
    seq_id         VARCHAR(64)   NOT NULL UNIQUE COMMENT "序列号",
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
    expired_at     DATETIME COMMENT "过期时间,为NULL时永不过期",
    status         TINYINT DEFAULT 1 COMMENT "状态,1:enabled 0:disabled"
) COMMENT "存储链接基本信息";

# 用户表
CREATE TABLE users (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
    username            VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名,唯一',
    email               VARCHAR(128) UNIQUE COMMENT '邮箱地址,唯一',
    password_hash       VARCHAR(100) COMMENT '密码哈希值',
    salt                VARCHAR(16) COMMENT '密码加密盐值',
    role                TINYINT          DEFAULT 0 COMMENT '角色,0:普通用户 1:管理员',
    status              TINYINT          DEFAULT 1 COMMENT '状态,0:禁用 1:正常',
    created_at          TIMESTAMP        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_at       DATETIME COMMENT '最后登录时间',
    last_login_ip       VARCHAR(45) COMMENT '最后登录IP',
    login_failed_times  TINYINT UNSIGNED DEFAULT 0 COMMENT '登录失败次数',
    login_failed_locked TINYINT          DEFAULT 0 COMMENT '是否被锁定,0:未锁定 1:已锁定',
    locked_at           DATETIME COMMENT '锁定时间',
    INDEX (username, email) COMMENT 'username和email的联合索引'
) COMMENT '用户信息表';

-- 插入管理员用户
INSERT INTO users (username, email, password_hash, salt, role)
VALUES ('admin', 'admin@example.com', '8c6976e5b5410415bde908bd4dee15dfb18',
        'Dx9Ym&@q', 1, 1, '2023-04-25 10:23:12', '2023-04-25 10:23:12', '2023-04-25 10:23:12',
        '127.0.0.1', 0, 0, NULL);


# 点击统计表
CREATE TABLE link_metrics (
    id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT "自增主键",
    link_id    BIGINT NOT NULL COMMENT "链接id,links表外键",
    client_ip  VARCHAR(45) COMMENT "客户端IP",
    country    VARCHAR(100) COMMENT "国家",
    referer    VARCHAR(1000) COMMENT "referring URL",
    clicked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT "点击时间",
    FOREIGN KEY (link_id) REFERENCES links (id) COMMENT "外键约束",
    INDEX (link_id, clicked_at) COMMENT "联合索引用于高效统计"
) COMMENT "存储每个链接的统计数据";

# 链接检查表
CREATE TABLE link_checks (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT "自增主键",
    link_id       BIGINT NOT NULL COMMENT "链接id",
    checked_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT "检查时间",
    status        TINYINT COMMENT "检查结果,1:正常 0:失败",
    response_time INT UNSIGNED COMMENT "响应时间(ms)",
    FOREIGN KEY (link_id) REFERENCES links (id) COMMENT "外键",
    INDEX (link_id, checked_at) COMMENT "联合索引"
) COMMENT "存储定期检查链接的结果";

# IP黑名单
CREATE TABLE blocked_ips (
    id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT "自增主键",
    ip_address VARCHAR(45) NOT NULL UNIQUE COMMENT "IP地址",
    blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT "拉黑时间",
    blocked_by BIGINT COMMENT "执行拉黑的操作员id,users表外键",
    reason     TEXT COMMENT "拉黑原因",
    INDEX (ip_address) COMMENT "IP索引,加速查询"
) COMMENT "存储被拉黑的IP地址";

# 配置表
CREATE TABLE configs (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT "自增主键",
    name        VARCHAR(100) NOT NULL UNIQUE COMMENT "配置名称,唯一",
    value       VARCHAR(1000) COMMENT "配置值",
    description VARCHAR(1000) COMMENT "配置描述"
) COMMENT "存储系统配置项";

INSERT INTO configs (name, value, description)
VALUES
    ('hash_function', 'sha256', '系统默认使用的哈希算法'),
    ('prevent_abuse_enabled', '1', '是否启用防止滥用策略'),
    ('logic_generator_count', '2', '逻辑发号器数量');