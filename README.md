# epoc-backend

## Requirements

### MacOS
- IDE, Intellij IDEA Community the best one out there `brew install intellij-idea-ce`
- openjdk-17 `brew install openjdk@17`
- kotlin & gradle plugins for IDEA

### Ubuntu
- IDEA can be installed either manually or with snap `sudo snap install intellij-idea-community --classic`
- openjdk-17 `sudo apt install openjdk-17-jdk`
- kotlin & gradle plugins for IDEA

### Run app
On IDEA create new gradle configuration and use `bootRun` as the run argument with env variables

| variable                                              | value                                                                                     |
|-------------------------------------------------------|-------------------------------------------------------------------------------------------|
| SPRING_DATASOURCE_USERNAME                            | user                                                                                      |
| SPRING_DATASOURCE_PASSWORD                            | password                                                                                  |
| SPRING_DATASOURCE_URL                                 | jdbc:postgresql://localhost/epoc                                                          |
| SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER-URI  | https://securetoken.google.com/<firebase-app-name>                                        |
| SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI | https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com |

Run a postgres db in a container
```bash
docker run --rm --name postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=epoc -p 5432:5432 postgres:14-alpine
```

## Run tests
This app uses flyway for migrations, so JPA Buddy plugin for Intellij IDEA is a tool that can be used to create migration files.
The tests use [this embedded database](https://github.com/zonkyio/embedded-database-spring-test), which is essentially a wrapper for testcontainers, so docker is needed on host machine when running tests.
Run tests with `gradle :clean :test` or create a new gradle run configuration on IDEA and use `clean test` as run command.
