USE `researchers_db`;
DROP TABLE IF EXISTS `researchers_db`.`researchers_catalog`;
CREATE TABLE `researchers_db`.`researchers_catalog` (
`id` varchar(16) NOT NULL,
`name` varchar(32) NOT NULL,
`birthdate` date NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;