create table system_role
(
    id          bigint auto_increment
        primary key,
    name        varchar(64) not null,
    code        varchar(64) not null,
    remark      varchar(64) null comment '备注',
    create_time datetime    null,
    modify_time datetime    null,
    status      tinyint(1)  null,
    deleted     int         not null,
    constraint code
        unique (code),
    constraint name
        unique (name)
)
    charset = utf8mb3;

INSERT INTO system_role (id, name, code, remark, create_time, modify_time, status, deleted)
VALUES (1, '超级管理员角色', 'admin', '系统默认最高权限，不可以编辑和任意修改', '2025-07-01 00:00:00', '2025-07-01 00:00:00',
        1, 0);
