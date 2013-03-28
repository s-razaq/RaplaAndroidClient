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

package org.rapla.mobile.android.test.test;

import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.RuntimeStorage;
import org.rapla.mobile.android.test.mock.MockRaplaConnection;
import org.rapla.mobile.android.utility.RaplaConnection;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * CustomActivityInstrumentationTestCase
 * 
 * This class provides extended functionality for testing activities by
 * injecting mock objects into the application.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public abstract class CustomActivityInstrumentationTestCase<T extends Activity>
		extends ActivityInstrumentationTestCase2<T> {

	protected MockRaplaConnection raplaConnection;

	public CustomActivityInstrumentationTestCase(String pkg,
			Class<T> activityClass) {
		super(pkg, activityClass);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.setActivityInitialTouchMode(false);

		// Rapla connection
		this.raplaConnection = new MockRaplaConnection();
		RuntimeStorage.getInstance().store(RaplaConnection.IDENTIFIER,
				this.raplaConnection);
	}

	public RaplaMobileApplication getCustomApplication() {
		return (RaplaMobileApplication) this.getActivity().getApplication();
	}

}
