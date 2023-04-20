USE `researchers_db`;
DROP TABLE IF EXISTS `researchers_db`.`researchers_catalog`;
CREATE TABLE `researchers_db`.`researchers_catalog` (
`ID` varchar(16) NOT NULL,
`NAME` varchar(32) NOT NULL,
`BIRTHDATE` date NULL
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;