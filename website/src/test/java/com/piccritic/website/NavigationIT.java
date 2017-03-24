package com.piccritic.website;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.testbench.elements.ButtonElement;

/**
 * This class contains JUnit tests, which are run using Vaadin TestBench 4.
 *
 * To run this, first get an evaluation license from
 * https://vaadin.com/addon/vaadin-testbench and follow the instructions at
 * https://vaadin.com/directory/help/installing-cval-license to install it.
 *
 * Once the license is installed, you can run this class as a JUnit test.
 */
public class NavigationIT extends PicCriticIT {

	@Before
	public void init() throws Exception {
		super.init();
	}

	@Test
	public void testNavigation() {
		goHome();

		assertTrue($(ButtonElement.class).caption("Home").exists());
		assertTrue($(ButtonElement.class).caption("Login").exists());
		assertTrue($(ButtonElement.class).caption("CreateUser").exists());
		$(ButtonElement.class).caption("Home").first().click();
	}
}
