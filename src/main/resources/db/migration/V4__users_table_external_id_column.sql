ALTER TABLE users
    ADD COLUMN external_id TEXT NULL;

ALTER TABLE users
    ALTER COLUMN external_platform TYPE TEXT;