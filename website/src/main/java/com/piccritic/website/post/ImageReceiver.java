package com.piccritic.website.post;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import com.piccritic.database.post.Post;
import com.piccritic.website.PicCritic;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;

/**
 * This object gets the required information from the back-end to allow the
 * storage of images
 * 
 * @author Damien Robichaud <br>
 *         Francis Bosse
 *
 */
class ImageReceiver implements Receiver {

	private static final long serialVersionUID = 2893748293657812L;
	private File file;

	private String handle;
	private Post post;
	private PicCritic ui;

	/**
	 * Constructs the ImageReceiver object that requests a file from the
	 * back-end to allow the storage of a picture
	 * 
	 * @param ui
	 * @param post
	 */
	public ImageReceiver(String handle, Post post) {
		this.handle = handle;
		this.post = post;
		ui = (PicCritic) UI.getCurrent();
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		if (ui == null) {
			return null;
		}

		if (ui.postService == null) {
			return null;
		}

		System.out.println(post.getPath());
		if (post != null && post.getPath() != null) {
			file = new File(post.getPath());
		} else {
			file = ui.postService.getImageFile(handle);
			post.setPath(file.getPath().replaceAll("\\\\", "/"));
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			Notification.show("Error Creating File", Type.WARNING_MESSAGE);
			return null;
		}

		return fos;
	}

	/**
	 * getFile returns the file used to store the image
	 * 
	 * @return File object used to store image
	 */
	public File getFile() {
		return file;
	}
}
