package br.com.cazuzaneto.blueprint.application;

import br.com.cazuzaneto.blueprint.model.Costumer;
import br.com.cazuzaneto.blueprint.model.CostumerService;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class CostumerController {

  private final CostumerService service;

  public CostumerController(final CostumerService service) {
    this.service = service;
  }

  public Future<List<JsonObject>> getAll() {
    return this.service.finAll()
      .compose(list -> Future.succeededFuture(list.stream()
        .map(Costumer::toJson)
        .collect(Collectors.toList())));
  }

  public Future<JsonObject> finOne(final String id) {
    return this.service.finOne(Integer.valueOf(id))
      .compose(costumer -> Future.succeededFuture(costumer.toJson()));
  }

  public Future<Integer> persist(final JsonObject value) {
    final Costumer costumer = this.fromJson(value);
    return this.service.persist(costumer);
  }

  public Future<Void> update(final JsonObject value, final String id) {
    final Costumer costumer = this.fromJson(value);
    costumer.setId(Integer.valueOf(id));
    return this.service.update(costumer);
  }

  private Costumer fromJson(final JsonObject value) {
    return Json.decodeValue(value.toBuffer(), Costumer.class);
  }

  public Future<Void> delete(final String id) {
    return this.service.delete(Integer.valueOf(id));
  }
}
