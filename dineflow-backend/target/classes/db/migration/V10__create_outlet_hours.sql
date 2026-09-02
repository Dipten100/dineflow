CREATE TABLE outlet_regular_hours (
    id BIGINT NOT NULL AUTO_INCREMENT,

    outlet_id BIGINT NOT NULL,

    day_of_week VARCHAR(20) NOT NULL,

    period_number INT NOT NULL DEFAULT 1,

    open_time TIME NULL,

    close_time TIME NULL,

    is_closed BOOLEAN NOT NULL DEFAULT FALSE,

    created_at DATETIME NOT NULL,

    updated_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_regular_hours_outlet
        FOREIGN KEY (outlet_id)
        REFERENCES outlets(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_regular_hours_period
        UNIQUE (
            outlet_id,
            day_of_week,
            period_number
        )
);

CREATE TABLE outlet_special_hours (
    id BIGINT NOT NULL AUTO_INCREMENT,

    outlet_id BIGINT NOT NULL,

    special_date DATE NOT NULL,

    open_time TIME NULL,

    close_time TIME NULL,

    is_closed BOOLEAN NOT NULL DEFAULT FALSE,

    reason VARCHAR(255),

    created_at DATETIME NOT NULL,

    updated_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_special_hours_outlet
        FOREIGN KEY (outlet_id)
        REFERENCES outlets(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_special_hours_date
        UNIQUE (
            outlet_id,
            special_date
        )
);