CREATE TABLE customer (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  enabled BOOLEAN NOT NULL,
  CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE employee (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  start_date date NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE project (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  customer_id BIGINT NOT NULL,
  employee_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  start_date date NOT NULL,
  end_date date,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  status VARCHAR(255) NOT NULL,
  CONSTRAINT pk_project PRIMARY KEY (id)
);

CREATE TABLE task (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  project_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  billable BOOLEAN NOT NULL,
  status VARCHAR(255) NOT NULL,
  CONSTRAINT pk_task PRIMARY KEY (id)
);

CREATE TABLE time_category (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_timecategory PRIMARY KEY (id)
);

CREATE TABLE timesheet (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  project_id BIGINT NOT NULL,
  employee_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  allocation INTEGER NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  status VARCHAR(255) NOT NULL,
  CONSTRAINT pk_timesheet PRIMARY KEY (id)
);

CREATE TABLE timesheet_entry (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  timesheet_id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  time_category_id BIGINT NOT NULL,
  quantity BIGINT NOT NULL,
  date date NOT NULL,
  description VARCHAR(255),
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_timesheetentry PRIMARY KEY (id)
);

ALTER TABLE timesheet ADD CONSTRAINT uc_91f52f1090db07993be2657d9 UNIQUE (project_id, employee_id);

ALTER TABLE customer ADD CONSTRAINT uc_customer_name UNIQUE (name);

ALTER TABLE employee ADD CONSTRAINT uc_employee_email UNIQUE (email);

ALTER TABLE time_category ADD CONSTRAINT uc_timecategory_name UNIQUE (name);

ALTER TABLE project ADD CONSTRAINT FK_PROJECT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE project ADD CONSTRAINT FK_PROJECT_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE task ADD CONSTRAINT FK_TASK_ON_PROJECT FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE timesheet_entry ADD CONSTRAINT FK_TIMESHEETENTRY_ON_TASK FOREIGN KEY (task_id) REFERENCES task (id);

ALTER TABLE timesheet_entry ADD CONSTRAINT FK_TIMESHEETENTRY_ON_TIMESHEET FOREIGN KEY (timesheet_id) REFERENCES timesheet (id);

ALTER TABLE timesheet_entry ADD CONSTRAINT FK_TIMESHEETENTRY_ON_TIME_CATEGORY FOREIGN KEY (time_category_id) REFERENCES time_category (id);

ALTER TABLE timesheet ADD CONSTRAINT FK_TIMESHEET_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE timesheet ADD CONSTRAINT FK_TIMESHEET_ON_PROJECT FOREIGN KEY (project_id) REFERENCES project (id);
