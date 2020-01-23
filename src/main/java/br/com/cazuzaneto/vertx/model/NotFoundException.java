package br.com.cazuzaneto.vertx.model;

/**
 * @author Cazuza Neto
 */

public class NotFoundException extends Exception {

  public NotFoundException(final String message) {
    super(message);
  }

  public NotFoundException() {
  }
}
