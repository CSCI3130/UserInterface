package com.piccritic.website.login;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class LoginForm extends FormLayout {

	TextField handle = new TextField("User Name");
	PasswordField password = new PasswordField("Password");
	Button login = new Button("Login");
	Button signUp = new Button("Sign Up");


	public LoginForm() {
		handle.setRequired(true);
		password.setRequired(true);
		HorizontalLayout actions = new HorizontalLayout(login, signUp);
		addComponents(handle, password, actions);
	}
}
