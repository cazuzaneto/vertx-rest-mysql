package br.com.cazuzaneto.blueprint.model;

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
