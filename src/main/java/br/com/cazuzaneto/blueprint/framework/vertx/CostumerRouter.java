package br.com.cazuzaneto.blueprint.framework.vertx;

import br.com.cazuzaneto.blueprint.model.CostumerService;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class CostumerRouter {
  private static final String PARAM_ID = "id";
  private static final String KEYS_PARAM = "keys";
  private final CostumerService service;

  public CostumerRouter(CostumerService service) {
    this.service = service;
  }

  void createCostumer(final RoutingContext context) {
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

  void findOneCostumer(RoutingContext context) {
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

  void findAllCostumers(final RoutingContext context) {
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

  private void putHeaderDefault(HttpServerResponse response) {
    response.putHeader("content-type", "application/json");
  }

}
