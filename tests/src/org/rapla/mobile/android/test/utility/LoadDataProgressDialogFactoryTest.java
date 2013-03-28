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

package org.rapla.mobile.android.test.utility;

import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.app.LoadDataProgressDialog;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * LoadDataProgressDialogFactoryTest
 * 
 * Unit test class for
 * org.rapla.mobile.android.utilities.factory.LoadDataProgressDialogFactory
 * 
 * @see org.rapla.mobile.android.utility.factory.utilities.factory.LoadDataProgressDialogFactory
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class LoadDataProgressDialogFactoryTest extends AndroidTestCase {

	public void testGetInstanceShouldAlwaysReturnTheSameInstance() {
		LoadDataProgressDialogFactory first = LoadDataProgressDialogFactory
				.getInstance();
		LoadDataProgressDialogFactory second = LoadDataProgressDialogFactory
				.getInstance();
		assertEquals(first, second);
	}

	public void testCreateShouldAlwaysReturnNewInstance()
			throws RaplaMobileException {
		Context context = this.getContext();
		LoadDataProgressDialog first = LoadDataProgressDialogFactory
				.getInstance().create(context);
		LoadDataProgressDialog second = LoadDataProgressDialogFactory
				.getInstance().create(context);
		assertNotSame(first, second);
	}
}
