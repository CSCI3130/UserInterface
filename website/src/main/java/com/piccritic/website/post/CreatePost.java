package com.piccritic.website.post;

import java.util.HashSet;
import java.util.Set;

import com.piccritic.compute.post.PostService;
import com.piccritic.database.post.Album;
import com.piccritic.database.post.AlbumException;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostException;
import com.piccritic.website.license.LicenseChooser;
import com.piccritic.website.login.LoginService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
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
import com.vaadin.ui.Upload.ChangeListener;
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
	private LicenseChooser license = new LicenseChooser();
	private Upload upload;
	private GridLayout tags = new GridLayout(2, 2);
	private Post post = new Post();
	private ImageReceiver receiver;
	private Image image = new Image("Uploaded Image");
	private Button confirm = new Button("Confirm", this::confirmUpload);
	private PostService service = new PostService();
	private String handle;
	private FormLayout form = new FormLayout();

	/**
	 * This window is responsible for creating a post form with an @see Upload
	 * component a post will only be uploaded if a picture, a title and a
	 * description.
	 * 
	 * @param handle user that is creating the post
	 */
	public CreatePost(String handle) {
		this.handle = handle;
		setSizeFull();
		setModal(true);
		Panel layout = new Panel();
		layout.setSizeFull();
		setContent(layout);
		layout.setContent(form);
		if (handle == null) {
			close();
		}

		image.setVisible(false);
		image.setSizeFull();
		image.setHeightUndefined();

		form.setMargin(true);
		form.addComponent(title);
		form.addComponent(image);
		form.addComponent(description);
		form.addComponent(tags);
		form.addComponent(license);
		form.getComponent(0);
		title.setSizeFull();
		description.setSizeFull();

		confirm.setEnabled(false);
		description.setRequired(true);
		tags.addComponent(new CheckBox("Nature"), 0, 0);
		tags.addComponent(new CheckBox("People"), 1, 0);
		tags.addComponent(new CheckBox("Sports"), 0, 1);
		tags.addComponent(new CheckBox("Urban"), 1, 1);
		title.setRequired(true);
		setupImagereceiver();
	}
	
	public void setupImagereceiver() {
		if (upload != null) {
			form.removeComponent(upload);
		}
		receiver = new ImageReceiver(handle, post);
		upload = new Upload("Upload Image Here", receiver);
		upload.addSucceededListener(this);

		upload.addStartedListener(e -> {
			confirm.setEnabled(false);
		});

		upload.setButtonCaption(null);
		upload.addChangeListener(e -> {
			if (e.getFilename() != null) {
				upload.setButtonCaption("Upload");
			}
		});

		form.addComponent(upload);
		form.addComponent(confirm);
	}
	
	public CreatePost(String handle, Post post) {
		this(handle);
		if (post != null) {
			this.post = post;
			title.setValue(post.getTitle());
			description.setValue(post.getDescription());
			setupImagereceiver();
			image.setSource(new FileResource(receiver.getFile()));
			image.setVisible(true);
			confirm.setEnabled(true);
		}
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// Show the uploaded file in the image viewer
		confirm.setEnabled(true);
		image.setVisible(true);
		image.setSource(new FileResource(receiver.getFile()));
		Notification.show("Image Saved", Type.TRAY_NOTIFICATION);
		upload.setButtonCaption(null);
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
			post.setDescription(description.getValue());
			post.setTitle(title.getValue());
			if (service != null) {
				Album defaultAlbum = service.getDefaultAlbum(LoginService.getHandle());
				post.setAlbum(defaultAlbum);
				Post created = service.createPost(post);

				Set<Post> posts = new HashSet<>();
				posts.add(post);
				defaultAlbum.setPosts(posts);
				service.updateAlbum(post.getAlbum());

				if (created != null) {
					Notification.show("Post Uploaded", Type.TRAY_NOTIFICATION);
					close();
					String postLocation = "#!post/" + post.getPath();
					UI.getCurrent().getPage().setLocation(postLocation);
					UI.getCurrent().getPage().reload();
				}
			}
			
			Notification.show("Could not create post.", Type.WARNING_MESSAGE);

		} catch (PostException|AlbumException e) {
			upload.interruptUpload();
			Notification.show(e.getMessage(), Type.WARNING_MESSAGE);
		}
		
	}
}
