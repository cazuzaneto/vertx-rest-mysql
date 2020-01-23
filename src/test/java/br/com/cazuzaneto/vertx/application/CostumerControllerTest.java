package br.com.cazuzaneto.vertx.application;

import br.com.cazuzaneto.vertx.framework.vertx.Config;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(VertxUnitRunner.class)
@Ignore
public class CostumerControllerTest {

  private Vertx vertx;
  private int port;

  @Before
  public void setUp(final TestContext context) throws IOException {
    this.vertx = Vertx.vertx();
    Config.createConfig(this.vertx).setHandler(context.asyncAssertSuccess(han -> {
      this.port = han.getInteger("http.port.test");
      final DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", this.port));
    }));
  }

  @After
  public void tearDown(final TestContext context) {
    this.vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testMyApplication(final TestContext context) {
    final Async async = context.async();
    this.vertx.createHttpClient().getNow(this.port, "localhost", "/", response -> response.handler(body -> {
      context.assertTrue(body.toString().contains("Hello"));
      async.complete();
    }));
  }
}
