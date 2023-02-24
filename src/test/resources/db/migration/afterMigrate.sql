INSERT INTO customer(name, description, created, updated, enabled, status) VALUES
    ('Maurin Makkara Oy', 'Get the pile', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, 'ACTIVE'),
    ('Matin Makkara Oy', 'Get the pile', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, 'ACTIVE');

INSERT INTO employee(first_name, last_name, email, start_date, created, updated, firebase_uid, role, status) VALUES
    ('Testi', 'Tekijä', 'testi@tekija.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1234-4321', 'USER', 'ACTIVE'),
    ('Test', 'Worker', 'test@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1111-2222', 'USER', 'ACTIVE'),
    ('Matti', 'Meikälainen', 'matti@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1111-3333', 'USER', 'ACTIVE'),
    ('Teesti', 'Testinen', 'teesti@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1111-4444', 'USER', 'ACTIVE');

INSERT INTO project(customer_id, employee_id, name, description, start_date, status, created, updated) VALUES
    (1, 1, 'test', 'testing', CURRENT_DATE, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 1, 'test2', 'testing delete', CURRENT_DATE, 'ARCHIVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO timesheet(project_id, employee_id, name, description, allocation, created, updated, status) VALUES
    (1, 1, 'test', 'testing', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
    (1, 2,'test2', 'testing', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
    (1, 3,'test2', 'testing delete', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO task(project_id, name, description, created, updated, billable, status) VALUES
    (1, 'test', 'testing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'ACTIVE'),
    (1, 'test2', 'testing delete', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'ACTIVE');

INSERT INTO timesheet_entry(timesheet_id, task_id, quantity, date, description, created, updated) VALUES
    (1, 1, 7.5, '2022-04-01', 'Testing timesheet entry', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 1, 7.5, '2022-04-03', 'Testing timesheet entry2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, 7.5, '2022-04-01', 'Testing timesheet entry3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, 1, '2022-04-03', 'Testing timesheet entry4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
