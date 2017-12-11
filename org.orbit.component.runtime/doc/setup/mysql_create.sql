--
-- schema for IndexItemsData
--
CREATE SCHEMA IF NOT EXISTS origin;

USE origin;

--
-- Table for IndexItemsData
--
CREATE TABLE IF NOT EXISTS origin.IndexItemData (
	id int NOT NULL AUTO_INCREMENT,
	type varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	properties varchar(20000) DEFAULT NULL,
	lastUpdateTime varchar(50) DEFAULT NULL,
	PRIMARY KEY (id)
);

--
-- Table for IndexItemsLog
--
CREATE TABLE IF NOT EXISTS origin.IndexItemLog (
	revision int NOT NULL AUTO_INCREMENT,
	command varchar(255) NOT NULL,
	arguments varchar(20000) NOT NULL,
	lastUpdateTime varchar(50) DEFAULT NULL,
	PRIMARY KEY (revision)
);

commit;
