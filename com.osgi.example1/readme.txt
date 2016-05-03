fsfilemetadata
------------------------------------------------------------------------
	fileId 			(string) (pk)
	parentFileId 	(string)
	name 			(string)
	isDirectory 	(boolean) // false by default
	isHidden 		(boolean) // false by default
	exists 			(boolean) // decided by the file content table. true if record is found. false if record is not found.
	canExecute 		(boolean) // true by default
	canRead 		(boolean) // true by default
	canWrite 		(boolean) // true by default
	length 			(long)
	lastModified 	(long)
------------------------------------------------------------------------


fsfilecontent
------------------------------------------------------------------------
	fileContentId 	(string) (pk)
	fileId 			(string) (fk)
	fileContent 	(blob)
------------------------------------------------------------------------


- SQL to create FsFileMetadata table -
------------------------------------------------------------------------
CREATE TABLE FsFileMetadata (
    fileId INT NOT NULL AUTO_INCREMENT,
    parentFileId INT NOT NULL,
    name VARCHAR(500) NOT NULL,
    isDirectory BOOLEAN NOT NULL DEFAULT 0,
    isHidden BOOLEAN NOT NULL DEFAULT 0,
    canExecute BOOLEAN NOT NULL DEFAULT 1,
    canRead BOOLEAN NOT NULL DEFAULT 1,
    canWrite BOOLEAN NOT NULL DEFAULT 1,
    length BIGINT,
    lastModified BIGINT,
    PRIMARY KEY (fileId)
);
------------------------------------------------------------------------


- SQL to create FsFileContent table -
------------------------------------------------------------------------
CREATE TABLE FsFileContent (
    fileContentId INT NOT NULL AUTO_INCREMENT,
    fileId INT NOT NULL,
    fileContent MEDIUMBLOB,
    PRIMARY KEY (fileContentId),
    FOREIGN KEY (fileId) REFERENCES FsFileMetadata(fileId)
);
------------------------------------------------------------------------


Data types:
------------------------------------------------------------------------
http://www.w3schools.com/sql/sql_datatypes.asp
https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
http://dev.mysql.com/doc/refman/5.7/en/storage-requirements.html
http://doc.ispirer.com/sqlways/Output/SQLWays-1-201.html
------------------------------------------------------------------------


Primary key:
------------------------------------------------------------------------
http://www.w3schools.com/sql/sql_primarykey.asp
------------------------------------------------------------------------


MySQL write/read blob example:
------------------------------------------------------------------------
http://www.mysqltutorial.org/mysql-jdbc-blob
http://www.javaxp.com/2012/11/mysql-jdbc-example-for-blob-storage.html
------------------------------------------------------------------------


