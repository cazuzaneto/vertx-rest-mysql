package io.costumer.framework.vertx;

import io.costumer.model.CostumerService;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
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
  private final CostumerService service;

  public CostumerRestController(final CostumerService service) {
    this.service = service;
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    final HttpServer server = this.vertx.createHttpServer();
    final Router router = Router.router(this.vertx);
    router.get("/costumer/").handler(this::getAll);
    router.post("/costumer/").handler(this::create).failureHandler(failureRoutingContext -> {
      logger.error("Error on request to [POST] -> /costumer/");
      final HttpServerResponse response = failureRoutingContext.response();
      response.setStatusCode(503).end(NOT_TODAY);
    });

    
    server.requestHandler(router).listen(this.config().getInteger("http.port"));
    super.start(startPromise);
  }

  private void create(final RoutingContext context) {
    context.request().bodyHandler(buffer -> {
        try {
          this.service.create(buffer.toJsonObject())
            .setHandler(handlerAllCostumers -> {
              context.response().putHeader("content-type", "application/json");
              if (handlerAllCostumers.failed()) {
                context.response()
                  .setStatusCode(HttpResponseStatus.BAD_GATEWAY.code())
                  .end(handlerAllCostumers.cause().getMessage());
                return;
              }
              final JsonObject result = handlerAllCostumers.result();
              final String location = "costumer/" + result.getJsonArray("keys").getInteger(0).toString();
              context.response()
                .setStatusCode(HttpResponseStatus.CREATED.code())
                .putHeader(HttpHeaderNames.LOCATION.toString(), location)
                .end(result.encode());
            });
        } catch (final Exception e) {
          context.fail(e);
        }
      }
    );
  }

  private void getAll(final RoutingContext context) {
    try {
      this.service.finAll().setHandler(handlerAllCostumers -> {
        context.response()
          .putHeader("content-type", "application/json");
        if (handlerAllCostumers.failed()) {
          context.response()
            .setStatusCode(HttpResponseStatus.BAD_GATEWAY.code())
            .end(handlerAllCostumers.cause().getMessage());
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
