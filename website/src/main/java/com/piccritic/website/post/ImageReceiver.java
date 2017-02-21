package com.piccritic.website.post;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.piccritic.website.PicCritic;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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

	private PicCritic ui;
	private String handle;

	/**
	 * Constructs the ImageReceiver object that requests a file from the
	 * back-end to allow the storage of a picture
	 * 
	 * @param ui
	 * @param handle
	 */
	public ImageReceiver(PicCritic ui, String handle) {
		this.ui = ui;
		this.handle = handle;
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		if (ui == null) {
			return null;
		}

		if (ui.postService == null) {
			return null;
		}

		file = ui.postService.getImageFile(handle);
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
