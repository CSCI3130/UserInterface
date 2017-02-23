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
import com.piccritic.database.user.UserException;
import com.piccritic.website.PicCritic;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Ryan Lowe<br>
 *         Damien Robichaud
 */
public class UserForm extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	private static final String cancelNotif = "Request canceled.";

	private Map<String, Integer> licenses = new HashMap<String, Integer>();

	private Button save = new Button("Save", this::save);
	private Button cancel = new Button("Cancel", this::cancel);
	private TextField handle = new TextField("Handle");
	private TextField firstName = new TextField("First name");
	private TextField lastName = new TextField("Last name");

	private TextArea bio = new TextArea("Bio");
	private ComboBox license = new ComboBox("License");

	private PasswordField password = new PasswordField("Password");
	private PasswordField confirmPass = new PasswordField("Confirm password");

	private Critic critic;
	boolean newProfile;

	public UserForm(String userHandle) {
		newProfile = (userHandle == null);
		handle.setValue((userHandle == null) ? "" : userHandle);
		critic = PicCritic.userService.select(userHandle);
		if (critic != null) {
			firstName.setValue(critic.getFirstName());
			lastName.setValue(critic.getLastName());
			bio.setValue(critic.getBio());
			license.setValue(critic.getLicenseID());
		} else {
			critic = new Critic();
		}
		handle.setEnabled(newProfile);

		licenses.put("License 1", new Integer(1));
		licenses.put("License 2", new Integer(2));
		licenses.put("License 3", new Integer(3));
		licenses.put("License 4", new Integer(4));
		licenses.put("License 5", new Integer(5));
		licenses.put("License 6", new Integer(6));
		licenses.put("License 7", new Integer(7));
		for (Entry<String, Integer> e : licenses.entrySet()) {
			license.setItemCaption(e.getValue(), e.getKey());
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

		addComponents(handle, firstName, lastName, bio, password, confirmPass, license, buttons);
	}

	public void save(Button.ClickEvent e) {
		saveUser();
		String status;

		if (!password.getValue().equals(confirmPass.getValue())) {
			Notification.show("Passwords do not match", Type.WARNING_MESSAGE);
			return;
		}
		try {
			if (newProfile) {
				status = PicCritic.userService.create(critic, password.getValue());
			} else {
				status = PicCritic.userService.update(critic, password.getValue());
			}
		} catch (UserException ue) {
			Notification.show(ue.getLocalizedMessage(), Type.WARNING_MESSAGE);
			return;
		}

		Notification.show(status, Type.TRAY_NOTIFICATION);
		closeWindow();
	}

	public void saveUser() {
		critic.setHandle(handle.getValue());
		critic.setFirstName(firstName.getValue());
		critic.setLastName(lastName.getValue());
		critic.setBio(bio.getValue());
		// TODO fix
		// critic.setLicenseID((Integer) license.getData());
	}

	public void cancel(Button.ClickEvent e) {
		Notification.show(cancelNotif, Type.TRAY_NOTIFICATION);
		closeWindow();
	}
	
	private void closeWindow() {
		((Window) getParent().getParent()).close();
	}
}
