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

import org.rapla.mobile.android.utility.Encrypter;

import android.app.Application;

/**
 * RaplaMobileApplication
 * 
 * This class is the enhances the standard android application functionality by
 * providing an application-wide storage mechanism.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class RaplaMobileApplication extends Application {

	protected RuntimeStorage storage;

	/**
	 * This constant should only be used during development. It allows for using
	 * the <code>DummyHomeActvity</code> instead of the browser integration for
	 * selecting an appointment and entering its edit mode.
	 */
	public static final boolean USE_DEMO_HOME = false;

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize runtime storage
		this.storage = RuntimeStorage.getInstance();

		// Initialize preferences handler
		PreferencesHandler.getInstance(this.getApplicationContext(),
				new Encrypter());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void storageSet(String key, Object value) {
		this.storage.store(key, value);
	}

	public Object storageGet(String key) {
		return this.storage.retrieve(key);
	}

	public boolean storageHas(String key) {
		return this.storage.has(key);
	}
}
