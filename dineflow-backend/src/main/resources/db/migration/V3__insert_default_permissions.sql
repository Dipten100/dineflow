INSERT INTO permissions
    (name, description, module, action)
VALUES

-- USER
('USER_VIEW', 'View users', 'USER', 'VIEW'),
('USER_CREATE', 'Create users', 'USER', 'CREATE'),
('USER_UPDATE', 'Update users', 'USER', 'UPDATE'),
('USER_DELETE', 'Delete users', 'USER', 'DELETE'),

-- ROLE
('ROLE_VIEW', 'View roles', 'ROLE', 'VIEW'),
('ROLE_CREATE', 'Create roles', 'ROLE', 'CREATE'),
('ROLE_UPDATE', 'Update roles', 'ROLE', 'UPDATE'),
('ROLE_DELETE', 'Delete roles', 'ROLE', 'DELETE'),

-- PERMISSION
('PERMISSION_VIEW', 'View permissions', 'PERMISSION', 'VIEW'),
('PERMISSION_CREATE', 'Create permissions', 'PERMISSION', 'CREATE'),
('PERMISSION_UPDATE', 'Update permissions', 'PERMISSION', 'UPDATE'),
('PERMISSION_DELETE', 'Delete permissions', 'PERMISSION', 'DELETE'),

-- RESTAURANT
('RESTAURANT_VIEW', 'View restaurants', 'RESTAURANT', 'VIEW'),
('RESTAURANT_CREATE', 'Create restaurant', 'RESTAURANT', 'CREATE'),
('RESTAURANT_UPDATE', 'Update restaurant', 'RESTAURANT', 'UPDATE'),
('RESTAURANT_DELETE', 'Delete restaurant', 'RESTAURANT', 'DELETE'),

-- OUTLET
('OUTLET_VIEW', 'View outlets', 'OUTLET', 'VIEW'),
('OUTLET_CREATE', 'Create outlet', 'OUTLET', 'CREATE'),
('OUTLET_UPDATE', 'Update outlet', 'OUTLET', 'UPDATE'),
('OUTLET_DELETE', 'Delete outlet', 'OUTLET', 'DELETE'),

-- MENU
('MENU_VIEW', 'View menu', 'MENU', 'VIEW'),
('MENU_CREATE', 'Create menu item', 'MENU', 'CREATE'),
('MENU_UPDATE', 'Update menu item', 'MENU', 'UPDATE'),
('MENU_DELETE', 'Delete menu item', 'MENU', 'DELETE'),

-- ORDER
('ORDER_VIEW', 'View orders', 'ORDER', 'VIEW'),
('ORDER_CREATE', 'Create order', 'ORDER', 'CREATE'),
('ORDER_UPDATE', 'Update order', 'ORDER', 'UPDATE'),
('ORDER_CANCEL', 'Cancel order', 'ORDER', 'CANCEL'),

-- PAYMENT
('PAYMENT_VIEW', 'View payments', 'PAYMENT', 'VIEW'),
('PAYMENT_CREATE', 'Create payment', 'PAYMENT', 'CREATE'),

-- REPORT
('REPORT_VIEW', 'View reports', 'REPORT', 'VIEW');