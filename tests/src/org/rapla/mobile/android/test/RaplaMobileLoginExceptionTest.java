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

package org.rapla.mobile.android.test;

import org.rapla.mobile.android.RaplaMobileLoginException;

import android.test.AndroidTestCase;

/**
 * RaplaMobileLoginExceptionTest
 * 
 * Unit tests for
 * <code>org.rapla.mobile.android.RaplaMobileLoginException</code>.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * @see org.rapla.mobile.android.RaplaMobileLoginException
 */
public class RaplaMobileLoginExceptionTest extends AndroidTestCase {

	public void testConstructorWithException() {
		RaplaMobileLoginException e = new RaplaMobileLoginException(
				new Exception());
		assertNotNull(e);
	}
}
