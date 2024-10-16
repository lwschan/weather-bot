CREATE TABLE IF NOT EXISTS users
(
    id                BIGSERIAL PRIMARY KEY,
    external_platform VARCHAR(255) NOT NULL,
    external_user_id  BIGINT       NOT NULL,
    created_on        TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_user_external_platform_external_user_id
    ON users (external_platform, external_user_id);
