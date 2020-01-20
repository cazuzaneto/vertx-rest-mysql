package io.costumer.model;

import io.costumer.framework.mysql.CostumerRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface CostumerService {

  static CostumerService create(final CostumerRepository repository) {
    return new CostumerServiceImpl(repository);
  }

  Future<List<JsonObject>> finAll();

  Future<JsonObject> create(JsonObject costumer);
}
