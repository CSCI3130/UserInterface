package com.piccritic.website;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload.*;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;

/*
 * Attempt at the front end for uploading pictures using vaadin
 * Author: rhiannagoguen
 * Took template from https://vaadin.com/docs8/-/part8/framework/components/components-upload.html
 * and from
 * http://demo.vaadin.com/book-examples/book#component.upload.basic
 * Sorry if it's terribly wrong
 */
@Theme("mytheme")
public class UploadPicturesFront extends UI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8544572658091510439L;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
		
		 final Image image = new Image("Uploaded Image");
		 image.setVisible(false);
		
		// Implement both receiver that saves upload in a file and
		// listener for successful upload
		class ImageUploader implements Receiver, SucceededListener {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 2893748293657812L;
			public File file;
	
		    public OutputStream receiveUpload(String filename,
		                                      String mimeType) {
		        // Create and return a file output stream
		    	FileOutputStream fos = null;
		    	try{
		    		file = new File(filename);
		    		fos = new FileOutputStream(file);
		    	} catch(FileNotFoundException e){
		    		//display error somehow
		    		return null;
		    	}
		        return fos;
		    }
	
		    public void uploadSucceeded(SucceededEvent event) {
		        // Show the uploaded file in the image viewer
		        image.setVisible(true);
		    	image.setSource(new FileResource(file));
		    }
		};
		
		ImageUploader receiver = new ImageUploader();
	
		// Create the upload with a caption and set receiver later
		Upload upload = new Upload("Upload Image Here", receiver);
		upload.addSucceededListener(receiver);
		
		setContent(layout);
   		layout.addComponent(upload);
   		layout.addComponent(image);
	
	}
	 
	 @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	    public static class MyUIServlet extends VaadinServlet {

			/**
			 * 
			 */
			private static final long serialVersionUID = 11L;
	    }
}
