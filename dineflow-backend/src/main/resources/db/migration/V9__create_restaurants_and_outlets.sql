CREATE TABLE restaurants (
    id BIGINT NOT NULL AUTO_INCREMENT,

    name VARCHAR(150) NOT NULL,

    description VARCHAR(500),

    email VARCHAR(150),

    phone VARCHAR(30),

    status VARCHAR(30) NOT NULL,

    created_at DATETIME NOT NULL,

    updated_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_restaurant_name
        UNIQUE (name)
);


CREATE TABLE outlets (
    id BIGINT NOT NULL AUTO_INCREMENT,

    restaurant_id BIGINT NOT NULL,

    name VARCHAR(150) NOT NULL,

    code VARCHAR(50) NOT NULL,

    address_line1 VARCHAR(255),

    address_line2 VARCHAR(255),

    city VARCHAR(100),

    state VARCHAR(100),

    postal_code VARCHAR(20),

    phone VARCHAR(30),

    status VARCHAR(30) NOT NULL,

    created_at DATETIME NOT NULL,

    updated_at DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_outlet_restaurant
        FOREIGN KEY (restaurant_id)
        REFERENCES restaurants(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_outlet_code
        UNIQUE (code)
);


CREATE INDEX idx_outlet_restaurant
    ON outlets(restaurant_id);