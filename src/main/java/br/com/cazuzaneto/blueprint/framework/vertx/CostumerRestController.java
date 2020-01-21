package br.com.cazuzaneto.blueprint.framework.vertx;

import br.com.cazuzaneto.blueprint.model.CostumerService;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class CostumerRestController extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(CostumerRestController.class);
  private static final String NOT_TODAY = "Sorry! Not today";
  private static final String HTTP_PORT = "http.port";
  public static final String COSTUMER_PATH = "/costumer/";
  public static final String ROOT_PATH = "/*";
  private final CostumerService service;

  public CostumerRestController(final CostumerService service) {
    this.service = service;
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
    router.get(COSTUMER_PATH).handler(this::findAllCostumers);
    router.post(COSTUMER_PATH).handler(this::createCostumer);
    router.route(ROOT_PATH).failureHandler(this::failureHandler);
    return router;
  }

  private void failureHandler(final RoutingContext context) {
    logger.error("Internal error on request -> [" + context.request().method() + "] " + context.request().uri());
    final HttpServerResponse response = context.response();
    response.setStatusCode(503).end(NOT_TODAY);
  }

  private void putHeaderDefault(HttpServerResponse response) {
    response.putHeader("content-type", "application/json");
  }

  private void createCostumer(final RoutingContext context) {
    context.request().bodyHandler(buffer -> {
        try {
          this.service.create(buffer.toJsonObject())
            .setHandler(handlerAllCostumers -> {
              putHeaderDefault(context.response());
              if (handlerAllCostumers.failed()) {
                context.fail(handlerAllCostumers.cause());
                return;
              }
              final JsonObject result = handlerAllCostumers.result();
              String key = result.getJsonArray("keys").getInteger(0).toString();
              final String location = context.request().absoluteURI() + key;
              context.response()
                .setStatusCode(HttpResponseStatus.CREATED.code())
                .putHeader(HttpHeaderNames.LOCATION.toString(), location)
                .end();
            });
        } catch (final Exception e) {
          context.fail(e);
        }
      }
    );
  }

  private void findAllCostumers(final RoutingContext context) {
    try {
      this.service.finAll().setHandler(handlerAllCostumers -> {
        putHeaderDefault(context.response());
        if (handlerAllCostumers.failed()) {
          context.fail(handlerAllCostumers.cause());
          return;
        }
        final String response = Json.encode(handlerAllCostumers.result());
        context.response()
          .setStatusCode(HttpResponseStatus.OK.code())
          .end(response);
      });
    } catch (final Exception e) {
      context.fail(e);
    }
  }


}
