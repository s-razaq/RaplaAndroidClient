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

package org.rapla.mobile.android.browser.integration;

import java.util.List;

import org.rapla.entities.domain.Reservation;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.entities.storage.RefEntity;
import org.rapla.entities.storage.internal.SimpleIdentifier;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.RuntimeStorage;
import org.rapla.mobile.android.activity.EventDetailsActivity;
import org.rapla.storage.impl.AbstractCachableOperator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * This class handles browser intents for editing a reservation. Pattern:
 * <code>raplaclient://action/edit/reservation/{id}</code>.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class ReservationEditIntentHandler implements BrowserIntentHandler {

	/**
	 * Matches <code>raplaclient://action/edit/reservation/{id}</code>
	 * 
	 * @see org.rapla.mobile.android.browser.integration.BrowserIntentHandler#matches(android.net.Uri)
	 */
	public boolean matches(Uri data) {
		try {
			List<String> parameters = data.getPathSegments();
			String action = parameters.get(0);
			String raplaType = parameters.get(1);
			String id = parameters.get(2);

			// Check path segments
			if (action.equals("edit") && raplaType.equals("reservation")
					&& Integer.parseInt(id) > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			// Last segment is not a number
			return false;
		} catch (IndexOutOfBoundsException e) {
			// Last segment is missing
			return false;
		}
	}

	/**
	 * Handle intent by reading the reservation id, putting the reservation into
	 * the runtime storage and starting the corresponding event details screen
	 * for editing the reservation.
	 * 
	 * @see org.rapla.mobile.android.browser.integration.BrowserIntentHandler#handleIntent(android.content.Context,
	 *      org.rapla.mobile.android.RaplaMobileApplication,
	 *      android.content.Intent, org.rapla.framework.RaplaContext)
	 */
	public void handleIntent(Context context,
			RaplaMobileApplication application, Intent intent,
			RaplaContext raplaContext) throws Exception {

		// Client facade
		ClientFacade facade = raplaContext.lookup(ClientFacade.class);

		// Get reservation
		List<String> parameters = intent.getData().getPathSegments();
		int reservationId = Integer.parseInt(parameters.get(2));
		Reservation reservation = this
				.getReservationById(facade, reservationId);
		ReservationImpl editableReservation = (ReservationImpl) facade
				.edit(reservation);

		// Put reservation into runtime storage
		application.storageSet(RuntimeStorage.IDENTIFIER_SELECTED_RESERVATION,
				editableReservation);

		// Create intent and start activity
		Intent i = new Intent(context, EventDetailsActivity.class);
		context.startActivity(i);
	}

	/**
	 * Get a reservation for a given id
	 * 
	 * @param facade
	 *            ClientFacade
	 * @param intId
	 *            Identifier of reservation
	 * @return Reservation with the specified it
	 */
	public Reservation getReservationById(ClientFacade facade, int intId) {
		AbstractCachableOperator operator = (AbstractCachableOperator) facade
				.getOperator();
		SimpleIdentifier id = new SimpleIdentifier(Reservation.TYPE, intId);
		// Fill the cache. This means, that all reservations assigned to the
		// user are cached and hence retrieved from the server.
		// TODO Come up with a better solution
		try {
			facade.getReservations(facade.getUser(), null, null, null);
		} catch (RaplaException e) {
			e.printStackTrace();
		}
		RefEntity<?> refEntity = operator.getCache().get(id);
		return (Reservation) refEntity;
	}
}
