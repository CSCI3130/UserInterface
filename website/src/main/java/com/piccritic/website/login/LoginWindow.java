package com.piccritic.website.login;

import com.vaadin.ui.Window;

public class LoginWindow extends Window {

	LoginForm loginForm = new LoginForm();

	public LoginWindow() {
		super("Login");
		setResizable(false);
		setModal(true);
		setContent(loginForm);
	}

}
