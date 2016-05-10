osgi.fs.example

------------------------------------------------------------------------
com.osgi.example
lib.jdbc
lib.jersey
lib.log
lig.org.apache.commons
lib.org.json
lib.org.junit
org.origin.common

com.eclipsesource.jaxrs.jersey-all
javax.activation
javax.annotation
javax.inject
javax.servlet
javax.ws.rs-api
org.apache.felix.gogo.command
org.apache.felix.gogo.runtime
org.apache.felix.gog.shell
org.eclipse.equinox.common
org.eclipse.equinox.console
org.eclipse.equinox.ds
org.eclipse.equinox.event
org.eclipse.equinox.frameworkadmin
org.eclipse.equinox.frameworkadmin.equinox
org.eclipse.equinox.http.jetty
org.eclipse.equinox.http.servlet
org.eclipse.equinox.util
org.eclipse.jetty.http
org.eclipse.jetty.io
org.eclipse.jetty.security
org.eclipse.jetty.server
org.eclipse.jetty.servlet
org.eclipse.jetty.util
org.eclipse.osgi
org.eclipse.osgi.services
org.eclipse.osgi.util
------------------------------------------------------------------------

Program arguments:
------------------------------------------------------------------------
-os ${target.os} -ws ${target.ws} -arch ${target.arch} -nl ${target.nl} -consoleLog -console
------------------------------------------------------------------------

VM arguments:
------------------------------------------------------------------------
-Declipse.ignoreApp=true -Dosgi.noShutdown=true -Dorg.osgi.service.http.port=9090
------------------------------------------------------------------------


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
    length BIGINT DEFAULT 0,
    lastModified BIGINT DEFAULT 0,
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


