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

package org.rapla.mobile.android.os;

import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.activity.EventDetailsActivity;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.AppointmentAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

/**
 * This class asynchronously loads allocatables for the given reservation and
 * assignes them to the list view UI.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class LoadAppointmentsAsyncTask extends
		AsyncTask<Void, Void, Appointment[]> {

	protected ProgressDialog progressDialog;
	protected LoadDataProgressDialogFactory progressDialogFactory;
	protected Context context;
	protected AppointmentFormater formatter;
	protected ReservationImpl reservation;
	protected ListView listView;
	protected ExceptionDialogFactory exceptionDialogFactory;
	protected Class<? extends Activity> onFailureGoTo;

	public LoadAppointmentsAsyncTask(Context context,
			AppointmentFormater formatter, ListView listView,
			ReservationImpl reservation,
			ExceptionDialogFactory exceptionDialogFactory,
			LoadDataProgressDialogFactory progressDialogFactory,
			Class<? extends Activity> onFailureGoTo) {
		super();
		this.context = context;
		this.listView = listView;
		this.reservation = reservation;
		this.exceptionDialogFactory = exceptionDialogFactory;
		this.progressDialogFactory = progressDialogFactory;
		this.onFailureGoTo = onFailureGoTo;
		this.formatter = formatter;
	}

	@Override
	protected Appointment[] doInBackground(Void... notUsed) {
		// Retrieve appointments
		Appointment[] appointments = this.reservation.getAppointments();
		return appointments;
	}

	protected void onPreExecute() {
		// Initialize progress dialog
		this.progressDialog = this.progressDialogFactory.create(this.context);
		this.progressDialog.show();
	}

	protected void onPostExecute(Appointment[] appointments) {
		if (appointments != null) {

			// Retrieval successful, update list view
			AppointmentAdapter adapter = new AppointmentAdapter(this.context,
					R.layout.appointment_list_item, appointments,
					this.formatter);
			this.listView.setAdapter(adapter);

			// Close progress dialog
			this.progressDialog.dismiss();
		} else {
			// Close progress dialog
			this.progressDialog.dismiss();

			// Retrieval failed, show exception dialog
			this.exceptionDialogFactory.create(this.context,
					R.string.exception_rapla_data_retrieval,
					EventDetailsActivity.class).show();
		}
	}
}