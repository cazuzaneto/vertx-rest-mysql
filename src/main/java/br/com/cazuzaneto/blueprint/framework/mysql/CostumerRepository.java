package br.com.cazuzaneto.blueprint.framework.mysql;

import br.com.cazuzaneto.blueprint.model.Costumer;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface CostumerRepository {

  static CostumerRepository create(final Vertx vertx, final JsonObject config) {
    return new CostumerRepositoryImpl(vertx, config);
  }

  Future<List<Costumer>> findAll();

  Future<Integer> persist(Costumer costumer);

  Future<Costumer> findOne(Integer id);

  Future<Void> update(Costumer costumer);

  Future<Void> delete(Integer id);
}
