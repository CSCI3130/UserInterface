package com.piccritic.website.post;

import com.piccritic.database.post.Post;
import com.piccritic.website.PicCritic;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window;

import compute.piccritic.compute.post.PostServiceInterface;

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
	private Button confirm = new Button("Confirm Upload", this::confirmUpload);

	/**
	 * This window is responsible for creating a post form with an @see Upload
	 * component a post will only be uploaded if a picture, a title and a
	 * description.
	 * 
	 * @param handle user that is creating the post
	 */
	public CreatePost(String handle) {
		final FormLayout form = new FormLayout();

		image.setVisible(false);
		receiver = new ImageReceiver(getUI(), handle);
		upload = new Upload("Upload Image Here", receiver);
		upload.addSucceededListener(this);

		/*
		 * make sure confirm is only enabled when there's a valid picture
		 */
		upload.addStartedListener(e -> {
			confirm.setEnabled(false);
		});

		setContent(form);
		form.setMargin(true);
		form.setWidth("80%");
		form.addComponent(title);
		form.addComponent(image);
		form.addComponent(description);
		form.addComponent(tags);
		form.addComponent(license);
		form.addComponent(upload);
		form.addComponent(confirm);
		form.getComponent(0);
		for (int i = 0; i < form.getComponentCount(); i++) {
			form.getComponent(i).setSizeFull();
		}
		confirm.setEnabled(false);
		description.setRequired(true);
		description.setRequiredError("Post needs a desctription");
		tags.addComponent(new CheckBox("Nature"), 0, 0);
		tags.addComponent(new CheckBox("People"), 1, 0);
		tags.addComponent(new CheckBox("Sports"), 0, 1);
		tags.addComponent(new CheckBox("Urban"), 1, 1);
		title.setRequired(true);
		title.setRequiredError("Post needs a title");
	}

	@Override
	public PicCritic getUI() {
		return (PicCritic) super.getUI();
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// Show the uploaded file in the image viewer
		confirm.setEnabled(true);
		image.setVisible(true);
		image.setSource(new FileResource(receiver.getFile()));
		image.setSizeFull();
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
			PostServiceInterface service = getUI().postService;
			if (service != null) {
				/*
				 * TODO Set post attributes remember to add filename to post.
				 */
				if (service.createPost(post) != null) {
					Notification.show("Post Uploaded", Type.TRAY_NOTIFICATION);
				}
			}
		} catch (InvalidValueException e) {
			upload.interruptUpload();
			Notification.show(e.getMessage(), Type.WARNING_MESSAGE);
		}
	}
}
