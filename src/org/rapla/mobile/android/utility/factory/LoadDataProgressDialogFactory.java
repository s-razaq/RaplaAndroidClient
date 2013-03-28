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

package org.rapla.mobile.android.utility.factory;

import org.rapla.mobile.android.app.LoadDataProgressDialog;

import android.content.Context;

/**
 * LoadDataProgressDialogFactory
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class LoadDataProgressDialogFactory {

	protected static LoadDataProgressDialogFactory instance;

	protected LoadDataProgressDialogFactory() {

	}

	public static LoadDataProgressDialogFactory getInstance() {
		if (instance == null) {
			instance = new LoadDataProgressDialogFactory();
		}
		return instance;
	}

	public LoadDataProgressDialog create(Context context) {
		return new LoadDataProgressDialog(context);
	}
}
