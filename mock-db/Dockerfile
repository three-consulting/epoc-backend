FROM postgres:14.0-alpine

ENV POSTGRES_USER=user
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=epoc

COPY ./db.sql /docker-entrypoint-initdb.d/1_db.sql
COPY ./employee-secrets.sql /docker-entrypoint-initdb.d/2_employee-secrets.sql
COPY ./data.sql /docker-entrypoint-initdb.d/3_data.sql

RUN chmod a+r /docker-entrypoint-initdb.d/*

EXPOSE 5432
