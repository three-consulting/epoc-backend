INSERT INTO customer( name, description, created, updated, enabled) VALUES
    ('Maurin Makkara Oy', 'Get the pile', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    ('Matin Makkara Oy', 'Get the pile', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

INSERT INTO employee( first_name, last_name, email, start_date, created, updated) VALUES
    ('Testi', 'Tekijä', 'testi@tekija.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Test', 'Worker', 'test@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Matti', 'Meikälainen', 'matti@worker.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO project(customer_id, employee_id, name, description, start_date, status, created, updated) VALUES
    (1, 1, 'test', 'testing', CURRENT_DATE, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 1, 'test2', 'testing delete', CURRENT_DATE, 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO timesheet(project_id, employee_id, name, description, allocation, created, updated, status) VALUES
    (1, 1, 'test', 'testing', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
    (1, 2,'test2', 'testing delete', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO task(project_id, name, description, created, updated, billable, status) VALUES
    (1, 'test', 'testing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'ACTIVE'),
    (1, 'test2', 'testing delete', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'ACTIVE');

INSERT INTO time_category(name, description, created, updated) VALUES
    ('Test work', 'Testing time category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Actual work', 'Working time category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO timesheet_entry(timesheet_id, task_id, time_category_id, quantity, date, description, created, updated) VALUES
    (1, 1, 1, 7.5, CURRENT_DATE, 'Testing timesheet entry', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 1, 1, 7.5, CURRENT_DATE, 'Testing timesheet entry2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
