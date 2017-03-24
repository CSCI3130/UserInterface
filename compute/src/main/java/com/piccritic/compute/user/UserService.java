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
import com.piccritic.database.post.AlbumConnector;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.JPAAlbumConnector;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserConnector;
import com.piccritic.database.user.UserException;

/**
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class UserService implements UserServiceInterface {
  	private static UserServiceInterface instance;
  	private static UserConnector uc;
    private static String createSuccess = "Profile successfully created!";
    private static String updateSuccess = "Profile successfully updated!";
    private static String createFailure = "Profile could not be created.";
    private static String updateFailure = "Profile could not be updated.";
    private static String passwordShort = "Password must be at least 8 characters.";
    private static String handleInUse = "Handle already in use.";
  	
  	private UserService() {
      	uc = new JPAUserConnector();
      	
    }
  
  	public static UserServiceInterface createService() {
      	if (instance == null) {
			final UserServiceInterface service = new UserService();
          	instance = service;
        }
      	
      	return instance;
    }

  	/* (non-Javadoc)
	 * @see com.piccritic.compute.user.UserServiceInterface#create(com.piccritic.database.user.Critic, java.lang.String)
	 */
  	@Override
	public String create(Critic critic, String password) throws UserException {
  		if (critic == null) {
  			throw new UserException(createFailure);
  		}
  		
  		if (password.length() < 8) {
  			throw new UserException(passwordShort);
  		}

		critic.setJoinDate(new Date(Calendar.getInstance().getTime().getTime()));
      	Critic selected = uc.selectCritic(critic.getHandle());
      	if (selected != null) {
          	throw new UserException(handleInUse);
        }

		try {
			Hasher hasher = new Hasher();
			String hash = hasher.generateHash(password);
			Critic inserted = uc.insertCritic(critic, hash);
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
			AlbumConnector ac = new JPAAlbumConnector();
			ac.insertAlbum(defaultAlbum);
		} catch (AlbumException e) {
			e.printStackTrace();
			throw new UserException(e.getLocalizedMessage());
		}
		
		Path usersPath = Paths.get("users", critic.getHandle());
		File users = usersPath.toFile();
		if (!users.exists() && !users.mkdirs()) {
			throw new UserException("Could not create image storage");
		}

      	return createSuccess;
    }
  
  	/* (non-Javadoc)
	 * @see com.piccritic.compute.user.UserServiceInterface#update(com.piccritic.database.user.Critic, java.lang.String)
	 */
  	@Override
	public String update(Critic critic, String password) throws UserException {
      	Critic selected = uc.selectCritic(critic.getHandle());
      	if (selected == null) {
        	throw new UserException(updateFailure);
        }
      
      	Critic updated;
      	if (password.isEmpty()) {
          	updated = uc.updateCritic(critic);
          	if (updated == null) {
              	throw new UserException(updateFailure);
            }
          	return updateSuccess;
        } else {
			try {
				Hasher hasher = new Hasher();
				String hash = hasher.generateHash(password);
				Critic inserted = uc.insertCritic(critic, hash);
				if (inserted == null) {
					throw new UserException(createFailure);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(createFailure);
			}
        }
      
      	String hash = uc.getUserHash(critic.getHandle());
      	if (hash == null) {
        	throw new UserException(updateFailure);
        }
      
      	return updateSuccess;
    }
  	
  	/* (non-Javadoc)
	 * @see com.piccritic.compute.user.UserServiceInterface#select(java.lang.String)
	 */
  	@Override
	public Critic select(String handle) {
  		return uc.selectCritic(handle);
  	}
}
