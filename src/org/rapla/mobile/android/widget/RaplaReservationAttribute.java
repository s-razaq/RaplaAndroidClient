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

package org.rapla.mobile.android.widget;

import org.rapla.entities.domain.internal.ReservationImpl;

/**
 * Interface for custom widgets that can have a reservation being bound to.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public interface RaplaReservationAttribute {

	/**
	 * Transfer value from reservation to widget
	 * 
	 * @param reservation
	 *            Reservation to pull value from
	 */
	public void pullValueFromReservation(ReservationImpl reservation);

	/**
	 * Transfer value from widget to reservation
	 * 
	 * @param reservation
	 *            Reservation to put value
	 * @return Reference to input reservation
	 */
	public ReservationImpl putValueToReservation(ReservationImpl reservation);

	/**
	 * Bind reservation to widget
	 * 
	 * @param reservation
	 *            Reservation to bind
	 */
	public void bindReservation(ReservationImpl reservation);

	/**
	 * Check whether widget is already bound
	 * 
	 * @return True if widget is bound to a reservation, false otherwise.
	 */
	public boolean isBoundToReservation();
}
