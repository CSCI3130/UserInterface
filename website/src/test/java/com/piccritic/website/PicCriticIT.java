package com.piccritic.website;

import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.piccritic.database.user.UserException;
import com.piccritic.compute.user.UserService;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.NotificationElement;

/**
 * @author Damien <br> Francis bosse
 */
public class PicCriticIT extends TestBenchTestCase {
	public UserService userService = UserService.createService();

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
			if (msg.getType().equals("warning")) {
				throw new UserException(msg.getCaption());
			}
		}
	}

	public void createUserUI(String handle) throws UserException {
		$(ButtonElement.class).caption("CreateUser").first().click();
		$(TextFieldElement.class).caption("Handle").first().setValue(handle);
		$(TextFieldElement.class).caption("First name").first().setValue("first");
		$(TextFieldElement.class).caption("Last name").first().setValue("last");
		$(TextFieldElement.class).caption("First name").first().setValue("first");
		$(PasswordFieldElement.class).caption("Password").first().setValue("password");
		$(PasswordFieldElement.class).caption("Confirm password").first().setValue("password");
		$(ButtonElement.class).caption("Save").first().click();

		hasUserException();
	}

	public void loginUserUI(String handle) throws UserException {
		$(ButtonElement.class).caption("Login").first().click();
		$(TextFieldElement.class).caption("Handle").first().setValue(handle);
		$(PasswordFieldElement.class).caption("Password").first().setValue("password");
		$(ButtonElement.class).caption("login").first().click();

		hasUserException();
	}
}
