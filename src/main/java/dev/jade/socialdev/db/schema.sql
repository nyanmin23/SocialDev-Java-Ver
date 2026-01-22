CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(200) UNIQUE NOT NULL,
    password   TEXT                NOT NULL,
    created_at TIMESTAMP           NOT NULL
);

CREATE TABLE messages
(
    id           BIGSERIAL PRIMARY KEY,
    sender_id    BIGINT REFERENCES users (id) NOT NULL,
    recipient_id BIGINT REFERENCES users (id),
    content      TEXT                         NOT NULL,
    created_at   TIMESTAMP                    NOT NULL
);

-- DROP TABLE users CASCADE;
-- DROP TABLE messages;

-- DROP SEQUENCE users_id_sequence;
-- DROP SEQUENCE messages_id_sequence;

-- ALTER SEQUENCE users_id_seq RESTART WITH 1;
-- ALTER SEQUENCE messages_id_seq RESTART WITH 1;