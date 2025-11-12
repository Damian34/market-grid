--liquibase formatted sql

--changeset damian:001_1
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    provider VARCHAR(20) NOT NULL,
    external_id VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    family_name VARCHAR(100),
    email VARCHAR(100),
    CONSTRAINT uk_external_identity UNIQUE (provider, external_id)
);
