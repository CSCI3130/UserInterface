/**
 * DBConnector.java
 * Created Feb 15, 2017
 */
package com.piccritic.database;

import java.sql.*;

/**
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public abstract class DBConnector {
	
	protected static Connection dbCon;
	
	static {
		try {
			dbCon = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=piccritic");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
