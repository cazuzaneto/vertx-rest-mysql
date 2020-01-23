package br.com.cazuzaneto.vertx.framework.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

/**
 * @author Cazuza Neto
 */

public class ApplicationServer extends AbstractVerticle {
  private static final String HTTP_PORT = "http.port";
  private static final String COSTUMER_PATH = "/costumer/";
  private static final String COSTUMER_ID_PATH = "/costumer/:id";
  private static final String ROOT_PATH = "/*";
  private final CostumerRouter costumerRouter;
  private final FailureHandler failureHandler;

  public ApplicationServer(final CostumerRouter costumerRouter, final FailureHandler failureHandler) {
    this.costumerRouter = costumerRouter;
    this.failureHandler = failureHandler;
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    this.initServer();
    super.start(startPromise);
  }

  private void initServer() {
    final Integer port = this.config().getInteger(ParamsConfig.HTTP_PORT.label());
    this.vertx.createHttpServer()
      .requestHandler(this.createRouter())
      .listen(port);
  }

  private Router createRouter() {
    final Router router = Router.router(this.vertx);
    router.get(COSTUMER_PATH).handler(this.costumerRouter::findAllCostumers);
    router.get(COSTUMER_ID_PATH).handler(this.costumerRouter::findOneCostumer);
    router.post(COSTUMER_PATH).handler(this.costumerRouter::createCostumer);
    router.put(COSTUMER_ID_PATH).handler(this.costumerRouter::updateCostumer);
    router.delete(COSTUMER_ID_PATH).handler(this.costumerRouter::deleteCostumer);
    router.route(ROOT_PATH).failureHandler(this.failureHandler::handle);
    return router;
  }
}
