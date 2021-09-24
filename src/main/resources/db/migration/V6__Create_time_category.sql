CREATE TABLE time_category (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_timecategory PRIMARY KEY (id)
);

ALTER TABLE time_category ADD CONSTRAINT uc_timecategory_name UNIQUE (name);
