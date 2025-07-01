create table system_menu
(
    id          bigint auto_increment
        primary key,
    parent_id   bigint       null comment '父菜单ID，一级菜单为0',
    name        varchar(64)  not null,
    path        varchar(255) null comment '菜单URL',
    permissions varchar(255) null comment '授权(多个用逗号分隔，如：user:list,user:create)',
    component   varchar(255) null,
    type        int          not null comment '类型     0：目录   1：菜单   2：按钮',
    icon        varchar(32)  null comment '菜单图标',
    orderNum    int          null comment '排序',
    create_time datetime     not null,
    modify_time datetime     null,
    status      tinyint(1)   null comment '启用:1 , 禁用: 0',
    deleted     int          not null,
    constraint name
        unique (name)
)
    charset = utf8mb3;



INSERT INTO system_menu (id, parent_id, name, path, permissions, component, type, icon, orderNum, create_time, modify_time, status, deleted) VALUES (1, 0, '系统管理', '', 'system:manage', '', 0, 'setting', 1, '2025-07-01 00:00:00', '2025-07-01 00:00:00', 1, 0);
INSERT INTO system_menu (id, parent_id, name, path, permissions, component, type, icon, orderNum, create_time, modify_time, status, deleted) VALUES (2, 1, '用户管理', '/system/users', 'system:user:list', 'system/User', 1, 'user', 1, '2025-07-01 00:00:00', '2025-07-01 00:00:00', 1, 0);
INSERT INTO system_menu (id, parent_id, name, path, permissions, component, type, icon, orderNum, create_time, modify_time, status, deleted) VALUES (3, 1, '角色管理', '/system/roles', 'system:role:list', 'system/Role', 1, 'avatar', 2, '2025-07-01 00:00:00', '2025-07-01 00:00:00', 1, 0);
INSERT INTO system_menu (id, parent_id, name, path, permissions, component, type, icon, orderNum, create_time, modify_time, status, deleted) VALUES (4, 1, '菜单管理', '/system/menus', 'system:menu:list', 'system/Menu', 1, 'operation', 3, '2025-07-01 00:00:00', '2025-07-01 00:00:00', 1, 0);
