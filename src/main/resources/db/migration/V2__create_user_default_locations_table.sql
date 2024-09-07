CREATE TABLE IF NOT EXISTS user_default_locations
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT REFERENCES users (id),
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);


