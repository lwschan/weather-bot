CREATE TABLE IF NOT EXISTS user_default_locations
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT REFERENCES users (id),
    name              TEXT             NOT NULL,
    formatted_address TEXT             NOT NULL,
    latitude          DOUBLE PRECISION NOT NULL,
    longitude         DOUBLE PRECISION NOT NULL,
    created_on        TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    last_updated_on   TIMESTAMPTZ      NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_user_default_locations_user_id
    ON user_default_locations (user_id);
