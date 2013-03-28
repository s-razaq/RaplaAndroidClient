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

package org.rapla.mobile.android.activity;

import org.rapla.mobile.android.PreferencesHandler;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.RaplaMobileLoginException;
import org.rapla.mobile.android.browser.integration.BrowserIntentHandler;
import org.rapla.mobile.android.browser.integration.ReservationEditIntentHandler;
import org.rapla.mobile.android.utility.RaplaConnection;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * This activity handles the integration with the HTML5 calendar view plugin. If
 * a calendar is called with the additional url parameter "android-client=1",
 * additional links are being displayed starting with
 * <code>rapla-client://</code> This client application listens to this
 * app-specific schema and is hence being started through the browser. In order
 * to add more handles, simple add them to the <code>registeredHandlers</code>
 * class variable.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class BrowserGatewayActivity extends BaseActivity {

	/**
	 * Add more <code>BrowserIntentHandler</code> instances here to enable them.
	 */
	public static BrowserIntentHandler[] registeredHandlers = new BrowserIntentHandler[] { new ReservationEditIntentHandler() };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Do nothing on create as this activity needs to act differently
		// everytime it is being called based in the intent
	}

	@Override
	public void onResume() {
		super.onResume();

		// Check connection
		if (!this.hasRaplaConnection()) {
			this.establishRaplaConnection();
		}

		// Get parameters from url intent
		Uri data = this.getIntent().getData();

		// Define goto activity if something fails
		Class<? extends BaseActivity> goTo;
		if (RaplaMobileApplication.USE_DEMO_HOME) {
			goTo = DummyHomeActivity.class;
		} else {
			goTo = UserCalendarListActivity.class;
		}

		try {

			// Check all registered handlers and execute the first one that
			// matches
			boolean handled = false;
			for (BrowserIntentHandler handler : registeredHandlers) {
				if (handler.matches(data)) {
					handler.handleIntent(this, this.getCustomApplication(),
							this.getIntent(), this.getRaplaContext());
					handled = true;
					break;
				}
			}

			// If no handler matched the request (this should not happen)
			if (!handled) {
				ExceptionDialogFactory
						.getInstance()
						.create(this,
								R.string.exception_browser_integration_no_handler_found,
								goTo);
			} else {
				// Finish this activity to avoid infinite behaviour when pressing back
				// button on handling activity
				this.finish();
			}

		} catch (Exception e) {
			ExceptionDialogFactory
					.getInstance()
					.create(this,
							R.string.exception_browser_integration_handling_error,
							goTo).show();
		}

	}

	/**
	 * Check whether a connection to the rapla server has been established
	 * 
	 * @return True if the runtime storage has a rapla connection instance
	 *         assigned to its identifier, false otherwise
	 */
	public boolean hasRaplaConnection() {
		return this.getCustomApplication().storageGet(
				RaplaConnection.IDENTIFIER) != null;
	}

	/**
	 * Establish connection to rapla server
	 */
	public void establishRaplaConnection() {
		try {

			// Create instance and try to login
			RaplaConnection conn = new RaplaConnection(
					PreferencesHandler.getInstance());
			conn.login();

			// Store connection instance in runtime storage
			this.getCustomApplication().storageSet(RaplaConnection.IDENTIFIER,
					conn);

		} catch (RaplaMobileLoginException e) {

			// Login failed, redirect user to settings activity to check
			// credentials
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.INTENT_BOOLEAN_NO_REDIRECT, true);
			this.startActivity(intent);
			this.finish();

		} catch (RaplaMobileException e) {

			// Some other unexpected exception occured, show dialog
			ExceptionDialogFactory
					.getInstance()
					.create(this, R.string.exception_rapla_data_retrieval,
							DummyHomeActivity.class).show();
		}
	}
}
