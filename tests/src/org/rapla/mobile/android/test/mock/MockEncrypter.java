/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Maximilian Lenkeit                                    |
 |                                                                          |
 | This program is free software; you can redistribute it and/or modify     |
 | it under the terms of the GNU General Public License as published by the |
 | Free Software Foundation. A copy of the license has been included with   |
 | these distribution in the COPYING file, if not go to www.fsf.org         |
 |                                                                          |
 | As a special exception, you are granted the permissions to link this     |
 | program with every library, which license fulfills the Open Source       |
 | Definition as published by the Open Source Initiative (OSI).             |
 *--------------------------------------------------------------------------*/

package org.rapla.mobile.android.test.mock;

import org.rapla.mobile.android.utility.Encrypter;

/**
 * MockEncrypter
 * 
 * Mock class for encrypter logic, just returns the input.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockEncrypter extends Encrypter {

	public int numEncryption = 0;
	public int numDecryption = 0;
	public boolean secretKeySet = false;
	public boolean encryptThrowsException = false;
	public boolean decryptThrowsException = false;

	public String encrypt(String text) throws Exception {
		this.numEncryption++;
		if (this.encryptThrowsException) {
			throw new Exception();
		}
		return text;
	}

	public String decrypt(String code) throws Exception {
		this.numDecryption++;
		if (this.decryptThrowsException) {
			throw new Exception();
		}
		return code;
	}

	public void setSecretKey(String secretKey) {
		this.secretKeySet = true;
	}

}
