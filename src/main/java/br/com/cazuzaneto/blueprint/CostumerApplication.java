package br.com.cazuzaneto.blueprint;

import br.com.cazuzaneto.blueprint.application.CostumerController;
import br.com.cazuzaneto.blueprint.framework.mysql.CostumerRepository;
import br.com.cazuzaneto.blueprint.framework.vertx.ApplicationServer;
import br.com.cazuzaneto.blueprint.framework.vertx.Config;
import br.com.cazuzaneto.blueprint.framework.vertx.CostumerRouter;
import br.com.cazuzaneto.blueprint.framework.vertx.FailureHandler;
import br.com.cazuzaneto.blueprint.framework.vertx.ParamsConfig;
import br.com.cazuzaneto.blueprint.model.CostumerService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.flywaydb.core.Flyway;

public class CostumerApplication {
  private static final Logger logger = LoggerFactory.getLogger(CostumerApplication.class);
  private static final String APPLICATION_SUCCESS_INIT = "Application Started";

  public static void main(final String[] args) {
    final Vertx vertx = Vertx.vertx();
    Config.createConfig(vertx).setHandler(asyncResult -> {
      if (asyncResult.failed()) {
        logger.error("Init Error");
        return;
      }
      final JsonObject config = asyncResult.result();
      initFlyway(config.getString(ParamsConfig.MYSQL_URL.label()), config.getString(ParamsConfig.MYSQL_USER.label()), config.getString(ParamsConfig.MYSQL_PASSWORD.label()));
      final CostumerRepository repository = CostumerRepository.create(vertx, config);
      final CostumerService service = CostumerService.persist(repository);
      final DeploymentOptions options = new DeploymentOptions().setConfig(config);
      final CostumerController controller = new CostumerController(service);
      final CostumerRouter router = new CostumerRouter(controller);
      vertx.deployVerticle(new ApplicationServer(router, new FailureHandler()), options);
      logger.info(APPLICATION_SUCCESS_INIT);
    });
  }

  public static void initFlyway(final String url, final String user, final String pass) {
    final Flyway flyway = Flyway.configure().dataSource(url, user, pass).load();
    flyway.migrate();
  }
}
