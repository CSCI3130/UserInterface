package com.piccritic.website;


import javax.servlet.annotation.WebServlet;

import com.piccritic.compute.user.UserService;
import com.piccritic.website.user.UserForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended
 * to be overridden to add component to the critic interface and initialize non-component
 * functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	
	public UserService userService = UserService.createService();

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		Button button = new Button("Get Project Name");
		button.addClickListener( e -> {
			Window userForm = new Window();
			userForm.setContent(new VerticalLayout(new UserForm(null)));
			addWindow(userForm);
		});

		layout.addComponent(button);
		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
