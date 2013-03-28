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

import org.rapla.mobile.android.RuntimeStorage;

import android.test.AndroidTestCase;

/**
 * RuntimeStorageTest
 * 
 * Unit test class for org.rapla.modile.android.RuntimeStorage
 * 
 * @see org.rapla.mobile.android.RuntimeStorage
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class RuntimeStorageTest extends AndroidTestCase {

	protected RuntimeStorage storage;

	protected void setUp() throws Exception {
		super.setUp();

		// Initialize runtime storage
		storage = RuntimeStorage.getInstance();
	}

	public void testGetInstanceShouldAlwaysReturnSameInstance() {
		RuntimeStorage local = RuntimeStorage.getInstance();
		assertEquals(storage, local);
	}
	
	public void testHasShouldReturnFalseIfKeyDoesNotExist() {
		assertFalse(storage.has("someRandomKey"));
	}
	
	public void testHasShouldReturnTrueIfKeyExists() {
		String key = "myKey";
		storage.store(key, new Object());
		assertTrue(storage.has(key));
	}
	
	public void testIntegrationScenarioStoreValueAndRetrieveValueAndCompare() {
		String key = "myKey";
		String value = "myValue";
		
		storage.store(key, value);
		String retrievedValue = (String)storage.retrieve(key);
		
		assertEquals(value, retrievedValue);
	}

}
