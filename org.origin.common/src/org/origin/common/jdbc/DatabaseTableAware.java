package org.origin.common.jdbc;

public interface DatabaseTableAware {

	public static final String MYSQL = "mysql";
	public static final String POSTGRESQL = "postgresql";
	public static final String ORACLE = "oracle";
	public static final String DB2 = "db2";
	public static final String SQLSERVER = "sqlserver";

	/**
	 * 
	 * @return
	 */
	public String getTableName();

	/**
	 * 
	 * @param database
	 * @return
	 */
	public String getCreateTableSQL(String database);

}
