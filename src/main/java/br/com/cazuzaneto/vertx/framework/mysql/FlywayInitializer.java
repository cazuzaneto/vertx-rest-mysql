package br.com.cazuzaneto.vertx.framework.mysql;

import br.com.cazuzaneto.vertx.framework.vertx.ParamsConfig;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.flywaydb.core.Flyway;

/**
 * @author cazuza
 */

public class FlywayInitializer {

  private final Vertx vertx;

  public FlywayInitializer(final Vertx vertx) {
    this.vertx = vertx;
  }

  public Future<Void> init(final JsonObject config) {
    final String url = config.getString(ParamsConfig.MYSQL_URL.label());
    final String user = config.getString(ParamsConfig.MYSQL_USER.label());
    final String pass = config.getString(ParamsConfig.MYSQL_PASSWORD.label());

    final Promise<Void> promise = Promise.promise();
    this.vertx.executeBlocking(fut -> {
      try {
        Flyway.configure().dataSource(url, user, pass).load().migrate();
        fut.complete();
      } catch (final Exception e) {
        fut.fail(e);
      }
    }, ret -> {
      if (ret.failed()) {
        promise.fail(ret.cause());
        return;
      }
      promise.complete();
    });
    return promise.future();
  }
}
