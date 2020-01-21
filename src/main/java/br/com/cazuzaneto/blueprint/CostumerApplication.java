package br.com.cazuzaneto.blueprint;

import br.com.cazuzaneto.blueprint.framework.mysql.CostumerRepository;
import br.com.cazuzaneto.blueprint.framework.vertx.CostumerRestController;
import br.com.cazuzaneto.blueprint.framework.vertx.Config;
import br.com.cazuzaneto.blueprint.model.CostumerService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.flywaydb.core.Flyway;

public class CostumerApplication {
  private static final Logger logger = LoggerFactory.getLogger(CostumerApplication.class);
  private static final String APPLICATION_SUCCESS_INIT = "Application success init";

  public static void main(final String[] args) {
    final Vertx vertx = Vertx.vertx();
    Config.createConfig(vertx).setHandler(asyncResult -> {
      if (asyncResult.failed()) {
        logger.error("Error init!");
        return;
      }
      final JsonObject config = asyncResult.result();
      initFlyway(config.getString("url"), config.getString("user"), config.getString("password"));
      final CostumerRepository repository = CostumerRepository.create(vertx, config);
      final CostumerService service = CostumerService.create(repository);
      final DeploymentOptions options = new DeploymentOptions().setConfig(config);
      vertx.deployVerticle(new CostumerRestController(service), options);
      logger.info(APPLICATION_SUCCESS_INIT);
    });
  }

  public static void initFlyway(final String url, final String user, final String pass) {
    final Flyway flyway = Flyway.configure().dataSource(url, user, pass).load();
    flyway.migrate();
  }
}
