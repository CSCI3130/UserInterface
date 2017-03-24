package com.piccritic.website;

import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.piccritic.compute.user.UserService;
import com.piccritic.compute.user.UserServiceInterface;
import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.license.JPALicenseConnector;
import com.piccritic.database.user.UserException;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

/**
 * @author Damien <br> Francis bosse
 */
public class PicCriticIT extends TestBenchTestCase {
	public UserServiceInterface userService = UserService.createService();
	public JPALicenseConnector lc = new JPALicenseConnector();

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailureRule =
			new ScreenshotOnFailureRule(this, true);

	@Before
	public void init() throws Exception {
		setDriver(new PhantomJSDriver());
	}

	public void goHome() {
		getDriver().get("http://localhost:8080/");
	}

	public void hasUserException() throws UserException {
		if ($(NotificationElement.class).exists()) {
			NotificationElement msg = $(NotificationElement.class).first();
			if (msg.getType().equals("warning") && !msg.getCaption().isEmpty()) {
				throw new UserException(msg.getCaption());
			}
		}
	}

	public void createUserUI(String handle) throws UserException {
		$(ButtonElement.class).caption("CreateUser").first().click();
		$(TextFieldElement.class).caption("Username").first().setValue(handle);
		$(TextFieldElement.class).caption("First name").first().setValue("first");
		$(TextFieldElement.class).caption("Last name").first().setValue("last");
		$(TextFieldElement.class).caption("First name").first().setValue("first");
		$(PasswordFieldElement.class).caption("Password").first().setValue("password");
		$(PasswordFieldElement.class).caption("Confirm password").first().setValue("password");
		ButtonElement save = $(ButtonElement.class).caption("Save").first();
		$(ComboBoxElement.class).caption("License").first()
			.selectByText(new AttributionLicense().toString());
		save.click();

		hasUserException();
	}

	public void loginUserUI(String handle) throws UserException {
		$(ButtonElement.class).caption("Login").first().click();
		$(TextFieldElement.class).caption("Username").first().setValue(handle);
		$(PasswordFieldElement.class).caption("Password").first().setValue("password");
		$(ButtonElement.class).caption("login").first().click();

		hasUserException();
	}
}
