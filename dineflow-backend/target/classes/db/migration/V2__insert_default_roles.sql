INSERT INTO roles (
    name,
    description,
    system_role
)
VALUES
(
    'SUPER_ADMIN',
    'Full system access',
    TRUE
),
(
    'RESTAURANT_ADMIN',
    'Restaurant administration access',
    FALSE
),
(
    'OUTLET_MANAGER',
    'Outlet management access',
    FALSE
),
(
    'CASHIER',
    'Cashier operations access',
    FALSE
),
(
    'KITCHEN_MANAGER',
    'Kitchen operations access',
    FALSE
),
(
    'CUSTOMER',
    'Customer access',
    FALSE
);