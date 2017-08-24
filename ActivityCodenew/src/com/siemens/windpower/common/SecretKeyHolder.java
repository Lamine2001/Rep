package com.siemens.windpower.common;

import javax.crypto.SecretKey;

public class SecretKeyHolder {
	// ~ Static fields/initializers
	// -----------------------------------------------------------------

	private static SecretKey s_secretKey;

	// ~ Methods
	// ------------------------------------------------------------------------------------

	/**
	 * Store the secret key used to send a SOAP message
	 *
	 * Relies on a standard Request/Response pattern
	 */
	public static void setSecretKey(SecretKey key) {
		s_secretKey = key;
	}

	/**
	 * Returns the last secret key used to send a SOAP message.
	 *
	 * Relies on a standard Request/Response pattern
	 */
	public static SecretKey getSecretKey() {
		return s_secretKey;
	}
}
