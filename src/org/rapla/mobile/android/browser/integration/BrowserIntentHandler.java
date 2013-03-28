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

package org.rapla.mobile.android.browser.integration;

import org.rapla.framework.RaplaContext;
import org.rapla.mobile.android.RaplaMobileApplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * This interface is used to dynamically handle an intent from the browser.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public interface BrowserIntentHandler {

	/**
	 * Check whether this handler class can handle the uri
	 * 
	 * @param data
	 *            Uri instance from intent
	 * @return True if the handler matches the uri data, false otherwise
	 */
	public boolean matches(Uri data);

	/**
	 * Handle the requested action
	 * 
	 * @param context
	 *            The current context
	 * @param application
	 *            Reference to the custom application
	 * @param intent
	 *            The received intent
	 * @param raplaContext
	 *            The current rapla context
	 * @throws Exception
	 *             If anything unexpected happens
	 */
	public void handleIntent(Context context,
			RaplaMobileApplication application, Intent intent,
			RaplaContext raplaContext) throws Exception;
}
