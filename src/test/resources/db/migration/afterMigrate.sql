INSERT INTO customer(name, description, created, updated, enabled, status) VALUES
    ('Maurin Makkara Oy', 'Get the pile', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, 'ACTIVE'),
    ('Matin Makkara Oy', 'Get the pile', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, 'ACTIVE'),
    ('Poisto Makkara Oy', 'Delete this', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, 'ACTIVE');

INSERT INTO employee(first_name, last_name, email, start_date, created, updated, firebase_uid, role, status) VALUES
    ('Testi', 'Tekijä', 'testi@tekija.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1234-4321', 'USER', 'ACTIVE'),
    ('Test', 'Worker', 'test@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1111-2222', 'USER', 'ACTIVE'),
    ('Matti', 'Meikälainen', 'matti@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1111-3333', 'USER', 'ACTIVE'),
    ('Teesti', 'Testinen', 'teesti@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1111-4444', 'USER', 'ACTIVE');

INSERT INTO project(customer_id, employee_id, name, description, start_date, status, created, updated) VALUES
    (1, 1, 'project', 'testing', CURRENT_DATE, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 1, 'project2', 'testing delete', CURRENT_DATE, 'ARCHIVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, 'project3', 'testing', CURRENT_DATE, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO timesheet(project_id, employee_id, name, description, allocation, created, updated, status) VALUES
    (1, 1, 'timesheet', 'testing', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
    (1, 2,'timesheet2', 'testing', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
    (1, 3,'timesheet2', 'testing delete', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
    (3, 1, 'timesheet4', 'testing', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
    (3, 2, 'timesheet5', 'testing', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO task(project_id, name, description, created, updated, billable, status) VALUES
    (1, 'task', 'testing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'ACTIVE'),
    (1, 'task2', 'testing delete', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'ACTIVE'),
    (1, 'task3', 'testing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'ACTIVE');

INSERT INTO timesheet_entry(timesheet_id, task_id, quantity, date, description, created, updated, flex) VALUES
    (1, 1, 7.5, '2022-04-01', 'Testing timesheet entry', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1.5),
    (1, 1, 7.5, '2022-04-03', 'Testing timesheet entry2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (2, 1, 7.5, '2022-04-01', 'Testing timesheet entry3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (2, 1, 1, '2022-04-03', 'Testing timesheet entry4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (3, 1, 7.5, '2023-03-01', 'Testing timesheet entry5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (3, 1, 7.5, '2023-03-03', 'Testing timesheet entry6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (3, 1, 7.5, '2023-03-02', 'Testing timesheet entry7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (1, 1, 7.5, '2023-04-01', 'Testing timesheet entry8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (2, 1, 7.5, '2023-04-02', 'Testing timesheet entry9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (3, 1, 7.5, '2023-04-03', 'Testing timesheet entry10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (4, 3, 7.5, '2023-04-04', 'Testing timesheet entry11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
    (5, 3, 7.5, '2023-04-05', 'Testing timesheet entry12', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
