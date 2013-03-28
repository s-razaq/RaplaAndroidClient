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

package org.rapla.mobile.android.activity;

import org.rapla.mobile.android.PreferencesHandler;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.os.ConnectToServerAsyncTask;
import org.rapla.mobile.android.utility.RaplaConnection;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The login screen is only shown at the first time when the application will be
 * started. The user has to type in his data. Optionally this screen can be call
 * from the HomeActivity when the user wants to change the settings.
 * 
 * @author Patrick Zorn <dev@patrickzorn.de>
 */

public class LoginActivity extends BaseActivity {

	private OnClickListener listener;
	private PreferencesHandler loginData;
	private EditText username, password, server;
	private Button login;
	private Boolean calledFromHomeActivity = false;
	private AsyncTask<?, ?, ?> runningTask;
	public static final String INTENT_BOOLEAN_NO_REDIRECT = "no_redirect";
	private static final int DIALOG_WAIT = 115;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set content and custom title
		this.setContentView(R.layout.login);
		this.setTitle(R.string.titlebar_title_login);

		listener = new SubmitListener();

		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(listener);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		server = (EditText) findViewById(R.id.server);

	}

	@Override
	public void onResume() {
		super.onResume();

		loginData = PreferencesHandler.getInstance();

		// check if the screen is called from HomeActivity for changing data
		calledFromHomeActivity = getIntent().getBooleanExtra(
				INTENT_BOOLEAN_NO_REDIRECT, false);

		// Pre-populate UI with username and host
		this.username.setText(loginData.getUsername());
		if (loginData.hasHost()) {
			String protocol;
			if(loginData.isSecure()) {
				protocol = "https://";
			} else {
				protocol = "http://";
			}
			this.server.setText(String.format((loginData.hasHostPort() ? "%s%s:%s" : "%s%s"), protocol, loginData.getHost(),
					loginData.getHostPort()));
		}

		if (calledFromHomeActivity) {

			// adapt the name of the login button
			login.setText(R.string.save);

		} else {
			// adapt the name of the login button
			login.setText(R.string.login);

			// check if login data exist
			if (loginData.hasConnectionPreferences()) {
				// login with existing login data
				login(loginData);
			}
		}

	}

	public void onDestroy() {
		super.onDestroy();

		// Stop running background task if available. This is e.g. necessary to
		// avoid short dump when rotating device.
		if (this.runningTask != null) {
			this.runningTask.cancel(true);
		}
	}

	/**
	 * This class handles the click event of the login button.
	 * 
	 * @author Patrick Zorn <dev@patrickzorn.de>
	 * 
	 */
	private class SubmitListener implements OnClickListener {

		public void onClick(View v) {

			login();

		}
	}

	/**
	 * This method tries to login to the Rapla Backend System
	 * 
	 * @param preference
	 *            stands for the PreferencesHandler
	 */
	private void login(PreferencesHandler preference) {
		RaplaConnection conn = null;

		try {
			conn = new RaplaConnection(loginData);

			connectToServer(conn);

		} catch (RaplaMobileException e) {
			// Not possible to set values
			CharSequence text = (CharSequence) getString(R.string.loginDataSaveError);
			showError(text);
		}
	}

	/**
	 * This method save the entered login credentials and tries to login to the
	 * Rapla Backend System
	 */
	private void login() {

		// if login data is not existing, set required login data. When
		// the screen was called from HomeActivity, overwrites the existing
		// data

		int port = 80;
		String host = "";
		String[] porttext;
		boolean isSecure = false;
		String serverInput;

		try {
			serverInput = server.getText().toString();
			if(serverInput.contains("https://")) {
				isSecure = true;
				serverInput = serverInput.replace("https://", "");
			} else if(serverInput.contains("http://")) {
				isSecure = false;
				serverInput = serverInput.replace("http://", "");
			}

			// split the server ip address which was entered by user into
			// the
			// number part and the port part
			porttext = serverInput.split(":");

			if (porttext != null && porttext.length >= 2
					&& porttext[1].length() >= 1) {
				host = porttext[0];

				// ensure that porttext[1] is a number.
				port = Integer.parseInt(porttext[1]);

			} else {
				// standard values when no port is given
				host = server.getText().toString();
				port = 80;
			}

			loginData.setUsername(username.getText().toString());
			loginData.setPassword(password.getText().toString());
			loginData.setHost(host);
			loginData.setHostPort(port);
			loginData.setSecure(isSecure);

			// call the login method
			login(loginData);

		} catch (NumberFormatException e) {
			// Port number is not an integer
			CharSequence text = (CharSequence) getString(R.string.exception_number_format);
			showError(text);
		}

	}

	/**
	 * This method shows generic error messages by using Toast Messages.
	 * 
	 * @param text
	 *            stands for the error message
	 */
	private void showError(CharSequence text) {

		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
		toast.show();
	}

	/**
	 * This method checks if an Internet connection is available
	 * 
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	/**
	 * This method calls an asyn task to login to the Rapla Server
	 * 
	 * @param conn
	 *            stands for the RaplaConnection object
	 */
	private void connectToServer(RaplaConnection conn) {

		if (isNetworkAvailable()) {

			this.runningTask = new ConnectToServerAsyncTask(this,
					this.getCustomApplication(), conn,
					ExceptionDialogFactory.getInstance(),
					onCreateDialog(DIALOG_WAIT),
					LoadDataProgressDialogFactory.getInstance(), this)
					.execute();
		} else {
			ExceptionDialogFactory
					.getInstance()
					.create(LoginActivity.this,
							R.string.exception_no_internet_connection).show();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case DIALOG_WAIT:
			// Dialog to confirm waiting
			dialog = this.createDialogConfirmWait(builder);
			break;
		}
		return dialog;
	}

	/**
	 * Initially create dialog for confirming if the user wants to wait or not
	 * 
	 * @param Alert
	 *            Dialog Builder to build the dialog
	 * @return Composed dialog
	 */
	public AlertDialog createDialogConfirmWait(AlertDialog.Builder builder) {
		DialogInterface.OnClickListener listener = new CancelDialogListener();
		builder.setMessage(R.string.cancel_login_wait)
				.setPositiveButton(R.string.yes, listener)
				.setNegativeButton(R.string.no, listener);
		return builder.create();
	}

	/**
	 * CancelDialogListener
	 * 
	 * This class handles the event that the user clicked on 'Cancel' or 'Yes'
	 * 
	 */
	public class CancelDialogListener implements
			DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				// Yes

				// stop login procedure
				runningTask.cancel(true);

				dialog.dismiss();
			} else {
				// Cancel

				// continue login
				dialog.dismiss();
			}
		}

	}

}
