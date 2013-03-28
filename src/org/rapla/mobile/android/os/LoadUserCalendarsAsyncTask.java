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

import java.util.Iterator;
import java.util.Map;

import org.rapla.entities.configuration.RaplaMap;
import org.rapla.facade.ClientFacade;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.app.ExceptionDialog;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.UserCalendarAdapter;
import org.rapla.plugin.autoexport.AutoExportPlugin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * This class asyncronously loads all calendars assigned to the currently logged
 * in user and assigned them to the given list view.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class LoadUserCalendarsAsyncTask extends AsyncTask<Void, Void, String[]> {

	private Context context;
	private ListView listView;
	private ClientFacade facade;
	private String defaultCalendarName;
	private ExceptionDialogFactory exceptionDialogFactory;
	private LoadDataProgressDialogFactory progressDialogFactory;
	private Class<? extends Activity> onFailureGoTo;
	private ProgressDialog progressDialog;
	private ExceptionDialog exceptionDialog;

	/**
	 * @param context
	 *            Current context
	 * @param listView
	 *            List view to be updated
	 * @param facade
	 *            Rapla client facade for retrieving data
	 * @param defaultCalendarName String of default calendar name
	 * @param exceptionDialogFactory
	 *            Factory class for exception dialog
	 * @param progressDialogFactory
	 *            Factory class for progress dialog
	 * @param onFailureGoTo
	 *            Activity to be called upon failure
	 */
	public LoadUserCalendarsAsyncTask(Context context, ListView listView,
			ClientFacade facade, String defaultCalendarName, ExceptionDialogFactory exceptionDialogFactory,
			LoadDataProgressDialogFactory progressDialogFactory,
			Class<? extends Activity> onFailureGoTo) {
		this.context = context;
		this.listView = listView;
		this.facade = facade;
		this.defaultCalendarName = defaultCalendarName;
		this.exceptionDialogFactory = exceptionDialogFactory;
		this.progressDialogFactory = progressDialogFactory;
		this.onFailureGoTo = onFailureGoTo;
	}

	protected void onPreExecute() {
		// Initialize progress dialog
		this.progressDialog = this.progressDialogFactory.create(this.context);
		this.progressDialog.show();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected String[] doInBackground(Void... notUsed) {
		String[] result = null;
		try {
			// Get map with calendars
			Map exportMap = ((RaplaMap) facade.getPreferences().getEntry(
					AutoExportPlugin.PLUGIN_ENTRY));

			if (exportMap != null) {
				// Initialize string array and set first element to 'default'
				// +1 as 'default' calendar needs to be added
				result = new String[exportMap.size() + 1];
				result[0] = this.defaultCalendarName;

				// Add values from map to string array
				Iterator it = exportMap.keySet().iterator();
				for (int i = 1; i < result.length; i++) {
					result[i] = it.next().toString();
				}
			} else {
				// If no custom calendars where found, return only the default one
				result = new String[1];
				result[0] = this.defaultCalendarName;
			}
		} catch (Exception e) {
			// Set result explicitly to null to indicate that an error has
			// occured
			result = null;
		}
		return result;
	}

	protected void onPostExecute(String[] calendars) {
		if (calendars != null) {
			// Data retrieval successful, update list view
			ListAdapter adapter = new UserCalendarAdapter(this.context, R.layout.calendar_list_item,
					calendars);
			this.listView.setAdapter(adapter);

			// Close progress dialog
			this.progressDialog.dismiss();
		} else {
			// Close progress dialog
			this.progressDialog.dismiss();

			// Retrieval failed, show exception dialog
			this.exceptionDialog = this.exceptionDialogFactory.create(
					this.context, R.string.exception_rapla_data_retrieval,
					this.onFailureGoTo);
			this.exceptionDialog.show();
		}
	}

}
