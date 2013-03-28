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

import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;

import android.content.Context;

/**
 * MockLoadDataProgressDialogFactory
 * 
 * Mocked load data progress dialog factory.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockLoadDataProgressDialogFactory extends
		LoadDataProgressDialogFactory {

	protected static MockLoadDataProgressDialogFactory instance;
	public MockLoadDataProgressDialog lastInstance;

	protected MockLoadDataProgressDialogFactory() {

	}

	public static MockLoadDataProgressDialogFactory getInstance() {
		if (instance == null) {
			instance = new MockLoadDataProgressDialogFactory();
		}
		return instance;
	}

	public MockLoadDataProgressDialog create(Context context) {
		this.lastInstance = new MockLoadDataProgressDialog(context);
		return this.lastInstance;
	}

}
