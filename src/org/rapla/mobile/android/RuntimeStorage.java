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

package org.rapla.mobile.android;

import java.util.HashMap;

/**
 * This singleton manages objects across the application runtime.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class RuntimeStorage {

	private static RuntimeStorage instance;
	private HashMap<String, Object> storage;
	public static final String IDENTIFIER_SELECTED_RESERVATION = "selected-reservation";
	
	protected RuntimeStorage() {
		this.storage = new HashMap<String, Object>();
	}
	
	public static RuntimeStorage getInstance() {
		if(instance == null) {
			instance = new RuntimeStorage();
		}
		return instance;
	}
	
	public void store(String key, Object value) {
		this.storage.put(key, value);
	}
	
	public Object retrieve(String key) {
		return this.storage.get(key);
	}
	
	public boolean has(String key) {
		return this.storage.containsKey(key);
	}
}
