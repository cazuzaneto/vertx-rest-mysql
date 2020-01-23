package br.com.cazuzaneto.vertx.framework.vertx;

import br.com.cazuzaneto.vertx.model.NotFoundException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Cazuza Neto
 */

public class FailureHandler implements Handler<RoutingContext> {
  private static final String FAILURE_MESSAGE = "Internal error on request | [{0} {1}] -> Error: {2}";
  private static final String NOT_TODAY = "Sorry! Not today";
  private static final Logger logger = LoggerFactory.getLogger(FailureHandler.class);

  @Override
  public void handle(final RoutingContext context) {
    logger.error(FAILURE_MESSAGE, context.request().method(), context.request().uri(), context.failure().getMessage());
    final HttpServerResponse response = context.response();

    if (context.failure() instanceof NotFoundException) {
      response.setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
      return;
    }

    if (context.failure() instanceof DecodeException) {
      response.setStatusCode(HttpResponseStatus.UNPROCESSABLE_ENTITY.code()).end();
      return;
    }

    response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(NOT_TODAY);
  }

}
