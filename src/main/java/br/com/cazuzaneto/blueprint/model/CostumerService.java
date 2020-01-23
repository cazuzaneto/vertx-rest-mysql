package br.com.cazuzaneto.blueprint.model;

import br.com.cazuzaneto.blueprint.framework.mysql.CostumerRepository;
import io.vertx.core.Future;

import java.util.List;

public interface CostumerService {

  static CostumerService persist(final CostumerRepository repository) {
    return new CostumerServiceImpl(repository);
  }

  Future<List<Costumer>> finAll();

  Future<Costumer> finOne(Integer id);

  Future<Integer> persist(Costumer costumer);

  Future<Void> update(Costumer costumer);

  Future<Void> delete(Integer id);
}
