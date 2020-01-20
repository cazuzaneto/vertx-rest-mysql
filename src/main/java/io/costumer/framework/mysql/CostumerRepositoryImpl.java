package io.costumer.framework.mysql;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

class CostumerRepositoryImpl implements CostumerRepository {
  private final Vertx vertx;
  private final JsonObject config;
  private final SQLClient client;

  CostumerRepositoryImpl(final Vertx vertx, final JsonObject config) {
    this.vertx = vertx;
    this.config = config;
    this.client = JDBCClient.createShared(vertx, this.config);
  }

  @Override
  public Future<List<JsonObject>> findAll() {
    return this.connection().compose(connection -> {
      final Promise<List<JsonObject>> promise = Promise.promise();
      connection.query(SQLStatements.SQL_QUERY_ALL, resultSetAsyncResult -> {
        if (resultSetAsyncResult.failed()) {
          promise.fail(resultSetAsyncResult.cause());
          connection.close();
          return;
        }
        promise.complete(resultSetAsyncResult.result().getRows());
        connection.close();
      });
      return promise.future();
    });
  }

  @Override
  public Future<JsonObject> persist(final JsonObject object) {
    return this.connection().compose(connection -> {

      final Promise<JsonObject> promise = Promise.promise();
      connection.updateWithParams(SQLStatements.SQL_INSERT, new JsonArray()
          .add(object.getString("name"))
          .add(object.getString("email"))
          .add(object.getString("password"))
        , result -> {
          if (result.failed()) {
            promise.fail(result.cause());
            connection.close();
            return;
          }
          promise.complete(result.result().toJson());
          connection.close();
        });
      return promise.future();
    });
  }

  @Override
  public Future<SQLConnection> connection() {
    final Promise<SQLConnection> promise = Promise.promise();
    this.client.getConnection(connectionAR -> {
      if (connectionAR.failed()) {
        promise.fail(connectionAR.cause());
        return;
      }
      promise.complete(connectionAR.result());
    });
    return promise.future();
  }
}
