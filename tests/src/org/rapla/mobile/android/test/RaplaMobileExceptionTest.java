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

import org.rapla.mobile.android.RaplaMobileException;

import android.test.AndroidTestCase;

/**
 * RaplaMobileExceptionTest
 * 
 * Unit tests for <code>org.rapla.mobile.android.RaplaMobileException</code>.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * @see org.rapla.mobile.android.RaplaMobileException
 */
public class RaplaMobileExceptionTest extends AndroidTestCase {

	public void testConstructor() {
		RaplaMobileException e = new RaplaMobileException();
		assertNotNull(e);
	}

	public void testConstructorWithString() {
		RaplaMobileException e = new RaplaMobileException("exception");
		assertNotNull(e);
	}

	public void testConstructorWithStringAndException() {
		RaplaMobileException e = new RaplaMobileException("exception",
				new Exception());
		assertNotNull(e);
	}

	public void testConstructorWithException() {
		RaplaMobileException e = new RaplaMobileException(new Exception());
		assertNotNull(e);
	}

	public void testgetPreviousShouldReturnPreviousException() {
		Exception previous = new Exception();
		RaplaMobileException e = new RaplaMobileException("exception", previous);
		assertSame(previous, e.getPrevious());
	}
}
