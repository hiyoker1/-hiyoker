CREATE DATABASE IF NOT EXISTS yoker_admin DEFAULT CHARACTER SET utf8mb4;

USE yoker_admin;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(50)  NOT NULL                COMMENT '用户名',
    password    VARCHAR(100) NOT NULL                COMMENT '密码',
    nickname    VARCHAR(50)                          COMMENT '昵称',
    phone       VARCHAR(20)                          COMMENT '手机号',
    email       VARCHAR(100)                         COMMENT '邮箱',
    avatar      VARCHAR(255)                         COMMENT '头像',
    status      TINYINT      NOT NULL DEFAULT 1      COMMENT '状态 0禁用 1启用',
    deleted     TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除 0正常 1删除',
    create_time DATETIME     NOT NULL DEFAULT NOW()  COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT NOW()  COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='系统用户表';

-- 初始管理员（密码：123456）
INSERT INTO sys_user (username, password, nickname, status)
VALUES ('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', 1);