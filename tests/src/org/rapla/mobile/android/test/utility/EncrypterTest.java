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

package org.rapla.mobile.android.test.utility;

import org.rapla.mobile.android.utility.Encrypter;

import android.test.AndroidTestCase;

/**
 * EncrypterTest
 * 
 * Unit test class for <codeorg.rapla.mobile.android.utility.Encrypter</code>.
 * 
 * @see org.rapla.mobile.android.utility.Encrypter
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class EncrypterTest extends AndroidTestCase {

	protected Encrypter encrypter;

	protected void setUp() throws Exception {
		super.setUp();
		encrypter = new Encrypter();
	}

	/**
	 * Scenario: encrypt password, decrypt password, compare
	 * 
	 * @throws Exception
	 */
	public void testIngetrationScenarioEncryptAndDecrypt() throws Exception {
		String secretKey = "0001020304050607";
		String plain = "00112233445566778899aabbccddeeff";
		this.encrypter.setSecretKey(secretKey);

		// Encrypt
		String encrypted = this.encrypter.encrypt(plain);
		assertFalse(plain.equals(encrypted));

		// Decrypt
		String decrypted = this.encrypter.decrypt(encrypted);
		assertEquals(plain, decrypted);
	}

	public void testIntegrationScenarioBytesToHexAndHexToBytes() {
		byte[] bytes = new byte[] { (byte) 0x32, (byte) 0xaf, (byte) 0xa4 };
		String hex = this.encrypter.bytesToHex(bytes);
		byte[] result = this.encrypter.hexToBytes(hex);
		// Assertion with string conversion as otherwise, the pointer ids would
		// be compared which are not the same
		assertEquals(new String(bytes), new String(result));
	}

	public void testIngetrationScenarioHexToBytesAndBytesToHey() {
		String hex = "123456";
		byte[] bytes = this.encrypter.hexToBytes(hex);
		String backHex = this.encrypter.bytesToHex(bytes);
		assertEquals(hex, backHex);
	}

	public void testBytesToHexShouldConvertByteArrayToHexString() {
		byte[] bytes = new byte[] { (byte) 0x12, (byte) 0xff, (byte) 0xa4 };
		String expected = "12ffa4";
		assertEquals(expected, this.encrypter.bytesToHex(bytes));
	}

	public void testHexToBytesShouldConvertHexStringToByteArray() {
		String hex = "12ffa4";
		byte[] expected = new byte[] { (byte) 0x12, (byte) 0xff, (byte) 0xa4 };
		// Assertion with string conversion as otherwise, the pointer ids would
		// be compared which are not the same
		assertEquals(new String(expected),
				new String(this.encrypter.hexToBytes(hex)));
	}

	public void testEncryptShouldThrowExceptionIfStringIsNull() {
		try {
			this.encrypter.encrypt(null);
			fail();
		} catch (Exception e) {
			// Expected
		}
	}

	public void testDecryptShouldThrowExceptionIfStringIsNull() {
		try {
			this.encrypter.decrypt(null);
			fail();
		} catch (Exception e) {
			// Expected
		}
	}

	public void testConstructorAcceptStringAsSecretKey() {
		Encrypter e = new Encrypter("$ecretKey");
		assertNotNull(e);
	}
}
