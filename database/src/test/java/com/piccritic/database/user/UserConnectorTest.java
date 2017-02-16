/**
 * UserConnectorTest.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.sql.Date;

/**
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class UserConnectorTest {
	
	UserConnector con;
	User user;
	
	@Before
  	public void init() throws SQLException {
    	con = new DBUserConnector();
      	user = new User();
      
    	user.setHandle("damienr74");
        user.setFirstName("Damien");
      	user.setLastName("Lowe");
      	user.setJoinDate(new Date(0));
      	user.setLicenseID(0);
      	user.setBio("I'm cool");
    }
  
  	@Test
  	public void testInsertUser() {
      	User inserted = con.insertUser(user, "", "");
        assertEquals(user, con.selectUser(inserted.getHandle()));
  	}
	
  	@After
  	public void clearUsers() {
  		con.deleteUser(user);
  	}
}
