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

import android.test.AndroidTestCase;

/**
 * Unit tests for the auto-generated resource class in order to have unit-test
 * coverage.
 * 
 * @see org.rapla.mobile.android.R
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RTest extends AndroidTestCase {

	public void testInstantiateR() {
		R r = new R();
		assertNotNull(r);
	}

	public void testInstantiateRAttr() {
		R.attr r = new R.attr();
		assertNotNull(r);
	}

	public void testInstantiateRDrawable() {
		R.drawable r = new R.drawable();
		assertNotNull(r);
	}

	public void testInstantiateRLayout() {
		R.layout r = new R.layout();
		assertNotNull(r);
	}

	public void testInstantiateRString() {
		R.string r = new R.string();
		assertNotNull(r);
	}
}
