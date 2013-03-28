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

package org.rapla.mobile.android.widget.adapter;

import java.util.Comparator;
import java.util.Locale;

import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.activity.AllocatableDetailsActivity;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * This class binds an allocatable array to a list view
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class AllocatableAdapter extends ArrayAdapter<Allocatable> {

	protected ReservationImpl selectedReservation;

	/**
	 * @param context
	 *            The current context
	 * @param selectedReservation
	 *            Reservation that is currently edited
	 * @param objects
	 *            Set of allocatables to be displayed
	 */
	public AllocatableAdapter(Context context,
			ReservationImpl selectedReservation, Allocatable[] objects) {
		super(context, R.layout.allocatable_details_list_item,
				R.id.allocatable_details_list_item_text, objects);

		// Set instance variables
		this.sort(new AllocatableComparator());
		this.selectedReservation = selectedReservation;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Make sure that convertView is not null
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.allocatable_details_list_item,
					null);
		}

		// Get references to TextViews on UI
		TextView itemText = (TextView) convertView
				.findViewById(R.id.allocatable_details_list_item_text);
		TextView itemDescription = (TextView) convertView
				.findViewById(R.id.allocatable_details_list_item_details);
		CheckBox itemCheckbox = (CheckBox) convertView
				.findViewById(R.id.allocatable_details_list_item_checkbox);

		// Fill widgets with values
		try {
			Allocatable item = this.getItem(position);
			itemText.setText(this.createItemTextForItem(item));
			itemDescription.setText(this.createItemDescriptionForItem(item));
			itemCheckbox.setChecked(this.getStateForItem(item));
			itemCheckbox.setEnabled(!itemCheckbox.isChecked());
		} catch (Exception ex) {
			// Exception thrown on facade error
			ExceptionDialogFactory
					.getInstance()
					.create(this.getContext(),
							R.string.exception_rapla_data_retrieval).show();
		}

		// Register event handler
		AllocatableDetailsActivity.AllocatableListItemCheckboxListener checkBoxListener = ((AllocatableDetailsActivity) this
				.getContext())
				.createAllocatableListItemCheckboxListener(position);
		itemCheckbox.setOnCheckedChangeListener(checkBoxListener);

		return convertView;
	}

	/**
	 * Create text for first item line based on allocatable name
	 * 
	 * @param a
	 *            Allocatable to read name from
	 * @return Name attribute of allocatable classification
	 */
	private String createItemTextForItem(Allocatable a) {
		return a.getName(Locale.getDefault());
	}

	/**
	 * Create text for second item line based on the appointments assigned to
	 * the allocatable
	 * 
	 * @param a
	 *            Allocatable to read appointment assignments from
	 * @return String with the number of assigned appointments and the maximum
	 *         number of assignable appointments
	 */
	private String createItemDescriptionForItem(Allocatable a) {
		// The reservation has not allocated the allocatable a single time, so
		// just return an empty string
		if (!this.selectedReservation.hasAllocated(a)) {
			return "";
		}

		// Read all possible appointments and assigned appointments
		Appointment[] allAppointments = this.selectedReservation
				.getAppointments();
		Appointment[] assignedAppointments = this.selectedReservation
				.getAppointmentsFor(a);

		if (allAppointments.length == assignedAppointments.length) {
			// Allocatable assigned to all appointments
			return this.getContext().getString(
					R.string.allocatable_assigned_to_all_appointments);
		} else {
			// Allocatable assigned to some appointments
			return String
					.format(this.getContext().getString(
							R.string.allocatable_assigned_to_x_of_y_appointmen),
							assignedAppointments.length, allAppointments.length);
		}
	}

	/**
	 * Read check box state from allocatable
	 * 
	 * @param a
	 *            Allocatable to read state from
	 * @return True of the allocatable is at least assigned to one appointment,
	 *         false otherwise
	 */
	private boolean getStateForItem(Allocatable a) {
		// If the reservation has allocated the appointment at least a single
		// time and the number of assigned appointments is larger than zero,
		// return true
		if (this.selectedReservation.hasAllocated(a)) {
			Appointment[] assignedAppointments = this.selectedReservation
					.getAppointmentsFor(a);
			return assignedAppointments.length > 0;
		} else {
			return false;
		}
	}

	/**
	 * AllocatableComparator
	 * 
	 * Comparator class for sorting allocatables by name.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 */
	public static class AllocatableComparator implements Comparator<Allocatable> {

		public int compare(Allocatable lhs, Allocatable rhs) {
			try {
				return lhs.getName(Locale.getDefault()).toString()
						.compareTo(rhs.getName(Locale.getDefault()).toString());
			} catch (RuntimeException e) {
				return 0;
			}
		}

	}
}
