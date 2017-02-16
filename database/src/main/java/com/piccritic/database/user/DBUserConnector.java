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
 * UserConnector implementation used to query data from the SQL database.
 *
 * The class instance should not be used directly use its interface
 * @see UserConnector
 *
 * This class takes the instance from the abstract @see DBConnector and uses
 * the connection to generate User specific queries. This allows the UI to
 * communicate efficiently with the database, and the minimisation of
 * coupling.
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class DBUserConnector extends DBConnector implements UserConnector {

	private PreparedStatement insertStatement;
	private PreparedStatement deleteStatement;
	private PreparedStatement simpleUpdateStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement selectStatement;
	private ResultSet res; // go

	public DBUserConnector() throws SQLException {
		insertStatement = dbCon.prepareStatement("insert into piccritic.\"User\" values(?,?,?,?,?,?,?,?);");

		deleteStatement = dbCon.prepareStatement("delete from piccritic.\"User\" where \"User_handle\" = ?;");

		simpleUpdateStatement = dbCon.prepareStatement("update piccritic.\"User\" set "
				+ "\"User_firstName\"=?, \"User_lastName\"=?, \"User_bio\"=?, \"License_ID\"=? "
				+ "where \"User_handle\"=?;");

		updateStatement = dbCon.prepareStatement("update piccritic.\"User\" set \"User_hash\"=?, "
				+ "\"User_firstName\"=?, \"User_lastName\"=?, \"User_bio\"=?, "
				+ "\"License_ID\"=? where \"User_handle\"=?;");

		selectStatement = dbCon.prepareStatement("select * from piccritic.\"User\" where \"User_handle\"=?;");
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

	public User updateUser(User user) {
		try {
			simpleUpdateStatement.setString(1, user.getFirstName());
			simpleUpdateStatement.setString(2, user.getLastName());
			simpleUpdateStatement.setString(3, user.getBio());
			simpleUpdateStatement.setInt(4, user.getLicenseID());
			simpleUpdateStatement.setString(5, user.getHandle());
			simpleUpdateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return selectUser(user.getHandle());
	}

	public User updateUser(User user, String hash) {
		try {
			updateStatement.setString(1, hash);
			updateStatement.setString(2, user.getFirstName());
			updateStatement.setString(3, user.getLastName());
			updateStatement.setString(4, user.getBio());
			updateStatement.setInt(5, user.getLicenseID());
			updateStatement.setString(6, user.getHandle());
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
			res.next();

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
