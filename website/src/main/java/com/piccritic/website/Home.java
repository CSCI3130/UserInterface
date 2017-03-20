package com.piccritic.website;

import static com.piccritic.website.login.LoginService.getHandle;
import static com.piccritic.website.login.LoginService.getLoginStatus;
import static com.piccritic.website.login.LoginService.logoutUser;

import com.piccritic.website.login.LoginService.LoginStatus;
import com.piccritic.website.login.LoginWindow;
import com.piccritic.website.post.CreatePost;
import com.piccritic.website.post.ViewPost;
import com.piccritic.website.user.UserForm;
import com.piccritic.website.user.UserView;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This class implements the UI navigation for the project
 * 
 * @author Damien Robichaud <br> Francis Bosse
 *
 */
public class Home extends HorizontalSplitPanel {
	VerticalLayout menu = new VerticalLayout();
	Panel content = new Panel();
	Navigator navigator = new Navigator(UI.getCurrent(), content);

	public Home() {
		setupMenu();
		addComponent(menu);
		addComponent(content);
		setSizeFull();
		content.setSizeFull();
		setSplitPosition(200, Unit.PIXELS);
		setLocked(true);
		
		navigator.addView(ViewPost.NAME, ViewPost.class);
		navigator.addView(DefaultView.NAME, DefaultView.class);
		navigator.addView(UserView.NAME, UserView.class);
		if (navigator.getState().isEmpty()) {
			navigator.navigateTo(DefaultView.NAME);
			System.out.println(navigator.getState());
		} else {
			navigator.navigateTo(navigator.getState());
		}
	}
	
	
	/**
	 * Sets up menu based on auth
	 */
	private void setupMenu() {
		Button home = new Button("Home", e-> {
			navigator.navigateTo(DefaultView.NAME);
		});
		menu.addComponent(home);
		LoginStatus loginStatus = getLoginStatus();
		if (loginStatus == LoginStatus.LOGGED_IN) {
			Button logout = new Button("Log out");
			logout.addClickListener(e -> {
				logoutUser();
				Page.getCurrent().reload();
			});
			menu.addComponent(logout);

			Button button = new Button("Create Post");
			button.addClickListener(e -> {
				Window createPost = new CreatePost(getHandle());
				UI.getCurrent().addWindow(createPost);
			});

			menu.addComponent(button);
		} else {
			Button loginUser = new Button("Login");
			loginUser.addClickListener(e -> {
				Window login = new LoginWindow();
				addWindow(login);
			});
			menu.addComponent(loginUser);
		}

		Button createUser = new Button(((loginStatus == LoginStatus.LOGGED_IN) ? "Update" : "Create") + "User");
		createUser.addClickListener( e -> {
			Window userForm = new Window();
			userForm.setModal(true);
			userForm.setContent(new VerticalLayout(new UserForm(getHandle())));
			addWindow(userForm);
		});

		menu.addComponent(createUser);
		menu.setMargin(true);
		menu.setSpacing(true);

	}
	
	/**
	 * Shortcut method to add window
	 * 
	 * @param w Window to add
	 */
	private void addWindow(Window w) {
		UI.getCurrent().addWindow(w);
	}

}
