# REST API using Vertx project, Flayway and MySQL

README in Portuguese: [here](docs/README.md)

This repository presents a use case for the [Vertx Project](https://vertx.io/) in the development of Asynchronous REST APIs. Here the objective is to show some tools that Vertx has, among them are, HTTP Server and Client, Router Context, JDBC Driver Async, Failure Handler, Application Configuration, Logger and more.

The data migration process is being managed by [Flyway](https://flywaydb.org/). Data persistence is done with [MySQL](https://www.mysql.com/).

## TODO List
- [ ] Create CRUD Async REST API  using Vertx tools
- [x] Crete persistence strategy with MySql
- [x] Implement Hexagonal Architecture strategy (isolate model)
- [x] Migration data with Flyway
- [ ] Create request failure handler
- [ ] Create docker build
