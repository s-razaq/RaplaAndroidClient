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

package org.rapla.mobile.android.test;

import org.rapla.mobile.android.PreferencesHandler;
import org.rapla.mobile.android.test.mock.MockContext;
import org.rapla.mobile.android.test.mock.MockEncrypter;
import org.rapla.mobile.android.test.mock.MockSharedPreferences;

import android.test.AndroidTestCase;

/**
 * PreferencesHandlerTest
 * 
 * Unit test class for org.rapla.modile.android.PreferencesHandler
 * 
 * @see org.rapla.mobile.android.PreferencesHandler
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class PreferencesHandlerTest extends AndroidTestCase {

	protected PreferencesHandler preferences;
	protected MockSharedPreferences mockPreferences;
	protected MockContext context;
	protected MockEncrypter encrypter;

	protected void setUp() throws Exception {
		super.setUp();

		// Initialize mock instances
		context = new MockContext();
		mockPreferences = new MockSharedPreferences();
		encrypter = new MockEncrypter();
		context.setSharedPreferences(mockPreferences);

		// Check pre conditions
		assertEquals(mockPreferences,
				context.getSharedPreferences("anyname", 0));

		// Reset preferences handler singleton
		PreferencesHandlerInjector.resetSingleton();

		// Set preferences handler
		this.preferences = PreferencesHandler.getInstance(context, encrypter);
	}

	public void testGetInstanceShouldAlwaysReturnSameInstance() {
		PreferencesHandler local = PreferencesHandler.getInstance();
		assertEquals(preferences, local);
	}

	public void testConstructorShouldInitializeSecretKeyWithRandomMd5Hash() {
		// Check whether secret key was set
		assertTrue(this.mockPreferences.secretKeySet);

		// Check secret key to be 16 hex chars
		assertEquals(32, this.mockPreferences.secretKey.length());
	}

	public void testConstructorShouldSetSecretKeyInEncrypter() {
		assertTrue(this.encrypter.secretKeySet);
	}

	public void testSetPasswordShouldEncryptAndStorePassword() {
		int numEncryptionsBefore = this.encrypter.numEncryption;

		// Set password
		String password = "myPassword123";
		this.preferences.setPassword(password);

		int numEncryptionsAfter = this.encrypter.numEncryption;

		// Check whether password was set
		assertTrue(this.mockPreferences.passwordSet);

		// Check whether encrypt was called
		assertEquals(numEncryptionsBefore + 1, numEncryptionsAfter);
	}

	public void testGetPasswordShouldDecryptPassword() {
		int numDecryptionsBefore = this.encrypter.numDecryption;

		// Set password in mock preferences
		String password = "1234567890abcdef";
		this.mockPreferences.passwordSet = true;
		this.mockPreferences.password = password;

		// Get password and check counter
		this.preferences.getPassword();
		int numDecryptionsAfter = this.encrypter.numDecryption;

		assertEquals(numDecryptionsBefore + 1, numDecryptionsAfter);
	}

	public void testGetUsernameShouldReturnUsernameFromSharedPreferences() {
		this.mockPreferences.username = "my-username";

		assertEquals(this.mockPreferences.username,
				this.preferences.getUsername());
	}

	public void testGetHostShouldReturnHostFromSharedPreferences() {
		this.mockPreferences.host = "127.0.0.1";

		assertEquals(this.mockPreferences.host, this.preferences.getHost());
	}

	public void testGetHostPortShouldReturnPortFromSharedPreferences() {
		this.mockPreferences.hostPort = 8052;

		assertEquals(this.mockPreferences.hostPort,
				this.preferences.getHostPort());
	}

	public void testHasUsernameShouldQuerySharedPreferences() {
		this.mockPreferences.containsReturn = true;

		boolean result = this.preferences.hasUsername();

		assertEquals(this.mockPreferences.containsReturn, result);
		assertEquals(PreferencesHandler.KEY_USERNAME,
				this.mockPreferences.containsKey);
	}

	public void testHasPasswordShouldQuerySharedPreferences() {
		this.mockPreferences.containsReturn = true;

		boolean result = this.preferences.hasPassword();

		assertEquals(this.mockPreferences.containsReturn, result);
		assertEquals(PreferencesHandler.KEY_PASSWORD,
				this.mockPreferences.containsKey);
	}

	public void testHasHostShouldQuerySharedPreferences() {
		this.mockPreferences.containsReturn = true;

		boolean result = this.preferences.hasHost();

		assertEquals(this.mockPreferences.containsReturn, result);
		assertEquals(PreferencesHandler.KEY_HOST,
				this.mockPreferences.containsKey);
	}

	public void testHasHostPortShouldQuerySharedPreferences() {
		this.mockPreferences.containsReturn = true;

		boolean result = this.preferences.hasHostPort();

		assertEquals(this.mockPreferences.containsReturn, result);
		assertEquals(PreferencesHandler.KEY_HOST_POST,
				this.mockPreferences.containsKey);
	}

	public void testHasConnectionPreferencesShouldReturnTrueIfUsernameAndPasswordAndHostAndHostPortExist() {
		this.mockPreferences.containsReturn = true;

		assertTrue(this.preferences.hasConnectionPreferences());
	}

	/**
	 * Just to have code coverage on the protected constructor of the
	 * <code>PreferencesHandler</code> class.
	 */
	public void testConstructorOfInjector() {
		PreferencesHandlerInjector injector = new PreferencesHandlerInjector();
		assertNotNull(injector);
	}

	public void testGetInstanceShouldThrowExceptionIfPreferencesHandlerHasNotBeenInitialized() {
		// Reset singleton
		PreferencesHandlerInjector.resetSingleton();

		try {
			PreferencesHandler.getInstance();
			fail(); // Expected runtime exception
		} catch (RuntimeException e) {
			// Expected
		}
	}

	public void testGetPasswordShouldThrowRuntimeExceptionIfDecryptFails() {
		this.encrypter.decryptThrowsException = true;

		try {
			this.preferences.getPassword();
			fail(); // Expected exception
		} catch (RuntimeException e) {
			// expected
		}
	}

	public void testSetPasswordShouldThrowRuntimeExceptionIfEncryptFails() {
		this.encrypter.encryptThrowsException = true;

		try {
			this.preferences.setPassword("random");
			fail(); // Expected exception
		} catch (RuntimeException e) {
			// expected
		}
	}

	/**
	 * PreferencesHandlerInjector
	 * 
	 * This class is to be used to reset the singleton
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private static class PreferencesHandlerInjector extends PreferencesHandler {

		private PreferencesHandlerInjector() {
			super();
		}

		public static void resetSingleton() {
			PreferencesHandler.instance = null;
		}
	}

}
