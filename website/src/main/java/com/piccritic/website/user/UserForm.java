/**
 * UserForm.java
 * Created Feb 16, 2017
 */
package com.piccritic.website.user;

import com.piccritic.compute.MasterService;
import com.piccritic.database.user.Critic;
import com.piccritic.database.user.UserException;
import com.piccritic.website.PicCritic;
import com.piccritic.website.license.LicenseChooser;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Create User form
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
	private static final String passwordMatch = "Passwords do not match";
	private static final String passwordLength = "Password must be at least 8 characters";

	private Button save = new Button("Save", this::save);
	private Button cancel = new Button("Cancel", this::cancel);
	private TextField handle = new TextField("Username");
	private TextField firstName = new TextField("First name");
	private TextField lastName = new TextField("Last name");

	private TextArea bio = new TextArea("Bio");
	private LicenseChooser license = new LicenseChooser();

	private PasswordField password = new PasswordField("Password");
	private PasswordField confirmPass = new PasswordField("Confirm password");

	private Critic critic;
	boolean newProfile;

	public UserForm(String userHandle) {
		newProfile = (userHandle == null);
		handle.setValue((userHandle == null) ? "" : userHandle);
		critic = MasterService.userService.select(userHandle);
		if (critic != null) {
			firstName.setValue(critic.getFirstName());
			lastName.setValue(critic.getLastName());
			bio.setValue(critic.getBio());
		} else {
			critic = new Critic();
			handle.setRequired(true);
			firstName.setRequired(true);
			lastName.setRequired(true);
			password.setRequired(true);
			confirmPass.setRequired(true);
		}
		handle.setEnabled(newProfile);

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
			Notification.show(passwordMatch, Type.WARNING_MESSAGE);
			return;
		}

		if ((!newProfile && !password.getValue().isEmpty()) && password.getValue().length() < 8) {
			Notification.show(passwordLength, Type.WARNING_MESSAGE);
			return;
		}

		try {
			if (newProfile) {
				status = MasterService.userService.create(critic, password.getValue());
			} else {
				status = MasterService.userService.update(critic, password.getValue());
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
		if (license.getValue() != null) {
			critic.setLicense(license.getValue());
		}
	}

	public void cancel(Button.ClickEvent e) {
		Notification.show(cancelNotif, Type.TRAY_NOTIFICATION);
		closeWindow();
	}
	
	private void closeWindow() {
		((Window) getParent().getParent()).close();
	}
}
