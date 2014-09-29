/*ADDING EXTERNAL DATA TO USER*/
ALTER TABLE `whitepowder`.`token` 
DROP FOREIGN KEY `fk_user_id`;

ALTER TABLE `whitepowder`.`token` 
CHANGE COLUMN `token_id` `exda_id` INT(11) NOT NULL ,
CHANGE COLUMN `token` `exda_token` VARCHAR(45) NULL ,
CHANGE COLUMN `token_user_id` `exda_user_id` INT(11) NOT NULL ,
ADD COLUMN `exda_registration_code` TEXT NULL AFTER `exda_token`, RENAME TO  `whitepowder`.`external_data` ;

ALTER TABLE `whitepowder`.`external_data` 
ADD CONSTRAINT `fk_exda_user`
  FOREIGN KEY (`exda_user_id`)
  REFERENCES `whitepowder`.`users` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

/*ADDING SKI MODE */  
ALTER TABLE `whitepowder`.`user_coordinate` 
ADD COLUMN `usco_ski_mode` TINYINT NOT NULL DEFAULT 0 AFTER `usco_update_date`;
  