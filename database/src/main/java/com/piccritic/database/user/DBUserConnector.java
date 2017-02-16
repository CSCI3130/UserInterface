/**
 * DBUserConnector.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

import com.piccritic.database.DBConnector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class DBUserConnector extends DBConnector implements UserConnector {
	
	private PreparedStatement insertStatement;
	private PreparedStatement deleteStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement selectStatement;
	private ResultSet res; // go
	
	public DBUserConnector() throws SQLException {
		insertStatement = dbCon.prepareStatement("insert into piccritic.\"User\" values(?,?,?,?,?,?,?,?);");
		
		deleteStatement = dbCon.prepareStatement("delete from piccritic.\"User\" where \"User_handle\" = ?;");
		
		updateStatement = dbCon.prepareStatement("update piccritic.\"User\" set \"User_hash\"=?,"
				+ "User_salt\"=?, \"User_firstName\"=?, \"User_lastName\"=?,"
				+ "\"User_joinDate\"=?, \"User_bio\"=?, \"License_ID\"=? where \"User_handle\" = ?;");
		
		selectStatement = dbCon.prepareStatement("select * from piccritic.\"User\" where \"User_handle\" = ?;");
	}
	
	public User insertUser(User user, String hash, String salt) {
		try {
			insertStatement.setString(1, user.getHandle());
			insertStatement.setString(2, hash);
			insertStatement.setString(3, salt);
			insertStatement.setString(4, user.getFirstName());
			insertStatement.setString(5, user.getLastName());
			insertStatement.setDate(6, user.getJoinDate());
			insertStatement.setString(7, user.getBio());
			insertStatement.setInt(8, user.getLicenseID());
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return selectUser(user.getHandle());
	}
	
	public boolean deleteUser(User user) {
		try {
			deleteStatement.setString(1, user.getHandle());
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public User updateUser(User user, String hash, String salt) {
		try {
			updateStatement.setString(1, hash);
			updateStatement.setString(2, salt);
			updateStatement.setString(3, user.getFirstName());
			updateStatement.setString(4, user.getLastName());
			updateStatement.setDate(5, user.getJoinDate());
			updateStatement.setString(6, user.getBio());
			updateStatement.setInt(7, user.getLicenseID());
			updateStatement.setString(8, user.getHandle());
			updateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return selectUser(user.getHandle());
	}
	
	public User selectUser(String handle) {
		User user = new User();
		try {
			selectStatement.setString(1, handle);
			res = selectStatement.executeQuery();
			boolean hasRow = res.next();
			
			user.setHandle(res.getString(1));
			user.setFirstName(res.getString(4));
			user.setLastName(res.getString(5));
			user.setJoinDate(res.getDate(6));
			user.setBio(res.getString(7));
			user.setLicenseID(res.getInt(8));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
}
