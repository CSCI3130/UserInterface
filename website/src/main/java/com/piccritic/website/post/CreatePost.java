package com.piccritic.website.post;

import com.piccritic.compute.post.PostService;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostException;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window;

/**
 * CreatePost Window to allow user to create post.
 * 
 * @author Rhianna Gogen
 * Also edited By Damien Robichaud and Francis Bosse
 */
@Theme("mytheme")
public class CreatePost extends Window implements SucceededListener {

	private static final long serialVersionUID = 8544572658091510439L;
	private TextArea description = new TextArea("Post Description");
	private TextField title = new TextField("Post Title");
	private ComboBox license = new ComboBox("License");
	private Upload upload;
	private GridLayout tags = new GridLayout(2, 2);
	private Post post = new Post();
	private ImageReceiver receiver;
	private Image image = new Image("Uploaded Image");
	private Button confirm = new Button("Confirm", this::confirmUpload);
	private PostService service = new PostService();

	/**
	 * This window is responsible for creating a post form with an @see Upload
	 * component a post will only be uploaded if a picture, a title and a
	 * description.
	 * 
	 * @param handle user that is creating the post
	 */
	public CreatePost(String handle) {
		setSizeFull();
		setModal(true);
		Panel layout = new Panel();
		setContent(layout);
		final FormLayout form = new FormLayout();
		layout.setContent(form);
		if (handle == null) {
			close();
		}
		

		image.setVisible(false);
		image.setSizeFull();
		image.setHeightUndefined();
		receiver = new ImageReceiver(handle, post);
		upload = new Upload("Upload Image Here", receiver);
		upload.addSucceededListener(this);

		/*
		 * make sure confirm is only enabled when there's a valid picture
		 */
		upload.addStartedListener(e -> {
			confirm.setEnabled(true);
		});

		form.setMargin(true);
		form.addComponent(title);
		form.addComponent(image);
		form.addComponent(description);
		form.addComponent(tags);
		form.addComponent(license);
		form.addComponent(upload);
		form.addComponent(confirm);
		form.getComponent(0);
		title.setSizeFull();
		description.setSizeFull();

		confirm.setEnabled(true);
		description.setRequired(true);
		tags.addComponent(new CheckBox("Nature"), 0, 0);
		tags.addComponent(new CheckBox("People"), 1, 0);
		tags.addComponent(new CheckBox("Sports"), 0, 1);
		tags.addComponent(new CheckBox("Urban"), 1, 1);
		title.setRequired(true);
	}
	
	public CreatePost(String handle, Post post) {
		this(handle);
		this.post = post;
		if (post != null) {
			title.setValue(post.getTitle());
			description.setValue(post.getDescription());
		}
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// Show the uploaded file in the image viewer
		confirm.setEnabled(true);
		image.setVisible(true);
		image.setSource(new FileResource(receiver.getFile()));
		Notification.show("Image Saved", Type.TRAY_NOTIFICATION);
	}

	/**
	 * Validates data and asks back-end to upload post.
	 * 
	 * @param event
	 */
	private void confirmUpload(Button.ClickEvent event) {
		try {
			title.validate();
			description.validate();
			if (service != null) {
				Post created = service.createPost(post);
				if (created != null) {
					Notification.show("Post Uploaded", Type.TRAY_NOTIFICATION);
					close();
					String postLocation = "#!post/" + post.getPath();
					UI.getCurrent().getPage().setLocation(postLocation);
					UI.getCurrent().getPage().reload();
				}
			}
			
			Notification.show("Could not create post.", Type.WARNING_MESSAGE);

		} catch (PostException e) {
			upload.interruptUpload();
			Notification.show(e.getMessage(), Type.WARNING_MESSAGE);
		}
		
	}
}
