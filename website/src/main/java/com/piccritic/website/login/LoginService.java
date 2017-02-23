/**
 * LoginSerice.java
 * Created Feb 21, 2017
 */
package com.piccritic.website.login;

import java.sql.SQLException;

import com.piccritic.compute.hashing.Hasher;
import com.vaadin.server.VaadinSession;

public class LoginService {
	public enum LoginStatus {
		LOGGED_IN, LOGGED_OUT, INVALID_INFO, SESSION_TIMEOUT, ERROR
	}
	
	private static final String LOGIN_STATUS = "LoginStatus";
	private static final String SESSION_HANDLE = "Handle";
	
	/**
	 * Attempts to login user with submitted information, returns status
	 * of login attempt.
	 * 
	 * @param handle User submitted handle
	 * @param password User submitted password
	 * @return LoginStatus for the user's session
	 * @throws SQLException 
	 */
	public static LoginStatus loginUser(String handle, String password) {
		try {
			Hasher hasher = new Hasher();
			if (hasher.checkLogin(handle, password)) {
				VaadinSession.getCurrent().setAttribute(LOGIN_STATUS, LoginStatus.LOGGED_IN);
				VaadinSession.getCurrent().setAttribute(SESSION_HANDLE, handle);
				return LoginStatus.LOGGED_IN;
			} else {
				return LoginStatus.INVALID_INFO;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return LoginStatus.INVALID_INFO;
		}
	}
	
	/**
	 * Gets the LoginStatus of the user
	 * 
	 * @return LoginStatus
	 */
	public static LoginStatus getLoginStatus() {
		LoginStatus status = (LoginStatus) VaadinSession.getCurrent().getAttribute(LOGIN_STATUS);
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
	public static LoginStatus logoutUser() {
		LoginStatus status = (LoginStatus) VaadinSession.getCurrent().getAttribute(LOGIN_STATUS);
		if (status == null) {
			return LoginStatus.ERROR;
		} else {
			VaadinSession.getCurrent().setAttribute(LOGIN_STATUS, LoginStatus.LOGGED_OUT);
			VaadinSession.getCurrent().setAttribute(SESSION_HANDLE, null);
			return LoginStatus.LOGGED_OUT;
		}
	}
	
	/**
	 * Returns the user handle of the logged in user
	 * 
	 * @return string handle
	 */
	public static String getHandle() {
		return (String) VaadinSession.getCurrent().getAttribute(SESSION_HANDLE);
	}
	
}
