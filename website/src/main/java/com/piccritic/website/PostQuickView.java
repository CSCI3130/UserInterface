package com.piccritic.website;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.piccritic.compute.MasterConnector;
import com.piccritic.database.post.Post;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.piccritic.database.user.Critic;
import com.piccritic.website.post.TagChooser;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PostQuickView extends VerticalLayout {

	protected Critic critic;
	protected ComboBox select;
	Image image;
	HorizontalLayout tagSearch = new HorizontalLayout();
	GridLayout gl = new GridLayout(3, 3);
	TagChooser chooser = new TagChooser();
	Button searchTags = new Button("Filter", e -> {
		List<String> tags = chooser.getTags();
		List<Post> posts = MasterConnector.tagConnector.findPosts(tags, critic);
		if (posts == null || posts.isEmpty() ) {
			Notification.show("No results", Type.WARNING_MESSAGE);
		} else {
			initPosts(posts);
		}
	});

	public PostQuickView() {
		setSizeFull();
		setHeightUndefined();
		tagSearch.addComponent(chooser);
		tagSearch.addComponent(searchTags);
		addComponent(tagSearch);
		gl.setSizeFull();
		gl.setHeightUndefined();
		
		//add sorting options combo box
		List<String> sortOptions = new ArrayList<>();
		sortOptions.add("Title");
		sortOptions.add("Upload Date");
		sortOptions.add("License");
		
		select = new ComboBox("Select sorting option");
		select.addItems(sortOptions);
		
		ValueChangeListener listener = new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String option = (String) event.getProperty().getValue();
				option = option.replaceAll(" ", "");
				UI.getCurrent().getPage().setLocation("#!home/" + option);
				UI.getCurrent().getPage().reload();
			}
		};
		
		select.addValueChangeListener(listener);
		
		addComponent(select);
		
		addComponent(gl);
		
		
	}

	public void initPosts(List<Post> posts) {		
		//add posts
		gl.removeAllComponents();
		if (posts == null) {
			return;
		}
		Iterator<Post> it = posts.iterator();
		for (int y = 0; y < 9; y++) {
			if (it.hasNext()) {
				gl.addComponent(new PostTile(it.next()));
			} else {
				return;
			}
		}
	}

	class PostTile extends VerticalLayout {
		Post post;

		public PostTile(Post p) {
			setMargin(true);
			post = p;
			image = new Image(post.getTitle(), new FileResource(new File(post.getPath())));
			Label feedback = new Label(
					"<center><p style=\"background-color:rgba(55,55,55,0.7);color:rgb(255,255,255)\">Quantitative feedback</p></center>",
					Label.CONTENT_RAW);
			addComponent(image);
			addComponent(feedback);
			this.setComponentAlignment(feedback, Alignment.BOTTOM_CENTER);

			image.setWidth("100%");
			image.setHeightUndefined();
			this.setStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
			setSizeFull();
			this.addLayoutClickListener(e -> {
				UI.getCurrent().getPage().setLocation("#!post/" + post.getPath());
				UI.getCurrent().getPage().reload();
			});
		}
	}
}
