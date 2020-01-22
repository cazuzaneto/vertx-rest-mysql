package br.com.cazuzaneto.blueprint.framework.vertx;

import br.com.cazuzaneto.blueprint.model.NotFoundException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Server extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(Server.class);
  private static final String NOT_TODAY = "Sorry! Not today";
  private static final String HTTP_PORT = "http.port";
  private static final String COSTUMER_PATH = "/costumer/";
  private static final String COSTUMER_ID_PATH = "/costumer/:id";
  private static final String ROOT_PATH = "/*";
  private static final String FAILURE_MESSAGE = "Internal error on request | [{0} {1}] -> Error: {2}";
  private final CostumerRouter costumerRouter;

  public Server(final CostumerRouter costumerRouter) {
    this.costumerRouter = costumerRouter;
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    initServer();
    super.start(startPromise);
  }

  private void initServer() {
    Integer port = this.config().getInteger(HTTP_PORT);
    this.vertx.createHttpServer()
      .requestHandler(createRouter())
      .listen(port);
  }

  private Router createRouter() {
    final Router router = Router.router(this.vertx);
    router.get(COSTUMER_PATH).handler(costumerRouter::findAllCostumers);
    router.get(COSTUMER_ID_PATH).handler(costumerRouter::findOneCostumer);
    router.post(COSTUMER_PATH).handler(costumerRouter::createCostumer);
    router.route(ROOT_PATH).failureHandler(this::failureHandler);
    return router;
  }

  private void failureHandler(final RoutingContext context) {
    logger.error(FAILURE_MESSAGE, context.request().method(), context.request().uri(), context.failure().getMessage());
    final HttpServerResponse response = context.response();
    if (context.failure() instanceof NotFoundException) {
      response.setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
      return;
    }
    response.setStatusCode(500).end(NOT_TODAY);
  }
}
