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

import org.rapla.mobile.android.app.LoadDataProgressDialog;

import android.content.Context;

/**
 * MockLoadDataProgressDialog
 * 
 * Mocked load data progress dialog
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockLoadDataProgressDialog extends LoadDataProgressDialog {
	
	public boolean shown = false;

	public MockLoadDataProgressDialog(Context context) {
		super(context);
	}
	
	/**
	 * Overwrite show to work in unit test environment
	 */
	public void show() {
		this.shown = true;
	}
}
