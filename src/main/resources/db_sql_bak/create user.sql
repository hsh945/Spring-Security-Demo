create table user
(
    id          bigint auto_increment comment 'ID'
        primary key,
    username    varchar(64)  null comment '用户名',
    password    varchar(255) null comment '密码',
    phone       varchar(64)  null comment '手机号',
    nick_name   varchar(255) null comment '昵称',
    first_name  varchar(255) null comment '姓',
    last_name   varchar(255) null comment '名',
    email       varchar(64)  null comment '邮箱',
    remark      varchar(255) null comment '备注',
    gender      tinyint      null comment '性别',
    birth_day   date         null comment '生日',
    create_time datetime     null comment '创建日期',
    modify_time datetime     null comment '修改日期',
    deleted     tinyint(1)   null comment '删除标识'
)
    charset = utf8mb4;

-- 默认的超级管理员， 密码123456，供你测试使用
INSERT INTO user (id, username, password, phone, nick_name, first_name, last_name, email, remark, gender,
                  birth_day, create_time, modify_time, deleted)
VALUES (1, 'admin', '$2a$10$gSmqqWHRqRqJdIvmvpA6cu9YXKtK0bWn9sD2K/qu/HhvODbSxtB6K', null, '超级管理员', null, null,
        null, '超级管理员，演示用', 1, '2000-01-01 00:00:00', '2025-07-01 00:00:00', '2025-07-01 00:00:00', 0);

