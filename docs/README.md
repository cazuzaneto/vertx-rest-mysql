# API REST usando o projeto Vertx, Flyway e MySQL

Este repositório apresenta um caso de uso do [Eclipse Vertx](https://vertx.io/) no desenvolvimento de APIs REST Assícronas. Aqui o objetivo é mostrar algumas ferramentas que o Vertx dispõe, dentre elas estão, HTTP Server and Client, Router Context, JDBC Driver Async, Failure Handler, Application Configuration, Logger and more.

O processo de migração de dados está sendo gerenciado pelo [Flyway](https://flywaydb.org/). Já e a persistência de dados é feita com o [MySQL](https://www.mysql.com/).

Caso você não queria instalar o Mysql em sua máquina, você pode usar o `docker-compose.yml` que tem a imagem do banco de dados para você. Basta usar o commando `docker-compose up -d` que você terá o MySql rodando na sua máquina através do docker.

## TODO List
- [x] Criar uma api rest assíncrona usando o kit de ferramentas do Vertx;
- [x] Criar estratétia de persistência com MySQL;
- [x] Implementar estratégia de arquitetura hexagonal (isolar o modelo);
- [x] Migração de dados com Flyway;
- [x] Usar o gerador de código do Vertx;
- [x] Criar gerenciamento de falhas nas requesições;
- [ ] Criar build com docker;
- [ ] Criar documentação do projeto, com explicações sobre o código.

