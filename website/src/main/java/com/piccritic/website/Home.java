package com.piccritic.website;

import com.piccritic.website.post.CreatePost;
import com.piccritic.website.post.ViewPost;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

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
		if (navigator.getState().isEmpty()) {
			navigator.navigateTo(ViewPost.NAME);
		} else {
			navigator.navigateTo(navigator.getState());
		}
	}
	
	

	private void setupMenu() {
		Button button = new Button("Create Post");
		button.addClickListener(e -> {
			Window createPost = new CreatePost("handle");
			UI.getCurrent().addWindow(createPost);
		});

		menu.addComponent(button);
		button.setSizeFull();
	}

}
