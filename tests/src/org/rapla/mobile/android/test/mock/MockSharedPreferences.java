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

import java.util.Map;

import org.rapla.mobile.android.PreferencesHandler;

/**
 * MockSharedPreferences
 * 
 * Mocked class for both interfaces SharedPreferences and
 * SharesPreferences.Editor. It only mocks the functionality covered by existing
 * unit tests. For new unit tests, make sure to check the class's functionality.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockSharedPreferences implements
		android.content.SharedPreferences,
		android.content.SharedPreferences.Editor {

	public String secretKey = null;
	public boolean secretKeySet = false;

	public String password = null;
	public boolean passwordSet = false;

	public String username;

	public String host;

	public int hostPort;

	public boolean committed = false;

	public boolean containsReturn = false;
	public String containsKey;

	public boolean contains(String key) {
		this.containsKey = key;
		return containsReturn;
	}

	public Editor edit() {
		return this;
	}

	public Map<String, ?> getAll() {
		throw new RuntimeException("Not mocked");
	}

	public boolean getBoolean(String key, boolean defValue) {
		throw new RuntimeException("Not mocked");
	}

	public float getFloat(String key, float defValue) {
		throw new RuntimeException("Not mocked");
	}

	public int getInt(String key, int defValue) {
		if (key.equals(PreferencesHandler.KEY_HOST_POST)) {
			return this.hostPort;
		} else {
			return 0;
		}
	}

	public long getLong(String key, long defValue) {
		throw new RuntimeException("Not mocked");
	}

	public String getString(String key, String defValue) {
		if (key.equals(PreferencesHandler.KEY_SECRET_KEY)) {
			if (secretKey == null) {
				return defValue;
			} else {
				return secretKey;
			}
		} else if (key.equals(PreferencesHandler.KEY_PASSWORD)) {
			if (password == null) {
				return defValue;
			} else {
				return password;
			}
		} else if (key.equals(PreferencesHandler.KEY_HOST)) {
			if (this.host == null) {
				return defValue;
			} else {
				return this.host;
			}
		} else if (key.equals(PreferencesHandler.KEY_USERNAME)) {
			if (this.username == null) {
				return defValue;
			} else {
				return this.username;
			}
		} else {
			return "";
		}
	}

	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		throw new RuntimeException("Not mocked");

	}

	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		throw new RuntimeException("Not mocked");

	}

	public Editor clear() {
		return this;
	}

	public boolean commit() {
		committed = true;
		return true;
	}

	public Editor putBoolean(String key, boolean value) {
		return this;
	}

	public Editor putFloat(String key, float value) {
		return this;
	}

	public Editor putInt(String key, int value) {
		return this;
	}

	public Editor putLong(String key, long value) {
		return this;
	}

	public Editor putString(String key, String value) {
		if (key.equals(PreferencesHandler.KEY_SECRET_KEY)) {
			secretKey = value;
			secretKeySet = true;
		} else if (key.equals(PreferencesHandler.KEY_PASSWORD)) {
			password = value;
			passwordSet = true;
		}
		return this;
	}

	public Editor remove(String key) {
		return this;
	}

}
