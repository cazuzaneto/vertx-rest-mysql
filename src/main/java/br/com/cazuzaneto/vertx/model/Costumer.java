package br.com.cazuzaneto.vertx.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject(generateConverter = true)
public class Costumer {
  @JsonProperty(required = false, value = "id")
  private long id;
  @JsonProperty(required = true, value = "name")
  private String name;
  @JsonProperty(required = true, value = "email")
  private String email;
  @JsonProperty(required = true, value = "password")
  private String password;

  public Costumer(final JsonObject costumer) {
    CostumerConverter.fromJson(costumer, this);
  }

  public Costumer(final String name, final String email, final String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  @JsonCreator
  public Costumer(@JsonProperty("id") final long id,
                  @JsonProperty("name") final String name,
                  @JsonProperty("email") final String email,
                  @JsonProperty("password") final String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public JsonObject toJson() {
    final JsonObject json = new JsonObject();
    CostumerConverter.toJson(this, json);
    return json;
  }

  public long getId() {
    return this.id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "Costumer{" +
      "id=" + this.id +
      ", name='" + this.name + '\'' +
      ", email='" + this.email + '\'' +
      ", password='" + this.password + '\'' +
      '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final Costumer costumer = (Costumer) o;
    return this.id == costumer.id &&
      Objects.equals(this.name, costumer.name) &&
      Objects.equals(this.email, costumer.email) &&
      Objects.equals(this.password, costumer.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.email, this.password);
  }
}
