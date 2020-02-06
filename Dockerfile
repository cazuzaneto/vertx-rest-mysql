FROM openjdk:8-jre-alpine

ENV APP_FILE="vertx-rest-mysql-fat.jar"

ENV APP_HOME /opt

COPY target/$APP_FILE app.jar

ENTRYPOINT [ "sh", "-c", "java -jar app.jar" ]
