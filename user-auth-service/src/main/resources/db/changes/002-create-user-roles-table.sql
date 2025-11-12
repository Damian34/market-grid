--liquibase formatted sql

--changeset damian:002_create_roles
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

--changeset damian:003_create_user_roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_roles_to_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_to_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT uk_user_roles UNIQUE (user_id, role_id)
);
