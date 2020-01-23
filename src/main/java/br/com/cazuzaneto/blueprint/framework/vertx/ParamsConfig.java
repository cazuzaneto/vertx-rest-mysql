package br.com.cazuzaneto.blueprint.framework.vertx;

public enum ParamsConfig {

  HTTP_PORT("http.port"),
  MYSQL_PASSWORD("password"),
  MYSQL_USER("user"),
  MYSQL_URL("url");

  private final String label;

  ParamsConfig(final String label) {
    this.label = label;
  }

  public String label() {
    return this.label;
  }
}
