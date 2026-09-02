INSERT INTO user_roles (
    user_id,
    role_id
)
SELECT
    u.id,
    r.id
FROM users u
CROSS JOIN roles r
WHERE u.email = 'superadmin@dineflow.com'
AND r.name = 'SUPER_ADMIN';