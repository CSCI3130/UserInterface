package com.piccritic.database.license;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LicenseConnectorTest {

	License attribution = new AttributionLicense();
	License attrShare = new AttributionShareAlikeLicense();
	License attrNoDerivs = new AttributionNoDerivsLicense();
	License attrNonCom = new AttributionNonCommercialLicense();
	License attrNonComShare = new AttributionNonComShareLicence();
	License attrNonComNoDerivs = new AttributionNonComNDerivsLicense();
	LicenseConnector connector = new JPALicenseConnector();

	@Test
	public void testSelectLicense() {
		assertEquals(attribution, connector.selectLicense(attribution.getLicenseType()));
		assertEquals(attrShare, connector.selectLicense(attrShare.getLicenseType()));
		assertEquals(attrNoDerivs, connector.selectLicense(attrNoDerivs.getLicenseType()));
		assertEquals(attrNonCom, connector.selectLicense(attrNonCom.getLicenseType()));
		assertEquals(attrNonComShare, connector.selectLicense(attrNonComShare.getLicenseType()));
		assertEquals(attrNonComNoDerivs, connector.selectLicense(attrNonComNoDerivs.getLicenseType()));
	}
}
