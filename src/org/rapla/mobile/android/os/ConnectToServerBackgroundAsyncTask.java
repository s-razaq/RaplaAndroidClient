/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Patrick Zorn                                          |
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

package org.rapla.mobile.android.os;

import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.RaplaMobileLoginException;
import org.rapla.mobile.android.utility.RaplaConnection;
import android.os.AsyncTask;

/**
 * This class contains the logic for connecting to the Rapla server. It can be
 * stopped from the calling asyncTask.
 * 
 * @author Patrick Zorn <dev@patrickzorn.de>
 * 
 */
public class ConnectToServerBackgroundAsyncTask extends
		AsyncTask<Void, Void, String> {

	private RaplaMobileApplication application;
	private RaplaConnection conn;
	private String status;
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String START = "START";

	/**
	 * @param application
	 *            Current Rapla Application
	 * @param conn
	 *            Current Rapla Connection 
	 */
	public ConnectToServerBackgroundAsyncTask(
			RaplaMobileApplication application, RaplaConnection conn) {
		this.application = application;
		this.conn = conn;		
	}

	protected void onPreExecute() {

	}

	@Override
	/** 
	 * This method tries to login to the Rapla Backend Server
	 * and store the data when login was possible
	 *  
	 *  @return String success status
	 */
	protected String doInBackground(Void... notUsed) {

		try {
			if (conn.login()) {
				// Login successful

				// save the Rapla Connection Data
				this.application.storageSet(RaplaConnection.IDENTIFIER, conn);

				status = SUCCESS;
			} else {
				status = ERROR;
				this.cancel(true);
			}
		} catch (RaplaMobileLoginException e) {
			status = ERROR;
			this.cancel(true);
		}

		return status;

	}

	/**
	 * This method calls the next activity and saves the Rapla Connection Data
	 * 
	 * @param String
	 *            success status
	 */
	protected void onPostExecute(String status) {

	}

	@Override
	protected void onProgressUpdate(Void... v) {
		super.onProgressUpdate(v);

	}

}
