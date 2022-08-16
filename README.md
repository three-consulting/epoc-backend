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

### Manage secret
Firebase admin sdk service account credentials are contained as a gpg encrypted file
Decrypt by running
```bash
gopass show 3/epoc/GPG | gpg -q --batch --yes --decrypt-files --passphrase-fd 0 *.gpg
```

Encrypt by running
```bash
gpg --batch --yes --symmetric --passphrase $(gopass show 3/epoc/GPG) --cipher-algo AES-256 firebase/epoc-auth-firebase-adminsdk.json
```

### Run app
On IDEA [create new gradle configuration](https://www.jetbrains.com/help/idea/run-debug-gradle.html) and use `bootRun` as the run argument with env variables

| variable                   | value                            |
|----------------------------|----------------------------------|
| SPRING_DATASOURCE_USERNAME | user                             |
| SPRING_DATASOURCE_PASSWORD | password                         |
| SPRING_DATASOURCE_URL      | jdbc:postgresql://localhost/epoc |

To run the application with security enabled add the following envs, which can also be found in [shared secrets](https://github.com/three-consulting/secrets) under `edam/epoc/auth/`

| variable                                              | value                                                                                     |
|-------------------------------------------------------|-------------------------------------------------------------------------------------------|
| GOOGLE_APPLICATION_CREDENTIALS                        | firebase/epoc-auth-firebase-adminsdk.json                                                 |
| SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER-URI  | https://securetoken.google.com/<firebase-app-name>                                        |
| SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI | https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com |

To run the application without spring security enabled use the following env

| variable               | value |
|------------------------|-------|
| SPRING_PROFILES_ACTIVE | dev   |

Once running locally, the api docs can be found at `localhost:8080/docs-ui.html`

## Run a database
Run a postgres db in a container
```bash
docker run --rm --name postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=epoc -p 5432:5432 postgres:14-alpine
```

Run the db with seed data in a container
```bash
docker run --rm --name epoc-db -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=epoc -p 5432:5432 ghcr.io/three-consulting/epoc-db:latest
```

## Run tests
The tests use [this embedded database](https://github.com/zonkyio/embedded-database-spring-test), which is essentially a wrapper for testcontainers, so docker is needed on host machine when running tests.
Run tests with `gradle :clean :test` or create a new gradle run configuration on IDEA and use `clean test` as run command.
