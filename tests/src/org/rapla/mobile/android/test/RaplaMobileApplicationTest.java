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
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.RuntimeStorage;
import org.rapla.mobile.android.test.RaplaMobileApplicationTest.InjectableRaplaMobileApplication;
import org.rapla.mobile.android.test.mock.MockRuntimeStorage;

import android.test.ApplicationTestCase;

/**
 * RaplaMobileApplicationTest
 * 
 * Unit tests for <code>org.rapla.mobile.android.RaplaMobileApplication</code>
 * 
 * @see org.rapla.mobile.android.RaplaMobileApplication
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class RaplaMobileApplicationTest extends
		ApplicationTestCase<InjectableRaplaMobileApplication> {

	public RaplaMobileApplicationTest() {
		super(InjectableRaplaMobileApplication.class);
	}

	public void testApplicationShouldInitializePreferencesHandler() {
		this.createApplication();

		try {
			PreferencesHandler.getInstance();
		} catch (RuntimeException e) {
			fail();
		}
	}

	public void testStorageSetShouldSaveObjectWithGivenKeyInRuntimeStorage() {
		// Prepare application
		this.createApplication();
		MockRuntimeStorage storage = new MockRuntimeStorage();
		this.getApplication().setRuntimeStorage(storage);

		Object o = new Object();
		String key = "my-key";
		this.getApplication().storageSet(key, o);

		assertSame(o, storage.storedObject);
		assertEquals(key, storage.storedKey);
	}

	public void testStorageGetShouldReturnCorrespondingObjectFromRuntimeStorage() {
		// Prepare application
		this.createApplication();
		MockRuntimeStorage storage = new MockRuntimeStorage();
		Object o = new Object();
		storage.retrievableObject = o;
		this.getApplication().setRuntimeStorage(storage);

		assertSame(o, this.getApplication().storageGet("my-key"));
	}

	public void testStorageHasShouldReturnValueFromRuntimeStorage() {
		// Prepare application
		this.createApplication();
		MockRuntimeStorage storage = new MockRuntimeStorage();
		storage.hasReturn = true;
		this.getApplication().setRuntimeStorage(storage);

		assertTrue(this.getApplication().storageHas("my-key"));
	}

	/**
	 * InjectableRaplaMobileApplication
	 * 
	 * This class allows for injecting a mock runtime storage instance into the
	 * application.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 */
	public static class InjectableRaplaMobileApplication extends
			RaplaMobileApplication {

		public void setRuntimeStorage(RuntimeStorage storage) {
			this.storage = storage;
		}
	}

}
