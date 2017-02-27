package com.piccritic.website;

import javax.servlet.annotation.WebServlet;

import com.piccritic.compute.post.PostService;
import com.piccritic.compute.user.UserService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class PicCritic extends UI {

	private static final long serialVersionUID = 1L;
	public static PostService postService = new PostService();
	public static UserService userService = UserService.createService();

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setContent(new Home());
	}


	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = PicCritic.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}
}
