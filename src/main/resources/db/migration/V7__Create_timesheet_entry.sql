CREATE TABLE timesheet_entry (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  timesheet_id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  time_category_id BIGINT NOT NULL,
  quantity BIGINT NOT NULL,
  date DATE NOT NULL,
  description VARCHAR(255),
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_timesheetentry PRIMARY KEY (id)
);

ALTER TABLE timesheet_entry ADD CONSTRAINT FK_TIMESHEETENTRY_ON_TASK FOREIGN KEY (task_id) REFERENCES task (id);

ALTER TABLE timesheet_entry ADD CONSTRAINT FK_TIMESHEETENTRY_ON_TIMESHEET FOREIGN KEY (timesheet_id) REFERENCES timesheet (id);

ALTER TABLE timesheet_entry ADD CONSTRAINT FK_TIMESHEETENTRY_ON_TIME_CATEGORY FOREIGN KEY (time_category_id) REFERENCES time_category (id);

