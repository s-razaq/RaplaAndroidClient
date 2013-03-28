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

import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.app.ExceptionDialog;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.AllocatableAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

/**
 * This class asynchronously loads allocatables and assignes them to the list
 * view UI.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class LoadAllocatablesAsyncTask extends
		AsyncTask<Void, Void, Allocatable[]> {

	protected ProgressDialog progressDialog;
	protected ReservationImpl selectedReservation;
	protected ListView listView;
	protected String allocatableCategoryElementKey;
	protected Context context;
	protected ExceptionDialog exceptionDialog;
	protected ClientFacade facade;
	protected Class<? extends Activity> onFailureGoTo;
	protected ExceptionDialogFactory exceptionDialogFactory;
	protected LoadDataProgressDialogFactory progressDialogFactory;

	/**
	 * @param context
	 *            Current context
	 * @param selectedReservation
	 *            Reservation that is currently edited
	 * @param listView
	 *            List view to be updated
	 * @param facade
	 *            Rapla client facade for retrieving data
	 * @param allocatableCategoryElementKey
	 *            Id of the selected allocatable category (dynamic type)
	 * @param exceptionDialogFactory
	 *            Factory class for exception dialog
	 * @param progressDialogFactory
	 *            Factory class for progress dialog
	 * @param onFailureGoTo
	 *            Activity to be called upon failure
	 */
	public LoadAllocatablesAsyncTask(Context context,
			ReservationImpl selectedReservation, ListView listView,
			ClientFacade facade, String allocatableCategoryElementKey,
			ExceptionDialogFactory exceptionDialogFactory,
			LoadDataProgressDialogFactory progressDialogFactory,
			Class<? extends Activity> onFailureGoTo) {
		super();
		this.context = context;
		this.selectedReservation = selectedReservation;
		this.listView = listView;
		this.allocatableCategoryElementKey = allocatableCategoryElementKey;
		this.facade = facade;
		this.exceptionDialogFactory = exceptionDialogFactory;
		this.progressDialogFactory = progressDialogFactory;
		this.onFailureGoTo = onFailureGoTo;
	}

	@Override
	protected Allocatable[] doInBackground(Void... notUsed) {
		Allocatable[] allocatables = null;
		try {
			DynamicType allocatableCategory = this
					.retrieveSelectedAllocatableCategory();
			allocatables = this.retrieveAllocatbales(allocatableCategory);
		} catch (Exception e) {
			// Set to null to indicate an error
			allocatables = null;
		}
		return allocatables;
	}

	protected void onPreExecute() {
		// Initialize progress dialog
		this.progressDialog = this.progressDialogFactory.create(this.context);
		this.progressDialog.show();
	}

	protected void onPostExecute(Allocatable[] allocatables) {
		if (allocatables != null) {

			// Retrieval successful, update list view
			AllocatableAdapter adapter = new AllocatableAdapter(this.context,
					this.selectedReservation, allocatables);
			this.listView.setAdapter(adapter);

			// Close progress dialog
			this.progressDialog.dismiss();

		} else {
			// Close progress dialog
			this.progressDialog.dismiss();

			// Retrieval failed, show exception dialog
			this.exceptionDialog = this.exceptionDialogFactory.create(
					this.context, R.string.exception_rapla_data_retrieval);
			this.exceptionDialog.show();
		}
	}

	/**
	 * Retrieve allocatables for given allocatable category
	 * 
	 * @param dt
	 *            Dynamic type for allocatable category
	 * @return Allocatables for given allocatable category
	 * @throws RaplaException
	 */
	protected Allocatable[] retrieveAllocatbales(DynamicType dt)
			throws RaplaException {
		ClassificationFilter filter = dt.newClassificationFilter();
		ClassificationFilter[] filters = new ClassificationFilter[] { filter };
		return this.facade.getAllocatables(filters);
	}

	/**
	 * Retrieve selected allocatable category
	 * 
	 * @return Dynamic type of selected allocatable category
	 * @throws RaplaException
	 */
	protected DynamicType retrieveSelectedAllocatableCategory()
			throws RaplaException {
		return this.facade.getDynamicType(this.allocatableCategoryElementKey);
	}
}