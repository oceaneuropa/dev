package org.origin.common.jdbc;

import java.sql.Connection;

public interface ConnectionAware {

	public Connection getConnection();

}
