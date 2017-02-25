package com.piccritic.website;

import javax.servlet.annotation.WebServlet;

import com.piccritic.compute.user.UserService;
import com.piccritic.website.post.CreatePost;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import compute.piccritic.compute.post.PostServiceInterface;

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
	public PostServiceInterface postService;
	public UserService userService;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		Button button = new Button("Get Project Name");
		button.addClickListener(e -> {
			Window createPost = new CreatePost(getHandle());
			addWindow(createPost);
		});

		layout.addComponent(button);
		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = PicCritic.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {

		private static final long serialVersionUID = 1L;
	}

	private String getHandle() {
		return "userHandle";
	}
}
