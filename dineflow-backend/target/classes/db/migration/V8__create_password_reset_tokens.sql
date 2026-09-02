CREATE TABLE password_reset_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,

    token_hash VARCHAR(255) NOT NULL,

    expires_at DATETIME NOT NULL,

    used_at DATETIME NULL,

    created_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_password_reset_token
        UNIQUE (token_hash)
);

CREATE INDEX idx_password_reset_user
    ON password_reset_tokens(user_id);

CREATE INDEX idx_password_reset_expires
    ON password_reset_tokens(expires_at);