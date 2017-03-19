/**
 * License Connector interface.
 *
 * used for the implementation of a database connector.
 */
package com.piccritic.database.license;

public interface LicenseConnector {

	/**
	 * this method returns a license object given the short name.
	 * @param licenseType String holding license name
	 * @return License object
	 */
	public License selectLicense(String licenseType);

}
