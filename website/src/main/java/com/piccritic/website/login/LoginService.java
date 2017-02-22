package com.piccritic.website.login;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;

import java.sql.SQLException;

import com.piccritic.compute.hashing.*;

public class LoginService {
	public enum LoginStatus {
		LOGGED_IN, LOGGED_OUT, INVALID_INFO, SESSION_TIMEOUT, ERROR
	}
	
	/**
	 * Attempts to login user with submitted information, returns status
	 * of login attempt.
	 * 
	 * @param handle User submitted handle
	 * @param password User submitted password
	 * @return LoginStatus for the user's session
	 * @throws SQLException 
	 */
	public LoginStatus loginUser(String handle, String password) throws SQLException {
		Hasher hasher = new Hasher();
		if (hasher.checkLogin(handle, password)) {
			VaadinSession.getCurrent().setAttribute("LoginStatus", LoginStatus.LOGGED_IN);
			return LoginStatus.LOGGED_IN;
		} else {
			return LoginStatus.INVALID_INFO;
		}
	}
	
	/**
	 * Gets the LoginStatus of the user
	 * 
	 * @return LoginStatus
	 */
	public LoginStatus getLoginStatus() {
		LoginStatus status = (LoginStatus) VaadinSession.getCurrent().getAttribute("LoginStatus");
		if (status == null) {
			return LoginStatus.ERROR;
		} else {
			return status;
		}
	}
	
	/**
	 * Logs out the user. 
	 * 
	 * @return LoginStatus is LOGGED_OUT if the log out is successful.
	 */
	public LoginStatus logoutUser() {
		LoginStatus status = (LoginStatus) VaadinSession.getCurrent().getAttribute("LoginStatus");
		if (status == null) {
			return LoginStatus.ERROR;
		} else {
			VaadinSession.getCurrent().setAttribute("LoginStatus", LoginStatus.LOGGED_OUT);
			return LoginStatus.LOGGED_OUT;
		}
	}
	
}
