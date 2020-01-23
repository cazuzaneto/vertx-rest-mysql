package br.com.cazuzaneto.blueprint.framework.mysql;

/**
 * @author Cazuza Neto
 */

interface SQLStatements {
  String SQL_INSERT = "INSERT INTO `costumer` " +
    "(`name`, `email`, `password`) VALUES (?, ?, ?)";
  String SQL_QUERY = "SELECT * FROM costumer WHERE id = ?";
  String SQL_QUERY_ALL = "SELECT * FROM costumer";
  String SQL_UPDATE = "UPDATE `costumer`\n" +
    "SET\n" +
    "`name` = ?,\n" +
    "`email` = ?,\n" +
    "`password` = ?\n" +
    "WHERE `id` = ?;";
  String SQL_DELETE = "DELETE FROM `costumer` WHERE `id` = ?";
}
