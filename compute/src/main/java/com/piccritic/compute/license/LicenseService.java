package com.piccritic.compute.license;

import com.piccritic.compute.MasterConnector;
import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.license.AttributionNoDerivsLicense;
import com.piccritic.database.license.AttributionNonComNDerivsLicense;
import com.piccritic.database.license.AttributionNonComShareLicense;
import com.piccritic.database.license.AttributionNonCommercialLicense;
import com.piccritic.database.license.AttributionShareAlikeLicense;
import com.piccritic.database.license.JPALicenseConnector;
import com.piccritic.database.license.License;
import com.piccritic.database.license.LicenseConnector;

public class LicenseService implements LicenseServiceInterface {

	public LicenseConnector lc;

	public LicenseService() {
		MasterConnector.init();
		lc = MasterConnector.licenseConnector;
	}
	
	@Override
	public AttributionLicense makeAttribution() {
		return (AttributionLicense) lc.selectLicense(License.ATTRIBUTION);
	}

	@Override
	public AttributionShareAlikeLicense makeAttributionShareAlike() {
		return (AttributionShareAlikeLicense) lc.selectLicense(License.ATTRIBUTION_SHAREALIKE);
	}

	@Override
	public AttributionNoDerivsLicense makeAttributionNoDerivs() {
		return (AttributionNoDerivsLicense) lc.selectLicense(License.ATTRIBUTION_NO_DERIVS);
	}

	@Override
	public AttributionNonCommercialLicense makeAttributionNonCommercial() {
		return (AttributionNonCommercialLicense) lc.selectLicense(License.ATTRIBUTION_NON_COMMERCIAL);
	}

	@Override
	public AttributionNonComShareLicense makeAttributionNonComShare() {
		return (AttributionNonComShareLicense) lc.selectLicense(License.ATTRIBUTION_NON_COMMERCIAL_SHAREALIKE);
	}

	@Override
	public AttributionNonComNDerivsLicense makeAttributionNonComNDerivsLicense() {
		return (AttributionNonComNDerivsLicense) lc.selectLicense(License.ATTRIBUTION_NON_COMMERCIAL_NO_DERIVS);
	}
	
	@Override
	public License FetchLicenseInfo(String shortName) {
		return lc.selectLicense(shortName);
	}

}
