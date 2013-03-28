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

import java.util.Locale;

import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.RaplaAttributeWidgetFactory;
import org.rapla.mobile.android.widget.DynamicListItem;
import org.rapla.mobile.android.widget.DynamicListItemViewWrapper;
import org.rapla.mobile.android.widget.RaplaReservationAttribute;
import org.rapla.mobile.android.widget.adapter.DynamicListItemAdapter;
import org.rapla.mobile.android.widget.adapter.RaplaDynamicTypeAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This screen shows the basic data of a reservation. The user can modify the
 * reservation data. The screen allows the user to jump to allocation screen and
 * appointment screen.
 * 
 * @author Patrick Zorn <dev@patrickzorn.de>
 */

public class EventDetailsActivity extends BaseActivity {

	private static final int DIALOG_CONFIRM_DISCARD = 1;
	private static final int DIALOG_CONFIRM_DELETE = 2;
	private OnClickListener listener;
	private Spinner eventtype;
	ReservationImpl reservation;
	private Button allocations;
	private Button appointments;
	private String allocationsText;
	private String appointmentsText;
	private DynamicType[] eventTypes = null;
	private ListView dynamicAttributesListView;
	private RaplaDynamicTypeAdapter eventTypesAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set content and custom title
		this.setContentView(R.layout.eventdetails);
		this.setTitle(R.string.titlebar_title_event_details);

		listener = new ButtonListener();

		// Set click listener for save button
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(listener);

		// Set click listener for cancel button
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(listener);

		// Dynamically create button for allocations
		this.allocations = new Button(this);
		this.allocations.setText(R.string.allocations);
		this.allocations.setId(android.R.id.button1);
		allocationsText = allocations.getText().toString();
		this.allocations.setOnClickListener(listener);

		// Dynamically create button for appointments
		this.appointments = new Button(this);
		this.appointments.setText(R.string.appointments);
		this.appointments.setId(android.R.id.button2);
		appointmentsText = appointments.getText().toString();
		this.appointments.setOnClickListener(listener);

		eventtype = (Spinner) findViewById(R.id.eventtype);

		// Get reference of list view for dynamic attributes
		this.dynamicAttributesListView = (ListView) this
				.findViewById(android.R.id.list);

		initEventTypes(eventtype);

	}

	public void onResume() {
		super.onResume();

		reservation = this.getSelectedReservation();

		// check if a new reservation is created
		if (reservation == null) {
			try {
				reservation = (ReservationImpl) getFacade().newReservation();
				this.setSelectedReservation(reservation);

			} catch (RaplaException e) {
				// Error! Referenced object [] not found in store. It was
				// probably recently removed.
				// ReferenceNotFoundExcpetion
				ExceptionDialogFactory
						.getInstance()
						.create(this, R.string.exception_creation_event_failed,
								UserCalendarListActivity.class).show();
			}
		}

		if (this.reservation != null) {
			// Update list view with dynamic attributes according to event type
			this.refreshDynamicAttributes(reservation.getClassification()
					.getType());

			// get the current event type name and preselect it on the spinner
			// ui
			// (drop-down element for event types)
			setSelectedEventType(reservation.getClassification().getType());

			// Update allocations button with hint
			countMarkedAllocations(allocations);

			// Update appointment button with hint
			countMarkedReservations(appointments);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create options menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.event_details_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * Try to delete currently selected reservation if user click on
		 * deleteEvent in option menu
		 */
		switch (item.getItemId()) {
		case R.id.deleteEvent:
			this.showDialog(DIALOG_CONFIRM_DELETE);
			break;
		}

		return true;

	}

	/**
	 * Refresh list view with dynamic attributes based on dynamic type
	 * 
	 * @param dt
	 *            Dynamic type that holds the metadata about the attributes to
	 *            be displayed
	 */
	public void refreshDynamicAttributes(DynamicType dt) {
		// Get dynamic attributes from dynamic type
		Attribute[] attributes = dt.getAttributes();

		// Set all array lengths for a better overview
		int lenDynamicAttributes = attributes.length;
		int lenListItems = attributes.length + 2; // Two additional buttons at
													// the bottom
		int indexAllocationButton = lenListItems - 2; // Second last item
		int indexAppointmentButton = lenListItems - 1; // Last item

		// Create list item array based on the dynamic attributes and two
		// additional items (buttons)
		DynamicListItem[] listItems = new DynamicListItem[lenListItems];

		// Create view using factory based on attributes
		for (int i = 0; i < lenDynamicAttributes; i++) {
			listItems[i] = RaplaAttributeWidgetFactory.getInstance().create(
					this, attributes[i]);
			((RaplaReservationAttribute) listItems[i])
					.bindReservation(this.reservation);
		}

		// Add allocation button
		listItems[indexAllocationButton] = new DynamicListItemViewWrapper(
				this.allocations);

		// Add appointment button
		listItems[indexAppointmentButton] = new DynamicListItemViewWrapper(
				this.appointments);

		// Fill fields with value from reservation if available
		if (this.reservation != null) {
			for (int i = 0; i < lenDynamicAttributes; i++) {
				RaplaReservationAttribute attribute = (RaplaReservationAttribute) listItems[i];
				attribute.pullValueFromReservation(this.reservation);
			}
		}

		// Create adapter for rapla attributes
		final DynamicListItemAdapter adapter = new DynamicListItemAdapter(this,
				listItems);
		this.dynamicAttributesListView.setAdapter(adapter);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			// show dialog
			onCreateDialog(DIALOG_CONFIRM_DISCARD).show();
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * This method initializes the spinner ui (drop-down element for event
	 * types). Therefore it reads all existing event type names from the facade
	 * and adds every one to the spinner adapter.
	 * 
	 * @param eventtype
	 *            stands for the spinner object
	 */
	public void initEventTypes(Spinner eventtype) {
		// get all existing event types
		try {
			eventTypes = this.getFacade().getDynamicTypes(
					DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);

			// Initialize adapter
			this.eventTypesAdapter = new RaplaDynamicTypeAdapter(this,
					android.R.layout.simple_spinner_item, android.R.id.text1,
					eventTypes);
			this.eventTypesAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// Set adapter
			this.eventtype.setAdapter(this.eventTypesAdapter);

			// Register item selected listener, so that the list of dynamic
			// attributes can be refreshed based on the dynamic type
			this.eventtype
					.setOnItemSelectedListener(new EventTypeItemSelectedListener(
							this, this.eventTypesAdapter));

		} catch (RaplaException e) {

			ExceptionDialogFactory
					.getInstance()
					.create(this,
							R.string.exception_internal_error,
							(RaplaMobileApplication.USE_DEMO_HOME ? DummyHomeActivity.class
									: UserCalendarListActivity.class)).show();
		}

	}

	/**
	 * This method preselects the correct event type on the spinner ui element
	 * (drop-down element for event types).
	 * 
	 * @param eventType
	 *            The event type to be set on the spinner
	 */
	public void setSelectedEventType(DynamicType eventType) {
		if (this.eventTypesAdapter == null)
			return;

		// Read position
		int position = this.eventTypesAdapter.getPosition(eventType);

		// Set selected
		this.eventtype.setSelection(position);

	}

	/**
	 * This method counts the marked reservations and displays the number of
	 * marked reservations.
	 * 
	 * @param appointments
	 *            on which the number of marked reservations will be displayed
	 */
	public void countMarkedReservations(Button appointments) {
		// set field initial
		appointments.setText(appointmentsText);

		// retrieve appointments
		Appointment[] appointment = this.getSelectedReservation()
				.getAppointments();

		appointments.setText(appointments.getText() + " (" + appointment.length
				+ ")");

	}

	/**
	 * This method counts the marked allocations and displays the number of
	 * marked allocations.
	 * 
	 * @param allocations
	 *            on which the number of marked allocations will be displayed
	 */
	public void countMarkedAllocations(Button allocations) {
		// set field initial
		allocations.setText(allocationsText);

		// retrieve allocations
		Allocatable[] allocation = this.getSelectedReservation()
				.getAllocatables();

		allocations.setText(allocations.getText() + " (" + allocation.length
				+ ")");
	}

	/**
	 * This method saves the changes on the reservation object temporarily
	 */
	public void storeValuesTemporarily() {

		// Read dynamic type from spinner
		DynamicType dt = this.eventTypesAdapter.getItem(this.eventtype
				.getSelectedItemPosition());

		// Set reservation classification based on dynamic type
		this.reservation.setClassification(dt.newClassification());

		// Walk the dynamic attributes in the list and push values to
		// reservation
		DynamicListItemAdapter adapter = (DynamicListItemAdapter) this.dynamicAttributesListView
				.getAdapter();
		int listItemsButtonOffset = 2;
		int lenDynamicAttributes = adapter.getCount() - listItemsButtonOffset;
		for (int i = 0; i < lenDynamicAttributes; i++) {
			RaplaReservationAttribute attribute = (RaplaReservationAttribute) adapter
					.getItem(i);
			this.reservation = attribute
					.putValueToReservation(this.reservation);
		}
	}

	/**
	 * This class handles click events on button save, cancel, allocations,
	 * appointments.
	 * 
	 * @author Patrick Zorn <dev@patrickzorn.de>
	 * 
	 */
	private class ButtonListener implements OnClickListener {

		public void onClick(View v) {

			// save the values temporarily before calling a new Activity
			EventDetailsActivity.this.storeValuesTemporarily();

			switch (v.getId()) {

			case R.id.save:

				// save reservation object permanently
				try {
					EventDetailsActivity.this.getFacade().store(reservation);

					// show success message
					int duration = Toast.LENGTH_LONG;
					CharSequence text = EventDetailsActivity.this
							.getString(R.string.saveSuccessfully);
					Toast toast = Toast.makeText(
							EventDetailsActivity.this.getApplicationContext(),
							text, duration);
					toast.show();

					// jump to HomeScreen
					EventDetailsActivity.this.finish();

				} catch (RaplaException e) {
					ExceptionDialogFactory
							.getInstance()
							.create(EventDetailsActivity.this,
									R.string.exception_internal_error).show();
				}
				break;

			case R.id.cancel:

				// show dialog
				showDialog(DIALOG_CONFIRM_DISCARD);

				break;

			case android.R.id.button1:

				// jump to allocation list
				goToAllocationActivity();

				break;

			case android.R.id.button2:

				// jump to appointment list
				goToAppointmentActivity();

				break;

			}

		}
	}

	/**
	 * This method discards all temporarily changes on the reservation object.
	 */
	private void discardChanges() {
		try {
			EventDetailsActivity.this.getFacade().refresh();
			finish();
		} catch (RaplaException e) {
			ExceptionDialogFactory.getInstance().create(
					EventDetailsActivity.this,
					R.string.exception_internal_error);
		}
	}

	private void goToAllocationActivity() {

		// jump to allocation list
		Intent i = new Intent(this, AllocatableListActivity.class);
		startActivity(i);
	}

	private void goToAppointmentActivity() {

		// jump to appointment list
		Intent i = new Intent(this, AppointmentListActivity.class);
		startActivity(i);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case DIALOG_CONFIRM_DISCARD:
			// Dialog to confirm discarding all temporarily changes
			dialog = this.createDialogConfirmingDiscard(builder);
			break;
		case DIALOG_CONFIRM_DELETE:
			// Dialog to confirm deletion of the reservation
			dialog = this.createDialogConfirmDelete(builder);
		}
		return dialog;
	}

	/**
	 * Initially create dialog for confirming deletion of reservation
	 * 
	 * @param builder
	 *            Builder reference to build the dialog
	 * @return Composed dialog
	 */
	public AlertDialog createDialogConfirmDelete(Builder builder) {
		builder.setMessage(R.string.confirm_delete_reservation)
				.setPositiveButton(
						R.string.yes,
						new DialogConfirmDeleteListener(this, this.getFacade(),
								this.getSelectedReservation(), this,
								ExceptionDialogFactory.getInstance()))
				.setNegativeButton(R.string.cancel, null);
		return builder.create();
	}

	/**
	 * Initially create dialog for confirming that all changes will be discard.
	 * 
	 * @param Alert
	 *            Dialog Builder to build the dialog
	 * @return Composed dialog
	 */
	public AlertDialog createDialogConfirmingDiscard(AlertDialog.Builder builder) {
		DialogInterface.OnClickListener listener = new CancelDialogListener();
		builder.setMessage(R.string.cancel_discard_changes)
				.setPositiveButton(R.string.yes, listener)
				.setNegativeButton(R.string.no, listener);
		return builder.create();
	}

	/**
	 * CancelDialogListener
	 * 
	 * This class handles the event that the user clicked on 'Cancel'
	 * 
	 */
	public class CancelDialogListener implements
			DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				// Yes

				// discard temporarily changes on the reservation object
				discardChanges();

				dialog.dismiss();
			} else {
				// Cancel
				dialog.dismiss();
			}
		}

	}

	/**
	 * Listener class for the event type selecion to update the list view with
	 * dynamic attributes based on the selected dynamic type
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class EventTypeItemSelectedListener implements
			AdapterView.OnItemSelectedListener {

		private EventDetailsActivity activity;
		private RaplaDynamicTypeAdapter adapter;

		/**
		 * @param activity
		 *            This activity so that it can be refreshed
		 * @param adapter
		 *            Adapter to retrieve selected item from
		 */
		public EventTypeItemSelectedListener(EventDetailsActivity activity,
				RaplaDynamicTypeAdapter adapter) {
			this.activity = activity;
			this.adapter = adapter;
		}

		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {

			// Get selected dynamic type
			DynamicType dt = this.adapter.getItem(position);

			// Refresh list view
			this.activity.refreshDynamicAttributes(dt);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// Shouldn't happen
		}

	}

	/**
	 * Listener for the ok button of the confirm delete reservation dialog.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class DialogConfirmDeleteListener implements
			Dialog.OnClickListener {

		private Context context;
		private ClientFacade facade;
		private ReservationImpl reservation;
		private Activity activity;
		private ExceptionDialogFactory exceptionDialogFactory;

		/**
		 * @param context
		 *            Current context
		 * @param facade
		 *            Rapla client facade
		 * @param reservation
		 *            Reservation to be deleted
		 * @param activity
		 *            Activity to be finished
		 * @param exceptionDialogFactory
		 */
		public DialogConfirmDeleteListener(Context context,
				ClientFacade facade, ReservationImpl reservation,
				Activity activity, ExceptionDialogFactory exceptionDialogFactory) {
			this.context = context;
			this.facade = facade;
			this.reservation = reservation;
			this.activity = activity;
			this.exceptionDialogFactory = exceptionDialogFactory;
		}

		public void onClick(DialogInterface dialog, int button) {
			// If confirmed
			if (button == DialogInterface.BUTTON_POSITIVE) {
				try {
					// Try to delete reservation
					this.facade.remove(this.reservation);
					Toast.makeText(
							this.context,
							String.format(
									this.context
											.getString(R.string.remove_reservation_succeed),
									this.reservation.getName(Locale
											.getDefault())), Toast.LENGTH_LONG)
							.show();
					this.activity.finish();
				} catch (RaplaException e) {
					this.exceptionDialogFactory.create(this.context,
							R.string.remove_reservation_fail).show();
				}
			}

		}
	}
}
