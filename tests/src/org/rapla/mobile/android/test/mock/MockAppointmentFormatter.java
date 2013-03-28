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

package org.rapla.mobile.android.test.mock;

import java.util.List;

import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.entities.domain.Repeating;

/**
 * MockAppointmentFormatter
 * 
 * Mocked appointment formatter
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockAppointmentFormatter implements AppointmentFormater {

	public String getShortSummary(Appointment appointment) {
		return "Short summary";
	}

	public String getVeryShortSummary(Appointment appointment) {
		return "Very short summary";
	}

	public String getSummary(Appointment a) {
		return "Summary";
	}
	
	@SuppressWarnings("rawtypes")
	public String getSummary(Repeating r, List periods) {
		return "Summary";
	}

	public String getSummary(Repeating r) {
		return "Summary";
	}

	public String getExceptionSummary(Repeating r) {
		return "Exception summary";
	}

}
