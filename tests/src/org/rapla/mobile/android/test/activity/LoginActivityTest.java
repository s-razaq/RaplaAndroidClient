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
import org.rapla.mobile.android.activity.LoginActivity;
import org.rapla.mobile.android.test.test.CustomActivityInstrumentationTestCase;

import android.app.Application;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Unit tests for <code>LoginActivity</code>
 * 
 * @see org.rapla.mobile.android.activity.LoginActivity
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class LoginActivityTest extends
		CustomActivityInstrumentationTestCase<LoginActivity> {
	
	public LoginActivityTest() {
		super("org.rapla.mobile.android.activity",
				LoginActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testPreConditions() {
		// Check activity class
		LoginActivity activity = this.getActivity();
		assertNotNull(activity);

		// Check application class
		Application app = activity.getApplication();
		assertNotNull(app);
		assertEquals(RaplaMobileApplication.class, app.getClass());
		
		// Check rapla logo
		ImageView logo = (ImageView)activity.findViewById(R.id.image);
		assertNotNull(logo);
		
		// Check username field
		EditText username = (EditText)activity.findViewById(R.id.username);
		assertNotNull(username);
		
		// Check password field
		EditText password = (EditText)activity.findViewById(R.id.password);
		assertNotNull(password);
		
		// Check server field
		EditText server = (EditText)activity.findViewById(R.id.server);
		assertNotNull(server);
	}
}
