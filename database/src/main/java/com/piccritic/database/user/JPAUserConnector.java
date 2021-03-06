/**
 * JPAUserConnector.java
 * Created Feb 21, 2017
 */
package com.piccritic.database.user;

import com.piccritic.database.JPAConnector;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

/**
 * This class enables a connection to the database using JPAContainers. It has a number of methods for
 * performing user-related operations on the database. Implements {@link UserConnector}. Extends {@link JPAConnector}.
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class JPAUserConnector extends JPAConnector<Critic> implements UserConnector {

	private JPAContainer<UserLogin> logins;

	/**
	 * Initializes the JPAContainers for this UserConnector.
	 */
	public JPAUserConnector() {
		super(Critic.class);
		logins = JPAContainerFactory.make(UserLogin.class, entity);
	}

	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#insertCritic(com.piccritic.database.user.Critic, java.lang.String)
	 */
	public Critic insertCritic(Critic critic, String hash) throws UserException {

		validate(critic);
		container.addEntity(critic);
		UserLogin login = new UserLogin();
		login.setHandle(critic.getHandle());
		login.setHash(hash);
		logins.addEntity(login);

		return selectCritic(critic.getHandle());
	}

	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#deleteCritic(com.piccritic.database.user.Critic)
	 */
	public boolean deleteCritic(Critic critic) {
		String handle = critic.getHandle();
		container.removeItem(handle);
		logins.removeItem(handle);

		return !container.containsId(handle) && !logins.containsId(handle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#selectCritic(java.lang.String)
	 */
	public Critic selectCritic(String handle) {
		EntityItem<Critic> criticItem = container.getItem(handle);
		return (criticItem != null) ? criticItem.getEntity() : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#updateCritic(com.piccritic.database.user.Critic)
	 */
	@SuppressWarnings("unchecked")
	public Critic updateCritic(Critic critic) throws UserException {
		EntityItem<Critic> criticItem = container.getItem(critic.getHandle());
		
		validate(critic);
		criticItem.getItemProperty("firstName").setValue(critic.getFirstName());
		criticItem.getItemProperty("lastName").setValue(critic.getLastName());
		criticItem.getItemProperty("license").setValue(critic.getLicense());
		criticItem.getItemProperty("bio").setValue(critic.getBio());
		criticItem.commit();

		return selectCritic(critic.getHandle());
	}

	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#updateCritic(com.piccritic.database.user.Critic, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Critic updateCritic(Critic critic, String hash) throws UserException {

		EntityItem<UserLogin> loginItem = logins.getItem(critic.getHandle());
		loginItem.getItemProperty("hash").setValue(hash);
		loginItem.commit();
		return updateCritic(critic);
	}

	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.user.UserConnector#getUserHash(java.lang.String)
	 */
	public String getUserHash(String handle) {

		EntityItem<UserLogin> login = logins.getItem(handle);
		if (login == null) {
			return null;
		}
		return (String) login.getItemProperty("hash").getValue();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.database.JPAConnector#validate(java.lang.Object)
	 */
	protected void validate(Critic critic) throws UserException {
		// in the super validate method, violation.getPropertyPath() + " " + violation.getMessage());
		// is not present as it was in iteration 2 in this method. Leaving it out for now unless it is needed.
		try{
			super.validate(critic);
		} catch(Exception e) {
			throw new UserException(e.getMessage());
		}
	}
	
}
