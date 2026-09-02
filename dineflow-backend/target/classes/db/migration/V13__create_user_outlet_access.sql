CREATE TABLE user_outlets (

    id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,

    outlet_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_user_outlet_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_outlet_outlet
        FOREIGN KEY (outlet_id)
        REFERENCES outlets(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_user_outlet
        UNIQUE (
            user_id,
            outlet_id
        )
);

CREATE INDEX idx_user_outlets_user
    ON user_outlets(user_id);

CREATE INDEX idx_user_outlets_outlet
    ON user_outlets(outlet_id);