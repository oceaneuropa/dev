package org.origin.mgm.persistence;

import org.origin.common.jdbc.DatabaseTableAware;

/**
 * CRUD methods for the IndexItemCommandRequest table.
 *
 */
public class IndexItemCommandRequestTableHandler implements DatabaseTableAware {

	public static IndexItemCommandRequestTableHandler INSTANCE = new IndexItemCommandRequestTableHandler();

	@Override
	public String getTableName() {
		return "IndexItemCommandRequest";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	requestId int NOT NULL AUTO_INCREMENT,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(20000) NOT NULL,";
			sql += "	status varchar(50) DEFAULT NULL,";
			sql += "	requestTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (requestId)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	requestId serial NOT NULL,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(20000) NOT NULL,";
			sql += "	status varchar(50) DEFAULT NULL,";
			sql += "	requestTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (requestId)";
			sql += ");";
		}
		return sql;
	}

}
