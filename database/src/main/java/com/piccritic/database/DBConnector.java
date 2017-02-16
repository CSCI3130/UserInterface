/**
 * DBConnector.java
 * Created Feb 15, 2017
 */
package com.piccritic.database;

import java.sql.*;

/**
 * DBConnector class provides a single connection to the database
 * server.
 *
 * This allows for many queries to use the same connection for any
 * type of data.
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
public abstract class DBConnector {

	protected static Connection dbCon;

	static {
		try {
			String databaseUrl = System.getenv("JDBC_DATABASE_URL");
			dbCon = DriverManager.getConnection(databaseUrl);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
