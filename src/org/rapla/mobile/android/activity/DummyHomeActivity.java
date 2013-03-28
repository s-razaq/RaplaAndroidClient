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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.framework.RaplaException;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.utility.RaplaConnection;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * This screen allows the user to show all reservations assigned to him.
 * Furthermore can does it provide options to add new reservations. It can be
 * perfectly used for testing and development purposes instead of using the
 * mobile view plugin to get to the edit mode for appointments. It is not
 * productively used!
 * 
 * @author Saqib Razaq <dev@razaq.de>
 */

public class DummyHomeActivity extends BaseActivity {

	private ListView reservationsList;
	private List<Map<String, ?>> list;
	public final static String ITEM_TITLE = "TITLE";
	public final static String ITEM_DETAILS = "DETAILS";
	private Reservation[] res;
	public static final String PROCESSING_RESERVATION = "Processing Reservation";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set content and custom title
		this.setContentView(R.layout.home);
		this.setTitle("todo");

		// Initialize references to view
		reservationsList = (ListView) findViewById(R.id.projectList);
		reservationsList
				.setOnItemClickListener(new ReservationsListItemClickListener(
						this.getCustomApplication()));

		// Register list view and list items for context menu
		registerForContextMenu(reservationsList);

	}

	public void onResume() {
		super.onResume();

		// login to Rapla Backend or call Login Activity
		if (!this.getCustomApplication().storageHas(RaplaConnection.IDENTIFIER)) {

			// call LoginActivity if no data exist
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);

		} else {
			fillListViewWithDemoData();
		}

	}

	private void fillListViewWithDemoData() {
		list = new LinkedList<Map<String, ?>>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy");
		try {
			res = getFacade().getReservations(getFacade().getUser(),
					new Date(), null, null);
			for (Reservation r : res) {
				Appointment[] app = r.getAppointments();
				List<Date> dateList = new ArrayList<Date>();
				for (Appointment a : app) {
					dateList.add(a.getStart());
				}

				Collections.sort(dateList);

				list.add(createItem(r.getName(null).toString(),
						sdf.format(dateList.get(0))));
			}
		} catch (RaplaException e) {
			e.printStackTrace();
		}

		String[] from = { ITEM_TITLE, ITEM_DETAILS };
		int[] to = { android.R.id.text1, android.R.id.text2 };

		SimpleAdapter sa = new SimpleAdapter(getBaseContext(), list,
				android.R.layout.simple_list_item_2, from, to);
		reservationsList.setAdapter(sa);

	}

	public Map<String, ?> createItem(String title, String details) {
		Map<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_DETAILS, details);
		return item;
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

	/*
	 * I want to ensure that the event comes from the ListView and if so, i
	 * create a new ContextMenu with appropriate Header and Data
	 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.projectList) {
			/*
			 * The argument of type ContextMenuInfo can actually change
			 * depending on what type of control is sending the event. For
			 * ListViews, the class you need to type cast into is
			 * AdapterView.AdapterContextMenuInfo
			 */
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(list.get(info.position).get(ITEM_TITLE)
					.toString());
			menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.detele));
		}
	}

	/*
	 * Retrieve information about which item the user selected and perform the
	 * appropriate action
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		// User wants to delete the reservation
		case 1:
			try {
				getFacade().remove(res[info.position]);
			} catch (RaplaException e) {
				Toast.makeText(
						getBaseContext(),
						res[info.position].getName(null) + " "
								+ getString(R.string.remove_reservation_fail),
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		return true;
	}

	/**
	 * ReservationListItemClickedListener
	 * 
	 * This class handles list item clicks. Depending on which item was clicked
	 * the appropriate data about the reservation should be retrieved from
	 * backend and stored in the internal StorageClass. Furthermore should it
	 * start the EventDetailsActivity.
	 * 
	 * @author Saqib Razaq <dev@razaq.de>
	 * 
	 */
	public class ReservationsListItemClickListener implements
			OnItemClickListener {

		RaplaMobileApplication customApplication;

		public ReservationsListItemClickListener(
				RaplaMobileApplication customApplication) {
			super();
			this.customApplication = customApplication;
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
				setSelectedReservation((ReservationImpl) getFacade().edit(
						res[arg2]));
			} catch (RaplaException e) {
				e.printStackTrace();
			}
			startActivity(new Intent(getApplicationContext(),
					EventDetailsActivity.class));
		}

	}

}
