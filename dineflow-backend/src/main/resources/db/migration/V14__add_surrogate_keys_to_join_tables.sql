-- Add surrogate keys to join tables for better JPA support

-- Add id column to user_roles
ALTER TABLE user_roles
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uk_user_roles_user_role (user_id, role_id);

-- Add id column to role_permissions  
ALTER TABLE role_permissions
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uk_role_permissions_role_permission (role_id, permission_id);
