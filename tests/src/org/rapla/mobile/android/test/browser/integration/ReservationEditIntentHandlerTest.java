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

package org.rapla.mobile.android.test.browser.integration;

import org.rapla.entities.domain.Reservation;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.mobile.android.browser.integration.ReservationEditIntentHandler;
import org.rapla.mobile.android.test.mock.MockCachableOperator;
import org.rapla.mobile.android.test.mock.MockClientFacade;
import org.rapla.mobile.android.test.mock.MockContext;
import org.rapla.mobile.android.test.mock.MockLocalCache;
import org.rapla.mobile.android.test.mock.MockRaplaContext;
import org.rapla.mobile.android.test.mock.MockRaplaMobileApplication;
import org.rapla.mobile.android.test.test.FixtureHelper;

import android.content.Intent;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * ReservationEditIntentHandlerTest
 * 
 * Unit test class for
 * <code>org.rapla.mobile.android.browser.integration.ReservationEditIntentHandler</code>
 * 
 * @see org.rapla.mobile.android.browser.integration.ReservationEditIntentHandler
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class ReservationEditIntentHandlerTest extends AndroidTestCase {

	protected ReservationEditIntentHandler handler;

	protected void setUp() throws Exception {
		super.setUp();

		this.handler = new ReservationEditIntentHandler();
	}

	public void testMatcheShouldReturnTrueForValidUri() {
		assertTrue(this.handler.matches(this.getMatchingUri()));
	}

	public void testMatchesShouldReturnFalseForUriWithTypo() {
		assertFalse(this.handler.matches(this.getNotMatchingUriWithTypo()));
	}

	public void testMatchesShouldReturnFalseForUriWithNotANumber() {
		assertFalse(this.handler
				.matches(this.getNotMatchingUriWithNotANumber()));
	}

	public void testMatchesShouldReturnFalseForUriWithMissingSegment() {
		assertFalse(this.handler.matches(this
				.getNotMatchingUriWithMissingSegment()));
	}

	private Uri getMatchingUri() {
		StringBuilder sb = new StringBuilder();
		sb.append("rapla-client://action/edit/reservation/2");
		return Uri.parse(sb.toString());
	}

	private Uri getNotMatchingUriWithTypo() {
		StringBuilder sb = new StringBuilder();
		sb.append("rapla-client://action/edit/reservatio/2");
		// Typo, hence not matching
		return Uri.parse(sb.toString());
	}

	private Uri getNotMatchingUriWithNotANumber() {
		StringBuilder sb = new StringBuilder();
		sb.append("rapla-client://action/edit/reservation/NaN");
		// Typo, hence not matching
		return Uri.parse(sb.toString());
	}

	private Uri getNotMatchingUriWithMissingSegment() {
		StringBuilder sb = new StringBuilder();
		sb.append("rapla-client://action/edit/reservation");
		// Typo, hence not matching
		return Uri.parse(sb.toString());
	}

	public void testHandleIntentShouldStoreCorrespondingReservationInApplicationStorage()
			throws Exception {
		ReservationImpl reservation = FixtureHelper.createReservation();
		MockContext context = new MockContext();
		MockRaplaMobileApplication application = new MockRaplaMobileApplication();
		Intent intent = new Intent().setData(this.getMatchingUri());
		MockRaplaContext raplaContext = new MockRaplaContext();

		// Prepare cache mock
		MockClientFacade facade = (MockClientFacade) raplaContext
				.lookup(ClientFacade.ROLE);
		facade.context = raplaContext;
		MockCachableOperator operator = (MockCachableOperator) facade
				.getOperator();
		MockLocalCache localCache = (MockLocalCache) operator.getCache();
		localCache.getReturn = reservation;

		// Reset variables
		application.lastStorageObject = null;

		// Handle Intent
		this.handler.handleIntent(context, application, intent, raplaContext);

		assertSame(reservation, application.lastStorageObject);
	}

	public void testGetReservationById() {
		// Prepare cache mock
		ReservationImpl reservation = FixtureHelper.createReservation();
		MockClientFacade facade = new MockClientFacade();
		facade.context = new MockRaplaContext();
		MockCachableOperator operator = (MockCachableOperator) facade
				.getOperator();
		MockLocalCache localCache = (MockLocalCache) operator.getCache();
		localCache.getReturn = reservation;

		Reservation result = this.handler.getReservationById(facade, 3);
		assertSame(result, reservation);
	}
}
