package com.piccritic.website.login;

import com.vaadin.ui.Window;

/**
 * This object is used to generate a login window
 * that allows the user to login.
 * 
 * @see LoginForm
 * 
 * @author Damien Robichaud <br>
 * 		Francis Bosse
 */
public class LoginWindow extends Window {

	LoginForm loginForm = new LoginForm();

	public LoginWindow() {
		super("Login");
		setResizable(false);
		setModal(true);
		setContent(loginForm);
	}
}
