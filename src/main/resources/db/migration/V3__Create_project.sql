CREATE TABLE project (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  customer_id BIGINT,
  employee_id BIGINT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  starting_date DATE NOT NULL,
  end_date DATE,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_project PRIMARY KEY (id)
);

ALTER TABLE project ADD CONSTRAINT FK_PROJECT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE project ADD CONSTRAINT FK_PROJECT_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employee (id);

INSERT INTO project(customer_id,employee_id,name,description,starting_date,created,updated)
VALUES
    (1,1,'test','testing',CURRENT_DATE,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1,1,'test2','testing delete',CURRENT_DATE,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
