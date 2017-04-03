package com.piccritic.website;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.piccritic.database.post.Post;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PostQuickView extends GridLayout {

	Image image;

	public PostQuickView() {
		super(4, 3);
		setSizeFull();
		setHeightUndefined();
	}

	public void initPosts(List<Post> posts) {
		this.removeAllComponents();
		
		System.out.println("does this happen twice?");
		
		//add sorting options combo box
		List<String> sortOptions = new ArrayList<>();
		sortOptions.add("Title");
		sortOptions.add("Upload Date");
		
		ComboBox select = new ComboBox("Select sorting option");
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
		
		addComponent(select, 0, 0, 3, 1);
		
		//add posts
		Iterator<Post> it = posts.iterator();
		for (int y = 0; y < 9; y++) {
			if (it.hasNext()) {
				addComponent(new PostTile(it.next()));
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
