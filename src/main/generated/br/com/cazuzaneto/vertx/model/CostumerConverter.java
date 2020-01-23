package br.com.cazuzaneto.vertx.model;

import io.vertx.core.json.JsonObject;

/**
 * Converter for {@link br.com.cazuzaneto.vertx.model.Costumer}.
 * NOTE: This class has been automatically generated from the {@link br.com.cazuzaneto.vertx.model.Costumer} original class using Vert.x codegen.
 */
public class CostumerConverter {

  public static void fromJson(final Iterable<java.util.Map.Entry<String, Object>> json, final Costumer obj) {
    for (final java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "email":
          if (member.getValue() instanceof String) {
            obj.setEmail((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).longValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "password":
          if (member.getValue() instanceof String) {
            obj.setPassword((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(final Costumer obj, final JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(final Costumer obj, final java.util.Map<String, Object> json) {
    if (obj.getEmail() != null) {
      json.put("email", obj.getEmail());
    }
    json.put("id", obj.getId());
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
  }
}
