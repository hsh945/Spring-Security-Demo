create table system_user_role
(
    id      bigint auto_increment
        primary key,
    user_id bigint not null,
    role_id bigint not null
)
    charset = utf8mb4;

INSERT INTO system_user_role (id, user_id, role_id) VALUES (1, 1, 1);
