package br.com.cazuzaneto.blueprint.framework.vertx;

import br.com.cazuzaneto.blueprint.application.CostumerController;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class CostumerRouter {
  private static final String PARAM_ID = "id";
  private final CostumerController controller;

  public CostumerRouter(final CostumerController controller) {
    this.controller = controller;
  }

  void createCostumer(final RoutingContext context) {
    context.request().bodyHandler(buffer -> {
        try {
          this.controller.persist(buffer.toJsonObject())
            .setHandler(handlerAllCostumers -> {
              this.putHeaderDefault(context.response());
              if (handlerAllCostumers.failed()) {
                context.fail(handlerAllCostumers.cause());
                return;
              }
              final Integer key = handlerAllCostumers.result();
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

  void findOneCostumer(final RoutingContext context) {
    try {
      final String id = context.request().getParam(PARAM_ID);
      this.controller.finOne(id).setHandler(requestResult -> {
        final HttpServerResponse response = context.response();
        this.putHeaderDefault(response);
        if (requestResult.failed()) {
          context.fail(requestResult.cause());
          return;
        }
        final String body = Json.encode(requestResult.result());
        response
          .setStatusCode(HttpResponseStatus.OK.code())
          .end(body);
      });
    } catch (final Exception e) {
      context.fail(e);
    }
  }

  void findAllCostumers(final RoutingContext context) {
    try {
      this.controller.getAll().setHandler(handlerAllCostumers -> {
        this.putHeaderDefault(context.response());
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

  private void putHeaderDefault(final HttpServerResponse response) {
    response.putHeader("content-type", "application/json");
  }

  void updateCostumer(final RoutingContext context) {
    context.request().bodyHandler(buffer -> {
      try {
        this.controller.update(buffer.toJsonObject(), context.pathParam(PARAM_ID)).setHandler(handlerAllCostumers -> {
          this.putHeaderDefault(context.response());
          if (handlerAllCostumers.failed()) {
            context.fail(handlerAllCostumers.cause());
            return;
          }
          context.response()
            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
            .end();
        });
      } catch (final Exception e) {
        context.fail(e);
      }
    });
  }

  void deleteCostumer(final RoutingContext context) {
    try {
      this.controller.delete(context.pathParam(PARAM_ID)).setHandler(handlerAllCostumers -> {
        this.putHeaderDefault(context.response());
        if (handlerAllCostumers.failed()) {
          context.fail(handlerAllCostumers.cause());
          return;
        }
        context.response()
          .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
          .end();
      });
    } catch (final Exception e) {
      context.fail(e);
    }
  }
}
