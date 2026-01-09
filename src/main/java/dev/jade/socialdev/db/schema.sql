CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT REFERENCES users(id) NOT NULL,
    recipient_id BIGINT REFERENCES users(id),
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(200) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);