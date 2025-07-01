create table system_role_menu
(
    id      bigint auto_increment
        primary key,
    role_id bigint not null,
    menu_id bigint not null
)
    charset = utf8mb4;


INSERT INTO system_role_menu (id, role_id, menu_id) VALUES (1, 1, 1);
INSERT INTO system_role_menu (id, role_id, menu_id) VALUES (2, 1, 2);
INSERT INTO system_role_menu (id, role_id, menu_id) VALUES (3, 1, 3);
INSERT INTO system_role_menu (id, role_id, menu_id) VALUES (4, 1, 4);
