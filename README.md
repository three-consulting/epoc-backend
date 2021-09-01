# epoc-backend

## Requirements
- IDE, Intellij IDEA Community the best one out there `brew install intellij-idea-ce`
- openjdk-11 `brew install openjdk@11`
- kotlin & gradle plugins for IDEA

### Run app
On IDEA create new gradle configuration and use `bootRun` as the run argument

## Run tests
This app uses flyway for migrations, so JPA Buddy plugin for Intellij IDEA is a tool that can be used to create migration files.
The tests use [this embedded database](https://github.com/zonkyio/embedded-database-spring-test), which is essentially a wrapper for testcontainers, so docker is needed on host machine when running tests.
Run tests with `gradle :clean :test` or create a new gradle run configuration on IDEA and use `clean test` as run command.
