CREATE TABLE IF NOT EXISTS user_default_locations
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES users (id),
    latitude        DOUBLE PRECISION NOT NULL,
    longitude       DOUBLE PRECISION NOT NULL,
    created_on      TIMESTAMP        NOT NULL DEFAULT NOW(),
    last_updated_on TIMESTAMP        NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_user_default_locations_user_id
    ON user_default_locations (user_id);
