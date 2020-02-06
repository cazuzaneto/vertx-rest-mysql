package br.com.cazuzaneto.vertx;

import br.com.cazuzaneto.vertx.application.CostumerController;
import br.com.cazuzaneto.vertx.framework.mysql.CostumerRepository;
import br.com.cazuzaneto.vertx.framework.mysql.FlywayInitializer;
import br.com.cazuzaneto.vertx.framework.vertx.ApplicationServer;
import br.com.cazuzaneto.vertx.framework.vertx.Config;
import br.com.cazuzaneto.vertx.framework.vertx.CostumerRouter;
import br.com.cazuzaneto.vertx.framework.vertx.FailureHandler;
import br.com.cazuzaneto.vertx.model.CostumerService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Cazuza Neto
 */

public class CostumerApplication {
  private static final Logger logger = LoggerFactory.getLogger(CostumerApplication.class);
  private static final String APPLICATION_SUCCESS_INIT = "Application Started";

  public static void main(final String[] args) {
    final Vertx vertx = Vertx.vertx();
    Config.createConfig(vertx)
      .compose(config -> {
        try {
          return new FlywayInitializer(vertx).init(config).compose($ -> createApplicationServer(vertx, config));
        } catch (Exception e) {
          return Future.failedFuture(e);
        }
      })
      .setHandler(res -> {
        if (res.failed()) {
          logger.error("Init Error");
          return;
        }
        logger.info(APPLICATION_SUCCESS_INIT);
      });
  }

  private static Future<Void> createApplicationServer(final Vertx vertx, final JsonObject config) {
    try {
      final CostumerRepository repository = CostumerRepository.create(vertx, config);
      final CostumerService service = CostumerService.persist(repository);
      final DeploymentOptions options = new DeploymentOptions().setConfig(config);
      final CostumerController controller = new CostumerController(service);
      final CostumerRouter router = new CostumerRouter(controller);
      final ApplicationServer applicationServer = new ApplicationServer(router, new FailureHandler());
      return deployVerticle(vertx, applicationServer, options);
    } catch (final Exception e) {
      return Future.failedFuture(e);
    }
  }

  private static Future<Void> deployVerticle(final Vertx vertx, final AbstractVerticle verticle, final DeploymentOptions options) {
    final Promise<Void> promise = Promise.promise();
    vertx.deployVerticle(verticle, options, event -> {
      if (event.failed()) {
        promise.fail(event.cause());
        return;
      }
      promise.complete();
    });
    return promise.future();
  }

}
