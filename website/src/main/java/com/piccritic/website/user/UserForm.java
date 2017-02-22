/**
 * UserForm.java
 * Created Feb 16, 2017
 */
package com.piccritic.website.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.piccritic.compute.user.UserService;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.UserConnector;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author Ryan Lowe<br> Damien Robichaud
 */
public class UserForm extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * 
	 */
  	private static final String[] notifications = {
      	"Creating profile!",
    	"Profile successfully created!",
      	"Profile successfully updated!",
      	"Profile could not be created.",
      	"Profile could not be updated.",
        "Password too short; must be at least 10 characters.",
      	"Handle already in use."
    };
  
  	private static final String cancelNotif = "Request canceled.";
  
  	Map<String, Integer> licenses = new HashMap<String,Integer>();
  
	Button save = new Button("Save", this::save);
	Button cancel = new Button("Cancel", this::cancel);
	TextField handle = new TextField("Handle");
	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");
  	
	TextArea bio = new TextArea("Bio");
  	ComboBox license = new ComboBox();
  
  	PasswordField password = new PasswordField("Password");
  	PasswordField confirmPass = new PasswordField("Confirm password");
	
  	UserService service;
  	Critic critic;
  	boolean newProfile;
  
  	public UserForm(String userHandle) {
      	newProfile = (userHandle == null);
      	handle.setValue(userHandle);
    	handle.setEnabled(newProfile);
      	
      	licenses.put("License 1", new Integer(1));
      	licenses.put("License 2", new Integer(2));
      	licenses.put("License 3", new Integer(3));
      	licenses.put("License 4", new Integer(4));
      	licenses.put("License 5", new Integer(5));
      	licenses.put("License 6", new Integer(6));
      	licenses.put("License 7", new Integer(7));
      	for(Entry<String, Integer> e : licenses.entrySet()) {
          	license.setItemCaption(e.getValue(), e.getKey());
          	license.setValue(e.getValue());
        }
      	
      	configureComponents();
      	buildLayout();
    }

  	public void configureComponents() {
      	save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    }
  
  	public void buildLayout() {
    	setSizeFull();
    	setMargin(true);
      	HorizontalLayout buttons = new HorizontalLayout(save, cancel);
      	buttons.setSpacing(true);
      
      	addComponents(handle, firstName, lastName, bio, password, confirmPass, buttons);
    }
    	
  
  	public void save(Button.ClickEvent e) {
      	saveUser();
     	String status;

        // TODO check that passwords match
        if (newProfile) {
    		status = service.create(critic, password.getValue());
        } else {
          	status = service.update(critic, password.getValue());
        }
      	
      	Notification.show(status, Type.TRAY_NOTIFICATION);
    }
  
  	public void saveUser() {
  		critic.setHandle(handle.getValue());
      	critic.setFirstName(firstName.getValue());
  		critic.setLastName(lastName.getValue());
      	critic.setBio(bio.getValue());
      	critic.setLicenseID((Integer) license.getValue());
  	}
  	
  	public void cancel(Button.ClickEvent e) {
    	Notification.show(cancelNotif, Type.TRAY_NOTIFICATION);
      	setVisible(false);
    }
}
