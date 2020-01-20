package io.costumer.framework.vertx;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Config {
  public static Future<JsonObject> createConfig(final Vertx vertx) {
    final Promise<JsonObject> promise = Promise.promise();
    final ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions()
      .addStore(createOptions()));
    retriever.getConfig(handler -> {
      if (handler.failed()) {
        promise.fail(handler.cause());
        return;
      }
      promise.complete(handler.result());
    });
    return promise.future();
  }

  private static ConfigStoreOptions createOptions() {
    return new ConfigStoreOptions()
      .setType("file")
      .setFormat("properties")
      .setConfig(new JsonObject()
        .put("path", "application.properties"));
  }
}
