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
import org.rapla.mobile.android.RuntimeStorage;
import org.rapla.mobile.android.activity.AllocatableListActivity;
import org.rapla.mobile.android.test.test.CustomActivityInstrumentationTestCase;
import org.rapla.mobile.android.test.test.FixtureHelper;

import android.app.Application;
import android.view.View;
import android.widget.ListView;

/**
 * Unit tests for <code>AllocatableListActivity</code>
 * 
 * @see org.rapla.mobile.android.activity.AllocatableListActivity
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class AllocatableListActivityTest extends
		CustomActivityInstrumentationTestCase<AllocatableListActivity> {

	public AllocatableListActivityTest() {
		super("org.rapla.mobile.android.activity",
				AllocatableListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	private void initializeSelectedReservation() {
		RuntimeStorage storage = RuntimeStorage.getInstance();
		storage.store(RuntimeStorage.IDENTIFIER_SELECTED_RESERVATION, FixtureHelper.createReservation());
	}

	public void testPreConditions() {
		this.initializeSelectedReservation();
		
		// Check activity class
		AllocatableListActivity activity = this.getActivity();
		assertNotNull(activity);

		// Check application class
		Application app = activity.getApplication();
		assertNotNull(app);
		assertEquals(RaplaMobileApplication.class, app.getClass());

		// Check list view
		View v = activity.findViewById(R.id.allocation_list);
		assertNotNull(v);
		ListView listView = (ListView) v;
		assertNotNull(listView);
	}
}
