package com.piccritic.website.post;

import org.junit.Before;
import org.junit.Test;

import com.piccritic.database.user.UserException;
import com.piccritic.website.PicCriticIT;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextAreaElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.WindowElement;

/**
 * This class contains JUnit tests, which are run using Vaadin TestBench 4.
 *
 * To run this, first get an evaluation license from
 * https://vaadin.com/addon/vaadin-testbench and follow the instructions at
 * https://vaadin.com/directory/help/installing-cval-license to install it.
 *
 * Once the license is installed, you can run this class as a JUnit test.
 */
public class ViewPostIT extends PicCriticIT {

	@Before
	public void init() throws Exception {
		super.init();
	}

    /**
     * Opens the URL where the application is deployed.
     */
    private void openTestUrl() {
        getDriver().get("http://localhost:8080/");
    }

	@Test
	public void CreatePost() throws UserException {
		openTestUrl();
		String user = "Username";

		if (userService.select(user) == null) {
			createUserUI(user);
		}

		loginUserUI(user);

		$(ButtonElement.class).caption("Create Post").first().click();
		$(TextFieldElement.class).caption("Post Title").first().setValue("title");
		$(TextAreaElement.class).caption("Post Description").first().setValue("D");
		/* there's no way we can select a file to upload from a headless
		 * machine
		 */
		$(WindowElement.class).first().close();
	}
}
