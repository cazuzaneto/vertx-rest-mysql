package br.com.cazuzaneto.vertx.model;

import br.com.cazuzaneto.vertx.framework.mysql.CostumerRepository;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author Cazuza Neto
 */


public interface CostumerService {

  static CostumerService persist(final CostumerRepository repository) {
    return new CostumerServiceImpl(repository);
  }

  Future<List<Costumer>> findAll();

  Future<Costumer> findOne(Integer id);

  Future<Integer> persist(Costumer costumer);

  Future<Void> update(Costumer costumer);

  Future<Void> delete(Integer id);
}
