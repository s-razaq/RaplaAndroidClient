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

import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.RuntimeStorage;
import org.rapla.mobile.android.utility.RaplaConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The base activity class provides methods and logic that is required across
 * multiple activities of this application. All activities are required to
 * extend this base class.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public abstract class BaseActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Request window feature to enable custom title bar
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

	}

	/**
	 * Set title in title bar (prefix by 'Rapla')
	 * 
	 * @param resId
	 *            Resource identifier for title
	 */
	public void setTitle(int resId) {
		this.setTitle(this.getString(resId));
	}

	/**
	 * Set title in title bar (prefix by 'Rapla')
	 * 
	 * @param title
	 *            Title as string
	 */
	public void setTitle(String titleSuffix) {
		// Request custom title
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				org.rapla.mobile.android.R.layout.titlebar);

		// Get reference to title text view
		TextView title = (TextView) this.getWindow().findViewById(
				org.rapla.mobile.android.R.id.title_text);

		// Set title by replacing the suffix
		title.setText(String.format(this
				.getString(org.rapla.mobile.android.R.string.titlebar_title),
				titleSuffix));

		// Register listener on icon
		ImageView icon = (ImageView) this.getWindow().findViewById(
				org.rapla.mobile.android.R.id.title_logo);
		icon.setOnClickListener(new TitleBarIconOnClickListener(this));
	}

	/**
	 * Get custom application
	 * 
	 * @return Custom application instance
	 */
	public RaplaMobileApplication getCustomApplication() {
		return (RaplaMobileApplication) this.getApplication();
	}

	/**
	 * Get facade
	 * 
	 * @return Ready to use client facade instance
	 */
	public ClientFacade getFacade() {
		return ((RaplaConnection) this.getCustomApplication().storageGet(
				RaplaConnection.IDENTIFIER)).getFacade();
	}

	/**
	 * Get rapla context
	 * 
	 * @return Current rapla context
	 */
	public RaplaContext getRaplaContext() {
		return ((RaplaConnection) this.getCustomApplication().storageGet(
				RaplaConnection.IDENTIFIER)).getContext();
	}

	/**
	 * Get the selected reservation that all other activities work with
	 * 
	 * @return Selected reservation (editable)
	 */
	public ReservationImpl getSelectedReservation() {
		return (ReservationImpl) this.getCustomApplication().storageGet(
				RuntimeStorage.IDENTIFIER_SELECTED_RESERVATION);
	}

	/**
	 * Set selected reservation to make it available for subsequent activities
	 * 
	 * @param reservation
	 *            Reservation to be edited on subsequent screens
	 */
	public void setSelectedReservation(ReservationImpl reservation) {
		this.getCustomApplication().storageSet(
				RuntimeStorage.IDENTIFIER_SELECTED_RESERVATION, reservation);
	}

	/**
	 * Click listener that starts the home activity.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class TitleBarIconOnClickListener implements
			View.OnClickListener {

		private Context context;

		public TitleBarIconOnClickListener(Context context) {
			this.context = context;
		}

		public void onClick(View v) {
			// Get target activity
			Class<? extends Activity> goTo;
			if (RaplaMobileApplication.USE_DEMO_HOME) {
				goTo = DummyHomeActivity.class;
			} else {
				goTo = UserCalendarListActivity.class;
			}

			// Go to activity
			Intent intent = new Intent(this.context, goTo);
			this.context.startActivity(intent);
		}

	}
}
