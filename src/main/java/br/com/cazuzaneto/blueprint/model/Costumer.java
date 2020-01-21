package br.com.cazuzaneto.blueprint.model;

import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Costumer {
  private long id;
  private String name;
  private String email;
  private String password;

  public Costumer(final JsonObject costumer) {
    this.id = costumer.getInteger("id");
    this.name = costumer.getString("name");
    this.email = costumer.getString("email");
    this.password = costumer.getString("password");
  }
}
