# REST API using Eclipse Vertx, Flayway and MySQL

README in Portuguese: [here](docs/README.md)

This repository presents a use case for the [Eclipse Vertx](https://vertx.io/) in the development of Asynchronous REST APIs. Here the objective is to show some tools that Vertx has, among them are, HTTP Server and Client, Router Context, JDBC Driver Async, Failure Handler, Application Configuration, Logger and more.

The data migration process is being managed by [Flyway](https://flywaydb.org/). Data persistence is done with [MySQL](https://www.mysql.com/).

If you didn't want to install Mysql on your machine, you can use `docker/docker-compose.yml` which has the database image for you. Just use the command `docker-compose up -d` that you will have MySql running on your machine through the docker.

The application can be launched directly from Maven: (a) to run it using the classes form target/classes: `mvn compile exec:java@run` or (b) to run the fat jar: `mvn package exec:exec@run-app`

## TODO List
- [x] Create CRUD Async REST API  using Vertx toolkit;
- [x] Crete persistence strategy with MySql;
- [x] Implement Hexagonal Architecture strategy (isolate model);
- [x] Migration data with Flyway;
- [X] Use Vertx Code Generation;
- [x] Create request failure handler;
- [ ] Create docker build;
- [ ] Create project documentation, with explanations of the code.
