package br.com.cazuzaneto.blueprint.framework.mysql;

import br.com.cazuzaneto.blueprint.model.Costumer;
import br.com.cazuzaneto.blueprint.model.NotFoundException;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
  public Future<List<Costumer>> findAll() {
    return this.connection().compose(connection -> {
      final Promise<List<Costumer>> promise = Promise.promise();
      connection.query(SQLStatements.SQL_QUERY_ALL, resultSetAsyncResult -> {
        if (resultSetAsyncResult.failed()) {
          promise.fail(resultSetAsyncResult.cause());
          connection.close();
          return;
        }
        List<JsonObject> rows = resultSetAsyncResult.result().getRows();
        if (rows == null) {
          promise.complete(Collections.emptyList());
          return;
        }
        try {
          List<Costumer> costumers = rows.stream().map(Costumer::new).collect(Collectors.toList());
          connection.close();
          promise.complete(costumers);
        } catch (Exception e) {
          connection.close();
          promise.fail(e);
        }
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

  @Override
  public Future<JsonObject> findOne(String id) {
    return this.connection().compose(connection -> {
      final Promise<JsonObject> promise = Promise.promise();
      connection.queryWithParams(SQLStatements.SQL_QUERY, new JsonArray().add(id),
        result -> {
          try {
            if (result.failed()) {
              connection.close();
              promise.fail(result.cause());
              return;
            }
            if (result.result().getRows() == null || result.result().getRows().isEmpty()) {
              promise.fail(new NotFoundException(String.format("Not found any Costumer with id %s", id)));
              return;
            }
            promise.complete(result.result().getRows().get(0));
            connection.close();
          } catch (Exception e) {
            promise.fail(e);
          }
        }
      );
      return promise.future();
    });
  }

}
