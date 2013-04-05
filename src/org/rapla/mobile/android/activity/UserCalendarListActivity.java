/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Saqib Razaq                                           |
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

import org.rapla.components.xmlbundle.I18nBundle;
import org.rapla.mobile.android.PreferencesHandler;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.os.LoadUserCalendarsAsyncTask;
import org.rapla.mobile.android.utility.MobileCalendarUrlBuilder;
import org.rapla.mobile.android.utility.RaplaConnection;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.UserCalendarAdapter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * This activity displays all calendars assigned to the currelty logged in user.
 * A click on a list item calls the mobile view in the browser for the selected
 * calendar.
 * 
 * @author Saqib Razaq <dev@razaq.de>
 */
public class UserCalendarListActivity extends BaseActivity {

	private ListView listView;
	private AsyncTask<?, ?, ?> runningTask;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set content and custom title
		this.setContentView(R.layout.calendar_list);
		this.setTitle(R.string.titlebar_title_user_calendars);

		// Initialize references to view
		listView = (ListView) findViewById(R.id.calendar_list);
		listView.setEmptyView(findViewById(android.R.id.empty));
		listView.setOnItemClickListener(new CalendarItemClickListener(
				PreferencesHandler.getInstance()));

	}

	public void onResume() {
		super.onResume();

		// login to Rapla Backend or call Login Activity
		if (!this.getCustomApplication().storageHas(RaplaConnection.IDENTIFIER)) {

			// call LoginActivity if no data exist
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);

		} else {
			refreshListView();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create options menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * Intents allow sending or receiving data from and to other activities
		 * or services. Furthermore can Intents be used with startActivity to
		 * launch an Activity
		 */
		Intent i;

		/*
		 * Lunch the right activity depending on which item was selected on the
		 * menu screen
		 */
		switch (item.getItemId()) {
		case R.id.addEvent:
			// Create Intent with information to start EventDetails Activity
			i = new Intent(getBaseContext(), EventDetailsActivity.class);
			this.setSelectedReservation(null);
			startActivity(i);
			break;
		case R.id.settings:
			// Create Intent with information about which class to start
			i = new Intent(getBaseContext(), LoginActivity.class);

			// call LoginActivitiy with special values (adapt button text)
			i.putExtra(LoginActivity.INTENT_BOOLEAN_NO_REDIRECT, true);

			// Launch a new activity
			startActivity(i);
			break;
		}

		return true;

	}

	/**
	 * Refresh list view by starting asynchronous background task
	 */
	public void refreshListView() {
		try {
			I18nBundle i18n =  this.getRaplaContext().lookup(I18nBundle.class);
			this.runningTask = new LoadUserCalendarsAsyncTask(this,
					this.listView, this.getFacade(), i18n.getString("default"),
					ExceptionDialogFactory.getInstance(),
					LoadDataProgressDialogFactory.getInstance(), null)
					.execute();
		} catch (Exception e) {
			ExceptionDialogFactory.getInstance()
					.create(this, R.string.exception_rapla_context_lookup)
					.show();
		}
	}

	/**
	 * CalendarItemClickListener
	 * 
	 * This class handles clicks on calendar items and calls the corresponding
	 * url of the mobile calendar view.
	 * 
	 * @author Saqib Razaq <dev@razaq.de>
	 * 
	 */
	private class CalendarItemClickListener implements OnItemClickListener {

		private PreferencesHandler preferences;

		/**
		 * @param preferences
		 *            Reference to the applications preferences handler
		 */
		public CalendarItemClickListener(PreferencesHandler preferences) {
			this.preferences = preferences;
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			// Assemble url
			String selectedCalendar = ((UserCalendarAdapter) listView
					.getAdapter()).getItem(position);
			MobileCalendarUrlBuilder urlBuilder = new MobileCalendarUrlBuilder(
					this.preferences.getHost(), this.preferences.getHostPort(),
					this.preferences.getUsername(), this.preferences.isSecure(), selectedCalendar);

			// Build and start intent to go to mobile view of selected calendar
			Intent action = new Intent(Intent.ACTION_VIEW,
					urlBuilder.buildUri());
			startActivityForResult(action, 33);
//			startActivity(action);
		}

	}
}
