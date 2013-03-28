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

package org.rapla.mobile.android.test.activity;

import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.activity.UserCalendarListActivity;
import org.rapla.mobile.android.test.test.CustomActivityInstrumentationTestCase;

import android.app.Application;
import android.view.View;
import android.widget.ListView;

/**
 * Unit tests for <code>UserCalendarListActivity</code>
 * 
 * @see org.rapla.mobile.android.activity.UserCalendarListActivity
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class UserCalendarListActivityTest extends
		CustomActivityInstrumentationTestCase<UserCalendarListActivity> {

	public UserCalendarListActivityTest() {
		super("org.rapla.mobile.android.activity",
				UserCalendarListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testPreConditions() {
		// Check activity class
		UserCalendarListActivity activity = this.getActivity();
		assertNotNull(activity);

		// Check application class
		Application app = activity.getApplication();
		assertNotNull(app);
		assertEquals(RaplaMobileApplication.class, app.getClass());

		// Check list view
		View v = activity.findViewById(R.id.calendar_list);
		assertNotNull(v);
		ListView listView = (ListView) v;
		assertNotNull(listView);
	}
}
