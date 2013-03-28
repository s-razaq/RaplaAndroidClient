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

import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.mobile.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class binds an appointment array to a two-item list view
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class AppointmentAdapter extends ArrayAdapter<Appointment> {

	private AppointmentFormater formatter;

	public AppointmentAdapter(Context context, int textViewResourceId,
			Appointment[] objects, AppointmentFormater formatter) {
		super(context, textViewResourceId, objects);

		this.formatter = formatter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Make sure that convertView is not null
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.appointment_list_item, null);
		}

		// Get references to TextViews on UI
		TextView itemText = (TextView) convertView
				.findViewById(R.id.appointment_list_item_text);
		TextView itemDescription = (TextView) convertView
				.findViewById(R.id.appointment_list_item_details);

		// Get current appointment
		Appointment currentAppointment = this.getItem(position);

		/**
		 * @see org.rapla.gui.internal.edit.reservation.AppointmentListEdit.AppointmentRow#setAppointment(Appointment,
		 *      int)
		 */
		itemText.setText(this.formatter.getSummary(currentAppointment));
		if (currentAppointment.getRepeating() != null) {
			itemDescription.setText(this.formatter
					.getSummary(currentAppointment.getRepeating()));
		} else {
			itemDescription.setText(this.getContext().getString(
					R.string.appointment_not_repeating));
		}

		return convertView;
	}

}
