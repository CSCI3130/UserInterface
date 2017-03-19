package com.piccritic.compute.license;

import com.piccritic.database.license.AttributionLicense;
import com.piccritic.database.license.AttributionNoDerivsLicense;
import com.piccritic.database.license.AttributionNonComNDerivsLicense;
import com.piccritic.database.license.AttributionNonComShareLicense;
import com.piccritic.database.license.AttributionNonCommercialLicense;
import com.piccritic.database.license.AttributionShareAlikeLicense;
import com.piccritic.database.license.License;

public interface LicenseServiceInterface {
	
	public AttributionLicense makeAttribution();
	
	public AttributionShareAlikeLicense makeAttributionShareAlike();
	
	public AttributionNoDerivsLicense makeAttributionNoDerivs();
	
	public AttributionNonCommercialLicense makeAttributionNonCommercial();
	
	public AttributionNonComShareLicense makeAttributionNonComShare();
	
	public AttributionNonComNDerivsLicense makeAttributionNonComNDerivsLicense();

	public License FetchLicenseInfo(String shortName);

	
}
