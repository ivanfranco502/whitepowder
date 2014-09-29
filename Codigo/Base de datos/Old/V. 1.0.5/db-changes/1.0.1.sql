ALTER TABLE `message` 
DROP FOREIGN KEY `FK_message_message_type`;
ALTER TABLE `message` 
DROP COLUMN `mess_message_type_id`,
DROP INDEX `FK_message_message_type_idx` ;

ALTER TABLE `message_type` 
CHANGE COLUMN `mety_id` `noty_id` INT(11) NOT NULL ,
CHANGE COLUMN `mety_description` `noty_description` VARCHAR(45) NOT NULL , RENAME TO  `notification_type` ;

ALTER TABLE `notification` 
ADD COLUMN `noti_notification_type_id` INT(11) NOT NULL AFTER `noti_description`,
ADD INDEX `FK_notification_notification_type_idx` (`noti_notification_type_id` ASC);
ALTER TABLE `notification` 
ADD CONSTRAINT `FK_notification_notification_type`
  FOREIGN KEY (`noti_notification_type_id`)
  REFERENCES `notification_type` (`noty_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;