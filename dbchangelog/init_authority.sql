-- DEFAULT CHARACTER SET utf8mb4
-- COLLATE utf8mb4_unicode_ci
-- DEFAULT ENCRYPTION='N'
USE `ars_user`;
-- Server version 8.0.37

-- Create SUPER_ADMIN role
INSERT INTO roles (
    name,
    code,
    normalized_name,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
) VALUES (
    'Administrator',
    'ROLE_ADMIN',
    'administrator',
    'system',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP
),
(
    'Shop owner',
    'ROLE_DEFAULT',
    'shop_owner',
    'system',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP
);

-- ADMIN
INSERT INTO `authority` (`name`, `code`, `description`, `parent_id`, `parent_code`, `created_by`, `last_modified_by`)
VALUES ('authority.admin', '00', 'authority.admin.description', NULL, NULL, 'admin', 'admin');


-- Manage System
INSERT INTO `authority` (`name`, `code`, `description`, `parent_id`, `parent_code`, `created_by`, `last_modified_by`)
VALUES ('authority.system', '01', 'authority.system.description', NULL, NULL, 'admin', 'admin');

SET @system_id = LAST_INSERT_ID();

INSERT INTO `authority` (`name`, `code`, `description`, `parent_id`, `parent_code`, `created_by`, `last_modified_by`) VALUES
('authority.system.update', '0101', 'authority.system.update.description', @system_id, '01', 'admin', 'admin');

-- Manage Users
INSERT INTO `authority` (`name`, `code`, `description`, `parent_id`, `parent_code`, `created_by`, `last_modified_by`)
VALUES ('authority.user', '02', 'authority.user.description', NULL, NULL, 'admin', 'admin');

SET @user_id = LAST_INSERT_ID();

INSERT INTO `authority` (`name`, `code`, `description`, `parent_id`, `parent_code`, `created_by`, `last_modified_by`) VALUES
('authority.user.create', '0201', 'authority.user.create.description', @user_id, '02', 'admin', 'admin'),
('authority.user.update', '0202', 'authority.user.update.description', @user_id, '02', 'admin', 'admin'),
('authority.user.delete', '0203', 'authority.user.delete.description', @user_id, '02', 'admin', 'admin');

-- Manage Roles
INSERT INTO `authority` (`name`, `code`, `description`, `parent_id`, `parent_code`, `created_by`, `last_modified_by`)
VALUES ('authority.role', '03', 'authority.role.description', NULL, NULL, 'admin', 'admin');

SET @role_id = LAST_INSERT_ID();

INSERT INTO `authority` (`name`, `code`, `description`, `parent_id`, `parent_code`, `created_by`, `last_modified_by`) VALUES
('authority.role.create', '0301', 'authority.role.create.description', @role_id, '03', 'admin', 'admin'),
('authority.role.update', '0302', 'authority.role.update.description', @role_id, '03', 'admin', 'admin'),
('authority.role.delete', '0303', 'authority.role.delete.description', @role_id, '03', 'admin', 'admin');


START TRANSACTION;
-- Delete all old rights of ROLE_ADMIN
DELETE ra
FROM role_authority ra JOIN roles r ON ra.role_id = r.id
WHERE r.code = 'ROLE_ADMIN';

-- Re-add all permissions to ROLE_ADMIN
INSERT INTO role_authority (
    role_id,
    authority_id,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
)
SELECT
    r.id AS role_id,
    a.id AS authority_id,
    'system' AS created_by,
    CURRENT_TIMESTAMP AS created_date,
    'system' AS last_modified_by,
    CURRENT_TIMESTAMP AS last_modified_date
FROM roles r CROSS JOIN authority a
WHERE r.code = 'ROLE_ADMIN';

COMMIT;


START TRANSACTION;
-- Delete all old rights of ROLE_DEFAULT
DELETE ra
FROM role_authority ra JOIN roles r ON ra.role_id = r.id
WHERE r.code = 'ROLE_DEFAULT';

-- Re-add all permissions to ROLE_DEFAULT
INSERT INTO role_authority (
    role_id,
    authority_id,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
)
SELECT
    r.id AS role_id,
    a.id AS authority_id,
    'system' AS created_by,
    CURRENT_TIMESTAMP AS created_date,
    'system' AS last_modified_by,
    CURRENT_TIMESTAMP AS last_modified_date
FROM roles r CROSS JOIN authority a
WHERE r.code = 'ROLE_DEFAULT' AND a.code NOT IN ('00', '01', '0101', '0102');

COMMIT;
