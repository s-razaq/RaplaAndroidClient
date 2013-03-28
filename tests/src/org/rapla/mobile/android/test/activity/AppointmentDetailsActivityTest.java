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

import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.activity.AppointmentDetailsActivity;
import org.rapla.mobile.android.test.test.CustomActivityInstrumentationTestCase;

import android.app.Application;
import android.content.Intent;

/**
 * Unit tests for <code>AppointmentDetailsActivity</code>
 * 
 * @see org.rapla.mobile.android.activity.AppointmentDetailsActivity
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class AppointmentDetailsActivityTest extends
		CustomActivityInstrumentationTestCase<AppointmentDetailsActivity> {

	public AppointmentDetailsActivityTest() {
		super("org.rapla.mobile.android.activity",
				AppointmentDetailsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	private void initializeNewAppointment() {
		Intent i = new Intent();
		i.putExtra(AppointmentDetailsActivity.INTENT_INT_APPOINTMENT_ID, -1);
		this.setActivityIntent(i);
	}

	public void testPreConditions() {
		this.initializeNewAppointment();
		
		// Check activity class
		AppointmentDetailsActivity activity = this.getActivity();
		assertNotNull(activity);

		// Check application class
		Application app = activity.getApplication();
		assertNotNull(app);
		assertEquals(RaplaMobileApplication.class, app.getClass());
	}
}
