package br.com.cazuzaneto.blueprint.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject(generateConverter = true)
public class Costumer {
  private long id;
  private String name;
  private String email;
  private String password;

  public Costumer(final JsonObject costumer) {
    CostumerConverter.fromJson(costumer, this);
  }

  public Costumer(long id, String name, String email, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "Costumer{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", email='" + email + '\'' +
      ", password='" + password + '\'' +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Costumer costumer = (Costumer) o;
    return id == costumer.id &&
      Objects.equals(name, costumer.name) &&
      Objects.equals(email, costumer.email) &&
      Objects.equals(password, costumer.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, email, password);
  }
}
