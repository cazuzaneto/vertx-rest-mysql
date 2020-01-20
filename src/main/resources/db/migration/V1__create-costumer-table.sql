create TABLE IF NOT EXISTS `costumer`(
     `id`       INT(11) NOT NULL auto_increment,
     `name`     VARCHAR(255) DEFAULT NULL,
     `email`    VARCHAR(255) DEFAULT NULL,
     `password` VARCHAR(255) DEFAULT NULL,
     PRIMARY KEY (id)
) ENGINE = InnoDB;
