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

import java.util.HashMap;
import java.util.Locale;

import org.rapla.entities.Entity;
import org.rapla.entities.EntityNotFoundException;
import org.rapla.entities.RaplaObject;
import org.rapla.entities.RaplaType;
import org.rapla.entities.User;
import org.rapla.entities.configuration.Preferences;
import org.rapla.entities.configuration.RaplaMap;
import org.rapla.entities.configuration.internal.PreferencesImpl;
import org.rapla.entities.configuration.internal.RaplaMapImpl;
import org.rapla.entities.storage.RefEntity;
import org.rapla.plugin.autoexport.AutoExportPlugin;

/**
 * MockRaplaPreferences
 * 
 * Mocked rapla preferences interface
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockRaplaPreferences implements Preferences {
	
	public boolean getEntryThrowsException = false;

	public RaplaMap userCalendars;

	public boolean isIdentical(Entity id2) {

		return false;
	}

	public boolean isPersistant() {

		return false;
	}

	public RaplaType getRaplaType() {

		return null;
	}

	public void setOwner(User owner) {

	}

	public User getOwner() {

		return null;
	}

	public String getName(Locale locale) {

		return null;
	}

	public void putEntry(String role, RaplaObject entry) {

	}

	public void putEntry(String role, String entry) {

	}

	public Object getEntry(String role) throws EntityNotFoundException {
		if(this.getEntryThrowsException) {
			throw new EntityNotFoundException("mock");
		}
		if (role.equals(AutoExportPlugin.PLUGIN_ENTRY)) {
			// User calendars
			if (this.userCalendars == null) {
				this.userCalendars = new RaplaMapImpl(new CalendarMap());
			}
			return this.userCalendars;
		}
		return null;
	}

	public boolean hasEntry(String role) {

		return false;
	}

	public String getEntryAsString(String role) {

		return null;
	}

	public String getEntryAsString(String role, String defaultValue) {

		return null;
	}

	public boolean getEntryAsBoolean(String role, boolean defaultValue) {

		return false;
	}

	public int getEntryAsInteger(String role, int defaultValue) {

		return 0;
	}

	public boolean isEmpty() {

		return false;
	}

	@SuppressWarnings("rawtypes")
	private class CalendarMap extends HashMap<String, RefEntity> {

		private static final long serialVersionUID = 1L;

		public CalendarMap() {
			this.put("first", new PreferencesImpl());
			this.put("second", new PreferencesImpl());
		}
	}

}
