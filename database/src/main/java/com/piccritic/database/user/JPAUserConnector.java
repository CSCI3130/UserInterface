/**
 * JPAUserConnector.java
 * Created Feb 21, 2017
 */
package com.piccritic.database.user;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

/**
 * This class enables connection to the database using JPAContainers.
 * It has a number of methods for performing user-related operations on the database.
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class JPAUserConnector implements UserConnector {
	
	private JPAContainer<Critic> critics;
	private JPAContainer<UserLogin> logins;
	
	public JPAUserConnector() {
		critics = JPAContainerFactory.make(Critic.class, "postgres");
		logins = JPAContainerFactory.make(UserLogin.class, "postgres");
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#insertCritic(com.piccritic.database.user.Critic, java.lang.String)
	 */
	public Critic insertCritic(Critic critic, String hash) {
		
		critics.addEntity(critic);
		UserLogin login = new UserLogin();
		login.setHandle(critic.getHandle());
		login.setHash(hash);
		logins.addEntity(login);
		
		return selectCritic(critic.getHandle());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#deleteCritic(com.piccritic.database.user.Critic)
	 */
	public boolean deleteCritic(Critic critic) {
		String handle = critic.getHandle();
		critics.removeItem(handle);
		logins.removeItem(handle);
		
		return !critics.containsId(handle) && !logins.containsId(handle);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#selectCritic(java.lang.String)
	 */
	public Critic selectCritic(String handle) {
		
		EntityItem<Critic> criticItem = critics.getItem(handle);
		return (criticItem != null) ? criticItem.getEntity() : null;
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#updateCritic(com.piccritic.database.user.Critic)
	 */
	@SuppressWarnings("unchecked")
	public Critic updateCritic(Critic critic) {
		
		EntityItem<Critic> criticItem = critics.getItem(critic.getHandle());
		criticItem.getItemProperty("firstName").setValue(critic.getFirstName());
		criticItem.getItemProperty("lastName").setValue(critic.getLastName());
		criticItem.getItemProperty("licenseID").setValue(critic.getLicenseID());
		criticItem.getItemProperty("bio").setValue(critic.getBio());
		criticItem.commit();
		
		return selectCritic(critic.getHandle());
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#updateCritic(com.piccritic.database.user.Critic, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Critic updateCritic(Critic critic, String hash) {
		
		EntityItem<UserLogin> loginItem = logins.getItem(critic.getHandle());
		loginItem.getItemProperty("hash").setValue(hash);
		loginItem.commit();
		return updateCritic(critic);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#getUserHash(java.lang.String)
	 */
	public String getUserHash(String handle) {
		
		return (String) logins.getItem(handle).getItemProperty("hash").getValue();
	}
	
}
