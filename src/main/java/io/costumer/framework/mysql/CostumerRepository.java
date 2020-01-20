package io.costumer.framework.mysql;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

public interface CostumerRepository {

  static CostumerRepository create(final Vertx vertx, final JsonObject config) {
    return new CostumerRepositoryImpl(vertx, config);
  }

  Future<List<JsonObject>> findAll();

  Future<SQLConnection> connection();

  Future<JsonObject> persist(JsonObject jsonObject);
}
