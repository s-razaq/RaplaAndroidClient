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

import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.os.LoadAppointmentsAsyncTask;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.AppointmentAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

/**
 * The appointment list screen allows the user to show, add, edit and display
 * all appointments assigned to the current reservation.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class AppointmentListActivity extends BaseActivity {

	private ListView appointmentListView;
	private AppointmentFormater formatter;
	private SelectedAppointmentActionHandler selectedAppointment = new SelectedAppointmentActionHandler();
	private AsyncTask<?, ?, ?> runningTask;
	private final static int DIALOG_CONFIRM_DELETE_APPOINTMENT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set content and custom title
		this.setContentView(R.layout.appointment_list);
		this.setTitle(R.string.titlebar_title_appointment_list);

		// Initialize references to view
		appointmentListView = (ListView) findViewById(R.id.appointment_list);
		appointmentListView.setEmptyView(findViewById(android.R.id.empty));
		appointmentListView
				.setOnItemClickListener(new AppointmentItemClickListener());

		// Register list view and list items for context menu
		this.registerForContextMenu(appointmentListView);
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		// Context menu for appointment list items
		if (v.getId() == R.id.appointment_list) {
			// Initialize selected appointment
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			this.selectedAppointment.handleListItem(info.position);

			// Context menu for list item
			menu.setHeaderTitle(R.string.options);
			menu.add(Menu.NONE, R.string.option_delete, 1,
					R.string.option_delete); // Delete list item
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.string.option_delete) {
			// Delete list item
			this.showDialog(DIALOG_CONFIRM_DELETE_APPOINTMENT);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case DIALOG_CONFIRM_DELETE_APPOINTMENT:
			// Dialog to confirm that an appointment should be deleted
			dialog = this.createDialogConfirmDeleteAppointment(builder);
			break;
		}
		return dialog;
	}

	/**
	 * Initially create dialog for confirming deleting an appointment
	 * 
	 * @param builder
	 *            Dialog builder to build the dialog
	 * @return Composed dialog
	 */
	private AlertDialog createDialogConfirmDeleteAppointment(
			AlertDialog.Builder builder) {
		DialogInterface.OnClickListener listener = new AppointmentItemDeletionDialogListener();
		builder.setMessage(R.string.appointment_confirm_delete)
				.setPositiveButton(R.string.yes, listener)
				.setNegativeButton(R.string.cancel, listener);
		return builder.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.appointment_list_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.appointment_new:
			Intent action = new Intent(this, AppointmentDetailsActivity.class);
			action.putExtra(AppointmentDetailsActivity.INTENT_INT_APPOINTMENT_ID, -1);
			this.startActivity(action);
			break;
		default:
			// Unknown command, return false as it can't be handled by this
			// method
			return false;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Refresh view with latest data
		this.refreshListView();
	}

	/**
	 * Refresh list view by retrieving the latest data from the selected
	 * reservation
	 */
	public void refreshListView() {
		try {
			// Lazy load appointment formatter
			if (this.formatter == null) {
				this.formatter = this.getRaplaContext().lookup(AppointmentFormater.class);
			}
			// Execute task
			this.runningTask = new LoadAppointmentsAsyncTask(this,
					this.formatter, this.appointmentListView,
					this.getSelectedReservation(),
					ExceptionDialogFactory.getInstance(),
					LoadDataProgressDialogFactory.getInstance(),
					EventDetailsActivity.class).execute();
		} catch (Exception e) {
			ExceptionDialogFactory.getInstance().create(this,
					R.string.exception_rapla_context_lookup);
		}

	}

	/**
	 * The list adapter cannot be kept as an instance attribute as for some
	 * reason, as soon as passing the reference to the async task for loading
	 * appointments, the reference gets lost. So always retrieve it on the fly
	 * from the list view.
	 * 
	 * @return Casted list adapter of list view
	 */
	public AppointmentAdapter getListAdapter() {
		return (AppointmentAdapter) this.appointmentListView.getAdapter();
	}

	/**
	 * AppointmentItemClickListener
	 * 
	 * This class handles click events on appointment list items. A regular
	 * click displays the details screen for the selected appointment.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 */
	private class AppointmentItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// Go to edit view for selected appointment
			Intent action = new Intent(getApplicationContext(),
					AppointmentDetailsActivity.class);
			action.putExtra(AppointmentDetailsActivity.INTENT_INT_APPOINTMENT_ID, arg2);
			startActivity(action);
		}

	}

	/**
	 * AppointmentItemDeletionDialogListener
	 * 
	 * This class handles the dialog for confirming deleting an appointment
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 */
	private class AppointmentItemDeletionDialogListener implements
			DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				selectedAppointment.delete();
			} else {
				dialog.dismiss();
			}
		}

	}

	/**
	 * SelectedAppointmentActionHandler
	 * 
	 * This class handles all actions related to an appointment selected from
	 * the list view.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 */
	private class SelectedAppointmentActionHandler {

		private Appointment currentAppointment;
		private int listItemIndex;

		public void handleListItem(int i) {
			this.resetAttributes();

			this.listItemIndex = i;
			this.currentAppointment = this
					.getAppointmentByListItemIndex(this.listItemIndex);
		}

		private Appointment getAppointmentByListItemIndex(int i) {
			return getListAdapter().getItem(i);
		}

		private void resetAttributes() {
			this.currentAppointment = null;
			this.listItemIndex = -1;
		}

		public void delete() {
			// Remove appointment from current reservation
			AppointmentListActivity.this.getSelectedReservation()
					.removeAppointment(this.currentAppointment);

			refreshListView();

			// Show notification
			Toast.makeText(AppointmentListActivity.this,
					R.string.appointment_successfully_deleted,
					Toast.LENGTH_LONG).show();
		}
	}
}
