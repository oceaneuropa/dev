package org.origin.common.jdbc;

public interface DatabaseTableAware {

	public static final String MYSQL = "mysql";
	public static final String POSTGRE = "postgre";
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
	 * @param db
	 * @return
	 */
	public String getCreateTableSQL(String db);

}
