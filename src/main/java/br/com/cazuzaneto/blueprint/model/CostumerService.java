package br.com.cazuzaneto.blueprint.model;

import br.com.cazuzaneto.blueprint.framework.mysql.CostumerRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface CostumerService {

  static CostumerService persist(final CostumerRepository repository) {
    return new CostumerServiceImpl(repository);
  }

  Future<List<JsonObject>> finAll();

  Future<JsonObject> finOne(String id);

  Future<JsonObject> persist(JsonObject costumer);
}
