CREATE DATABASE IF NOT EXISTS `ars_user`;
-- DEFAULT CHARACTER SET utf8mb4
-- COLLATE utf8mb4_unicode_ci
-- DEFAULT ENCRYPTION='N'
USE `ars_user`;
-- Server version 8.0.37

-- Insert default super admin
INSERT INTO users (
    username,
    password,
    email,
    fullname,
    normalized_name,
    phone,
    type,
    is_admin,
    status,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
) VALUES (
    'admin',
    '$2a$12$gktW54NWmDzOCWwUNdkhOuJ4SIcYEHBudpIr.kAozvLWhRXgYL3F.',
    'admin@example.com',
    'Administrator',
    'administrator',
    '0123456789',
    'ADMIN',
    1,
    1,
    'system',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP
);

SET @admin_user_id = LAST_INSERT_ID();
SELECT id INTO @role_admin_id FROM roles WHERE code = 'ROLE_ADMIN';

-- Assign ROLE_ADMIN to admin
INSERT INTO user_role (
    user_id,
    role_id,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
) VALUES (
    @admin_user_id,
    @role_admin_id,
    'system',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP
);
