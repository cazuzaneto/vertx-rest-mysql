package br.com.cazuzaneto.blueprint.application;

import br.com.cazuzaneto.blueprint.model.CostumerService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class CostumerController {

  private final CostumerService service;

  public CostumerController(CostumerService service) {
    this.service = service;
  }

  private Future<List<JsonObject>> getAll() {
    return null;
  }
}
