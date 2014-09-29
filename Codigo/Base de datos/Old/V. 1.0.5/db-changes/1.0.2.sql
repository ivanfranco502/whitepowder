CREATE TABLE `whitepowder`.`color` (
  `colo_id` INT NOT NULL AUTO_INCREMENT,
  `colo_description` VARCHAR(45) NULL,
  `colo_hexa_code` VARCHAR(6) NULL,
  PRIMARY KEY (`colo_id`));

INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Blanco', 'FFFFFF');
INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Rojo', 'FF0000');
INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Naranja', 'FF6600');
INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Amarillo', 'FFFF00');
INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Verde', '008000');
INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Azul', '0000FF');
INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Violeta', '808000');
INSERT INTO `whitepowder`.`color` (`colo_description`, `colo_hexa_code`) VALUES ('Negro', '000000');

UPDATE `whitepowder`.`slope_dificulty` SET `sldi_color`='5' WHERE `sldi_id`='1';
UPDATE `whitepowder`.`slope_dificulty` SET `sldi_color`='6' WHERE `sldi_id`='2';
UPDATE `whitepowder`.`slope_dificulty` SET `sldi_color`='2' WHERE `sldi_id`='3';
UPDATE `whitepowder`.`slope_dificulty` SET `sldi_color`='8' WHERE `sldi_id`='4';

ALTER TABLE `whitepowder`.`slope_dificulty` 
CHANGE COLUMN `sldi_color` `sldi_color_id` INT(11) NOT NULL ;

ALTER TABLE `whitepowder`.`slope_dificulty` 
ADD INDEX `FK_slope_dificulty_color_idx` (`sldi_color_id` ASC);
ALTER TABLE `whitepowder`.`slope_dificulty` 
ADD CONSTRAINT `FK_slope_dificulty_color`
  FOREIGN KEY (`sldi_color_id`)
  REFERENCES `whitepowder`.`color` (`colo_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
