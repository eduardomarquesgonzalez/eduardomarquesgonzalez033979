-- V6__insert_initial_users.sql
INSERT INTO users (username, password, role, active, created_at)
VALUES (
    'admin',
    '$2a$12$gdJD8/CuG/UjWxjPqeqfpuR4ITv5oGKPnm8VipnKWaJdeCt1r861e',
    'ADMIN',
    true,
    CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, role, active, created_at)
VALUES (
    'user',
    '$2a$12$W1.TYRzK2UDYnV2XV7T8qe2JcqpvMmET7lCDemwKG79b.jEvJAdeW',
    'USER',
    true,
    CURRENT_TIMESTAMP
);
