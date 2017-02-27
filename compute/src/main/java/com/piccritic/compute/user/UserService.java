/**
 * JPAUserConnector.java
 * Created Feb 16, 2017
 */
package com.piccritic.compute.user;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.piccritic.compute.hashing.Hasher;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserConnector;
import com.piccritic.database.user.UserException;

/**
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class UserService {
  	private static UserService instance;
  	private static UserConnector connector;
  	// private static Hasher hasher;
    private static String createSuccess = "Profile successfully created!";
    private static String updateSuccess = "Profile successfully updated!";
    private static String createFailure = "Profile could not be created.";
    private static String updateFailure = "Profile could not be updated.";
    private static String passwordShort = "Password must be at least 8 characters.";
    private static String handleInUse = "Handle already in use.";
  	
  	private UserService() {
      	connector = new JPAUserConnector();
      	// hasher = new Hasher();
    }
  
  	public static UserService createService() {
      	if (instance == null) {
			final UserService service = new UserService();
          	instance = service;
        }
      	
      	return instance;
    }

  	public String create(Critic critic, String password) throws UserException {
  		if (critic == null) {
  			throw new UserException(createFailure);
  		}
  		
  		if (password.length() < 8) {
  			throw new UserException(passwordShort);
  		}

		critic.setJoinDate(new Date(Calendar.getInstance().getTime().getTime()));
      	Critic selected = connector.selectCritic(critic.getHandle());
      	if (selected != null) {
          	throw new UserException(handleInUse);
        }

		try {
			Hasher hasher = new Hasher();
			String hash = hasher.generateHash(password);
			Critic inserted = connector.insertCritic(critic, hash);
			if (inserted == null) {
				throw new UserException(createFailure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException(createFailure);
		}

		Album defaultAlbum = new Album();
		defaultAlbum.setCritic(critic);
		defaultAlbum.setCreationDate(critic.getJoinDate());
		defaultAlbum.setName("default");
		Set<Album> albums = new HashSet<Album>();
		albums.add(defaultAlbum);
		critic.setAlbums(albums);

		try {
			JPAPostConnector pc = new JPAPostConnector();
			pc.insertAlbum(defaultAlbum);
		} catch (AlbumException e) {
			e.printStackTrace();
			throw new UserException(e.getLocalizedMessage());
		}
		
		Path usersPath = Paths.get("users", critic.getHandle());
		File users = usersPath.toFile();
		if (!users.mkdirs()) {
			throw new UserException("Could not create image storage");
		}

      	return createSuccess;
    }
  
  	public String update(Critic critic, String password) throws UserException {
      	Critic selected = connector.selectCritic(critic.getHandle());
      	if (selected == null) {
        	throw new UserException(updateFailure);
        }
      
      	Critic updated;
      	if (password.isEmpty()) {
          	updated = connector.updateCritic(critic);
          	if (updated == null) {
              	throw new UserException(updateFailure);
            }
          	return updateSuccess;
        } else {
			try {
				Hasher hasher = new Hasher();
				String hash = hasher.generateHash(password);
				Critic inserted = connector.insertCritic(critic, hash);
				if (inserted == null) {
					throw new UserException(createFailure);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(createFailure);
			}
        }
      
      	String hash = connector.getUserHash(critic.getHandle());
      	if (hash == null) {
        	throw new UserException(updateFailure);
        }
      
      	return updateSuccess;
    }
  	
  	public Critic select(String handle) {
  		return connector.selectCritic(handle);
  	}
}
