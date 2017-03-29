package com.piccritic.website.license;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.piccritic.database.license.AttributionLicense;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.LabelElement;

/**
 * This class contains JUnit tests, which are run using Vaadin TestBench 4.
 *
 * To run this, first get an evaluation license from
 * https://vaadin.com/addon/vaadin-testbench and follow the instructions at
 * https://vaadin.com/directory/help/installing-cval-license to install it.
 *
 * Once the license is installed, you can run this class as a JUnit test.
 */
public class LicenseIT extends TestBenchTestCase {
	@Rule
	public ScreenshotOnFailureRule screenshotOnFailureRule =
			new ScreenshotOnFailureRule(this, true);

	@Before
	public void init() throws Exception {
		setDriver(new PhantomJSDriver()); // PhantomJS
	}

	/**
	 * Opens the URL where the application is deployed.
	 */
	private void openTestUrl() {
		getDriver().get("http://localhost:8080/#!licenses/");
	}

	@Test
	public void testShowLicense() throws Exception {
		openTestUrl();
		$(ComboBoxElement.class).caption("License").first()
			.selectByText(new AttributionLicense().toString());
		$(LabelElement.class).caption(AttributionLicense.DATTRIBUTION).exists();
	}
}
