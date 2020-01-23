package br.com.cazuzaneto.blueprint.model;

import br.com.cazuzaneto.blueprint.framework.mysql.CostumerRepository;
import io.vertx.core.Future;

import java.util.List;

public class CostumerServiceImpl implements CostumerService {

  private final CostumerRepository repository;

  public CostumerServiceImpl(final CostumerRepository repository) {
    this.repository = repository;
  }

  @Override
  public Future<List<Costumer>> findAll() {
    return this.repository.findAll();
  }

  @Override
  public Future<Integer> persist(final Costumer costumer) {
    return this.repository.persist(costumer);
  }

  @Override
  public Future<Costumer> findOne(final Integer id) {
    return this.repository.findOne(id);
  }

  @Override
  public Future<Void> update(final Costumer costumer) {
    return this.repository.update(costumer);
  }

  @Override
  public Future<Void> delete(final Integer id) {
    return this.repository.delete(id);
  }
}
