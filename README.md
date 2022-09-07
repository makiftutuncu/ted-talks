# TED Talks

## Table of Contents

1. [Introduction](#introduction)
2. [Configuration](#configuration)
3. [Development and Running](#development-and-running)
4. [Docker](#docker)
5. [API](#api-docs)
6. [Contributing](#contributing)
7. [License](#license)

## Introduction

TED Talks is a web backend application. It provides APIs to manage information about TED Talks.

It uses

* Spring Boot 2.7.3
* Java 18
* PostgreSQL for database
* Flyway to manage DB schemas
* Spring Data JPA to database access
* JUnit 5 for testing
* SpringDoc for API documentation

## Configuration

You should be able to run the application out-of-the-box without any further configuration (as long as database is available, see [Docker section](#docker)). However, if you wish to make your own configuration, you can do so via [application.properties](src/main/resources/application.properties). You can also override config values with following environment variables.

| Variable Name | Data Type | Description                          | Required                    |
|---------------|-----------|--------------------------------------|-----------------------------|
| DB_HOST       | String    | Host address of application database | No, defaults to `localhost` |
| DB_PORT       | Integer   | Port of application database         | No, defaults to `5432`      |
| DB_NAME       | String    | Name of application database         | No, defaults to `ted-talks` |
| DB_USER       | String    | User of application database         | No, defaults to `ted-talks` |
| DB_PASSWORD   | String    | Password of application database     | No, defaults to `ted-talks` |

## Development and Running

TED Talks is built with Gradle. Standard Gradle tasks like `clean`, `compile`, `run` and `test` can be used.

If you don't have Gradle installed, you can replace `gradle` commands with `./gradlew` to use Gradle wrapper.

To run the application locally:

```bash
gradle run --console=plain
```

You may send a `POST` request to `/csv/import` endpoint to import some test data from CSV file. You can find a sample CSV file [data.csv](src/main/resources/data.csv).

## Docker

TED Talks can be run in Docker with its database. To build an image, first build the project

```bash
gradle build -x test
```

and then run using `docker-compose`:

```bash
docker-compose up -d --build
```

This will create database container, build the application image, create application container and run everything together in detached mode.

Docker builds can also be customized as follows:

| Variable Name | Data Type | Description                                          | Required                                |
|---------------|-----------|------------------------------------------------------|-----------------------------------------|
| PORT          | Integer   | Port to bind in the host machine for the application | No, defaults to `8080`                  |
| DB_PORT       | Integer   | Port to bind in the host machine for the database    | No, defaults to `5432`                  |

This way, when you run the stack as following

```bash
PORT=80 DB_PORT=1234 docker-compose up -d --build
```

you'll be able to access the application at `http://localhost:80` and the database at `jdbc:postgresql://localhost:1234/ted-talks`.

## API Docs

TED Talks provide OpenAPI documentation and a Swagger UI to browse them. After running the application, you may go to [`/swagger-ui.html`](http://localhost:8080/swagger-ui.html) to see the documentation.

All errors return a message in plain text with a non-OK HTTP status code.

All successful responses will have `200 OK` status unless explicitly mentioned.

## Contributing

All contributions are welcome. Please feel free to send a pull request. Thank you.

## License

TED Talks is licensed with [MIT License](LICENSE.md).
