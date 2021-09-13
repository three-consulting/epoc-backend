CREATE TABLE employee (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  start_date DATE NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_employee PRIMARY KEY (id)
);

INSERT INTO employee( first_name, last_name, email, start_date, created, updated)
VALUES
    ( 'Testi', 'Tekijä', 'testi@tekija.fi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
