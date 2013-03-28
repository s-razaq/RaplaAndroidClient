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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.rapla.framework.RaplaLocale;

/**
 * MockRaplaLocale
 * 
 * Mocked rapla locale
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class MockRaplaLocale implements RaplaLocale {

	public String[] getAvailableLanguages() {
		
		return null;
	}

	public Calendar createCalendar() {
		
		return null;
	}

	public String formatTime(Date date) {
		
		return null;
	}

	public Date toDate(Date date, boolean fillDate) {
		
		return null;
	}

	public Date toDate(int year, int month, int date) {
		
		return null;
	}

	public Date toTime(int hour, int minute, int second) {
		
		return null;
	}

	public Date toDate(Date date, Date time) {
		
		return null;
	}

	public String formatNumber(long number) {
		
		return null;
	}

	public String formatDateShort(Date date) {
		
		return null;
	}

	public String formatDate(Date date) {
		
		return null;
	}

	public String formatDateLong(Date date) {
		
		return null;
	}

	public String getWeekday(Date date) {
		
		return null;
	}

	public String getMonth(Date date) {
		
		return null;
	}

	public String getCharsetNonUtf() {
		
		return null;
	}

	public TimeZone getTimeZone() {
		
		return null;
	}

	public Locale getLocale() {
		return new Locale("de");
	}

}
