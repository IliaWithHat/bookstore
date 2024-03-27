## Requirements

- Java 17
- Postgres

## Installation

### Using Docker

- Download project.
- `./gradlew bootRun`.
- `docker-compose up -d`

### Without Docker

- Download project.
- Create db and initialize it with a [script](src/main/resources/sql/init.sql).
- Edit [application.yml](src/main/resources/application.yml). Write the current url, username and
  password.
- Run project by `./gradlew bootRun`.

Now you can send and receive requests to localhost:6565