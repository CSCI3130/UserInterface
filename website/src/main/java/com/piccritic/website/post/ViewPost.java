package com.piccritic.website.post;

import java.io.File;

import com.piccritic.database.post.Post;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ViewPost extends VerticalLayout implements View {
	private Post post; // post object to view
	private Image image = new Image();
	private Label postDescription = new Label();

	private Button delete = new Button("Delete post", e -> {
	});

	private Button edit = new Button("Edit post", e -> {
		UI.getCurrent().addWindow(new CreatePost("handle", post));
	});

	public static final String NAME = "post";

	public ViewPost() {
		addComponent(image);
		addComponent(postDescription);
		setMargin(true);
		setSpacing(true);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		image.setCaption(event.getParameters());
		image.setSource(new FileResource(new File("users/" + event.getParameters())));
		image.setSizeFull();
		postDescription.setValue(System.getProperty("user.dir"));
		if (event.getParameters().matches("handle" + "/.*")) {
			addComponent(delete);
			addComponent(edit);
		}
	}

}
