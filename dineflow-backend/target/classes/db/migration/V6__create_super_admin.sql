INSERT INTO users (
    email,
    phone,
    password,
    first_name,
    last_name,
    status,
    created_at,
    updated_at
)
VALUES (
    'superadmin@dineflow.com',
    NULL,
    '{bcrypt}$2a$10$YOUR_GENERATED_HASH',
    'DineFlow',
    'Super Admin',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);