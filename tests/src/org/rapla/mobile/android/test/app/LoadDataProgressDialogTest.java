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

package org.rapla.mobile.android.test.app;

import org.rapla.mobile.android.app.LoadDataProgressDialog;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * LoadDataProgressDialogTest
 * 
 * Unit test class for org.rapla.modile.android.app.LoadDataProgressDialog
 * 
 * @see org.rapla.mobile.android.app.LoadDataProgressDialog
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class LoadDataProgressDialogTest extends AndroidTestCase {

	protected Context context;

	protected void setUp() throws Exception {
		super.setUp();

		this.context = this.getContext();
	}

	public void testConstructorWithContext() {
		LoadDataProgressDialog d = new LoadDataProgressDialog(this.context);
		assertNotNull(d);
	}
}
