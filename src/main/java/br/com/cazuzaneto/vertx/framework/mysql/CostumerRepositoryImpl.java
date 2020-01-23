package br.com.cazuzaneto.vertx.framework.mysql;

import br.com.cazuzaneto.vertx.model.Costumer;
import br.com.cazuzaneto.vertx.model.NotFoundException;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cazuza Neto
 */

class CostumerRepositoryImpl implements CostumerRepository {

  private static final String NOT_FOUNDED = "Resource not founded";
  private static final String NOT_FOUND_WITH_ID = "Not found any Costumer with id %s";
  private static final int FIRST_INDEX = 0;
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
        final List<JsonObject> rows = resultSetAsyncResult.result().getRows();
        if (rows == null) {
          promise.complete(Collections.emptyList());
          return;
        }
        try {
          final List<Costumer> costumers = rows.stream().map(Costumer::new).collect(Collectors.toList());
          connection.close();
          promise.complete(costumers);
        } catch (final Exception e) {
          connection.close();
          promise.fail(e);
        }
      });
      return promise.future();
    });
  }

  @Override
  public Future<Integer> persist(final Costumer costumer) {
    return this.connection().compose(connection -> {
      final Promise<Integer> promise = Promise.promise();
      connection.updateWithParams(SQLStatements.SQL_INSERT, new JsonArray()
          .add(costumer.getName())
          .add(costumer.getEmail())
          .add(costumer.getPassword())
        , result -> {
          try {
            if (result.failed()) {
              connection.close();
              promise.fail(result.cause());
              return;
            }
            final UpdateResult result1 = result.result();
            final Integer json = result1.getKeys().getInteger(0);
            connection.close();
            promise.complete(json);

          } catch (final Exception e) {
            connection.close();
            promise.fail(e);
          }
        });
      return promise.future();
    });
  }


  private Future<SQLConnection> connection() {
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
  public Future<Costumer> findOne(final Integer id) {
    return this.connection().compose(connection -> {
      final Promise<Costumer> promise = Promise.promise();
      connection.queryWithParams(SQLStatements.SQL_QUERY, new JsonArray().add(id),
        result -> {
          try {
            if (result.failed()) {
              connection.close();
              promise.fail(result.cause());
              return;
            }
            if (result.result().getRows() == null || result.result().getRows().isEmpty()) {
              connection.close();
              promise.fail(new NotFoundException(String.format(NOT_FOUND_WITH_ID, id)));
              return;
            }
            final JsonObject result1 = result.result().getRows().get(FIRST_INDEX);
            connection.close();
            promise.complete(new Costumer(result1));
          } catch (final Exception e) {
            connection.close();
            promise.fail(e);
          }
        }
      );
      return promise.future();
    });
  }

  @Override
  public Future<Void> update(final Costumer costumer) {
    return this.connection().compose(connection -> {
      final Promise<Void> promise = Promise.promise();
      connection.updateWithParams(SQLStatements.SQL_UPDATE, new JsonArray()
        .add(costumer.getName())
        .add(costumer.getEmail())
        .add(costumer.getPassword())
        .add(costumer.getId()), result -> {
        try {
          if (result.failed()) {
            connection.close();
            promise.fail(result.cause());
            return;
          }
          if (result.result().getUpdated() == 0) {
            connection.close();
            promise.fail(new NotFoundException(NOT_FOUNDED));
            return;
          }
          connection.close();
          promise.complete();
        } catch (final Exception e) {
          connection.close();
          promise.fail(e);
        }
      });
      return promise.future();
    });
  }

  @Override
  public Future<Void> delete(final Integer id) {
    return this.connection().compose(connection -> {
      final Promise<Void> promise = Promise.promise();
      connection.updateWithParams(SQLStatements.SQL_DELETE, new JsonArray().add(id), result -> {
        try {
          if (result.failed()) {
            connection.close();
            promise.fail(result.cause());
            return;
          }
          if (result.result().getUpdated() == 0) {
            connection.close();
            promise.fail(new NotFoundException("Resource not founded"));
            return;
          }
          connection.close();
          promise.complete();
        } catch (final Exception e) {
          connection.close();
          promise.fail(e);
        }
      });
      return promise.future();
    });
  }
}
