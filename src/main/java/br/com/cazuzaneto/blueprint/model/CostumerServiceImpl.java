package br.com.cazuzaneto.blueprint.model;

import br.com.cazuzaneto.blueprint.framework.mysql.CostumerRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class CostumerServiceImpl implements CostumerService {

  private final CostumerRepository repository;

  public CostumerServiceImpl(final CostumerRepository repository) {
    this.repository = repository;
  }

  @Override
  public Future<List<Costumer>> finAll() {
    return this.repository.findAll();
  }

  @Override
  public Future<JsonObject> persist(final JsonObject costumer) {
    return this.repository.persist(costumer);
  }

  @Override
  public Future<JsonObject> finOne(String id) {
    return this.repository.findOne(id);
  }
}
