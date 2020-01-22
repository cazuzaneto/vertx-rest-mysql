package br.com.cazuzaneto.blueprint.framework.vertx;

import br.com.cazuzaneto.blueprint.model.CostumerService;
import br.com.cazuzaneto.blueprint.model.NotFoundException;
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
  public static final String COSTUMER_ID_PATH = "/costumer/:id";
  public static final String ROOT_PATH = "/*";
  private static final String FAILURE_MESSAGE = "Internal error on request | [{0} {1}] -> Error: {2}";
  public static final String PARAM_ID = "id";
  public static final String KEYS_PARAM = "keys";
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
    router.get(COSTUMER_ID_PATH).handler(this::findOneCostumer);
    router.post(COSTUMER_PATH).handler(this::createCostumer);
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

  private void putHeaderDefault(HttpServerResponse response) {
    response.putHeader("content-type", "application/json");
  }

  private void createCostumer(final RoutingContext context) {
    context.request().bodyHandler(buffer -> {
        try {
          this.service.persist(buffer.toJsonObject())
            .setHandler(handlerAllCostumers -> {
              putHeaderDefault(context.response());
              if (handlerAllCostumers.failed()) {
                context.fail(handlerAllCostumers.cause());
                return;
              }
              final JsonObject result = handlerAllCostumers.result();
              String key = result.getJsonArray(KEYS_PARAM).getInteger(0).toString();
              final String location = context.request().uri() + key;
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

  private void findOneCostumer(RoutingContext context) {
    try {
      String id = context.request().getParam(PARAM_ID);
      service.finOne(id).setHandler(requestResult -> {
        HttpServerResponse response = context.response();
        putHeaderDefault(response);
        if (requestResult.failed()) {
          context.fail(requestResult.cause());
          return;
        }
        final String body = Json.encode(requestResult.result());
        response
          .setStatusCode(HttpResponseStatus.OK.code())
          .end(body);
      });
    } catch (Exception e) {
      context.fail(e);
    }
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
