ALTER TABLE `whitepowder`.`general_information` 
	ADD COLUMN `gein_x` DOUBLE NULL DEFAULT NULL AFTER `gein_details`,
	ADD COLUMN `gein_y` DOUBLE NULL AFTER `gein_x` 