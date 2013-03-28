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

import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.utility.RaplaConnection;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This class asyncronously connects to the Rapla server by using another
 * AsyncTask.
 * 
 * @author Patrick Zorn <dev@patrickzorn.de>
 * 
 */
public class ConnectToServerAsyncTask extends
		AsyncTask<Void, Integer, RaplaConnection> {

	private Context context;
	private RaplaMobileApplication application;
	private RaplaConnection conn;
	private ExceptionDialogFactory exceptionDialogFactory;
	private LoadDataProgressDialogFactory progressDialogFactory;
	private Activity onSuccessFinish;
	private Dialog waitdialog;
	private ProgressDialog progressDialog;
	private AsyncTask<?, ?, ?> asyncTask;
	private String status = "";
	private boolean error = false;
	private static final String SUCCESS = "SUCCESS";

	/**
	 * @param context
	 *            Current context
	 * @param application
	 *            Current Rapla Application
	 * @param conn
	 *            Current Rapla Connection
	 * @param exceptionDialogFactory
	 *            Factory class for exception dialog
	 * @param waitdialog
	 *            Wait Dialog which aks the user if he wants to wait or not
	 * @param progressDialogFactory
	 *            Factory class for progress dialog
	 * @param onSuccessFinish
	 *            Activity to be shut down
	 */
	public ConnectToServerAsyncTask(Context context,
			RaplaMobileApplication application, RaplaConnection conn,
			ExceptionDialogFactory exceptionDialogFactory, Dialog waitdialog,
			LoadDataProgressDialogFactory progressDialogFactory,
			Activity onSuccessFinish) {
		this.context = context;
		this.application = application;
		this.conn = conn;
		this.exceptionDialogFactory = exceptionDialogFactory;
		this.waitdialog = waitdialog;
		this.progressDialogFactory = progressDialogFactory;
		this.onSuccessFinish = onSuccessFinish;
	}

	protected void onPreExecute() {
		// Initialize progress dialog
		this.progressDialog = this.progressDialogFactory.create(this.context);
		this.progressDialog.show();
	}

	@Override
	protected RaplaConnection doInBackground(Void... notUsed) {
		int i = 1;
		try {

			while (!status.equals(SUCCESS)) {
				// if the asynTask was canceled, stop working
				if (this.isCancelled()) {
					break;
				}
				// call the method to show a dialog after a specific time if the
				// user wants to wait
				this.publishProgress(i);
				i++;
				// wait a second
				Thread.sleep(1000);
			}

		} catch (InterruptedException e) {
			// an error occurred, close asyncTask
			error = true;
			this.cancel(true);
		}

		return conn;

	}

	/**
	 * This method calls the next activity
	 * 
	 * @param RaplaConncetion
	 *            conn
	 */
	protected void onPostExecute(RaplaConnection connection) {
		if (this.asyncTask != null) {
			this.asyncTask.cancel(true); // TODO verify necessity
		}

		if (status.equals(SUCCESS)) {

			// Close progress dialog
			this.progressDialog.dismiss();

			if (this.onSuccessFinish != null) {
				// start Activity
				onSuccessFinish.finish();
			}

		}

	}

	protected void onProgressUpdate(Integer... i) {

		// how long has the user to wait before the waitDialog will be shown
		int waitTime = 60;
		// start login asynTask in background
		if (asyncTask == null) {
			asyncTask = new ConnectToServerBackgroundAsyncTask(application,
					conn).execute();
		}

		// check the status of the background task
		if (asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)
				&& !asyncTask.isCancelled()) {
			// task was successfully executed
			status = SUCCESS;

		} else if (asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)
				&& asyncTask.isCancelled()) {
			// task was not successfully executed
			error = true;
			this.cancel(true);

		} else if (!this.isCancelled() && i[0] % waitTime == 0) {

			// show only the waitDialog, if the AsynTask is running now and no
			// waitDialog is visible in the UI
			if (!waitdialog.isShowing()
					&& !asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
				waitdialog.show();
			}
		}

	}

	protected void onCancelled() {

		if (asyncTask != null) {
			asyncTask.cancel(true);
		}

		// Close progress dialog
		this.progressDialog.dismiss();

		// Login failed, show an error message
		if (error) {
			CharSequence text = (CharSequence) this.context
					.getString(R.string.loginDataError);
			this.exceptionDialogFactory.create(this.context, text.toString())
					.show();
		}

	}
}
