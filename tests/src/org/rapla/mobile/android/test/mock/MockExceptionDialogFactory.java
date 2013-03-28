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

import org.rapla.mobile.android.app.ExceptionDialog;
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

import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;

import android.app.Activity;
import android.content.Context;

/**
 * MockExceptionDialogFactory
 * 
 * Mocked exception dialog factory
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockExceptionDialogFactory extends ExceptionDialogFactory {

	protected static MockExceptionDialogFactory instance;
	public MockExceptionDialog lastInstance;

	protected MockExceptionDialogFactory() {

	}

	public static MockExceptionDialogFactory getInstance() {
		if (instance == null) {
			instance = new MockExceptionDialogFactory();
		}
		return instance;
	}

	public ExceptionDialog create(Context context, int resId) {
		this.lastInstance = new MockExceptionDialog(context, resId);
		return this.lastInstance;
	}

	public ExceptionDialog create(Context context, String message) {
		this.lastInstance = new MockExceptionDialog(context, message);
		return this.lastInstance;
	}

	public ExceptionDialog create(Context context, Throwable t) {
		this.lastInstance = new MockExceptionDialog(context, t);
		return this.lastInstance;
	}

	public ExceptionDialog create(Context context, int resId,
			Class<? extends Activity> onFailureGoTo) {
		this.lastInstance = new MockExceptionDialog(context, resId,
				onFailureGoTo);
		return this.lastInstance;
	}

	public ExceptionDialog create(Context context, String message,
			Class<? extends Activity> onFailureGoTo) {
		this.lastInstance = new MockExceptionDialog(context, message,
				onFailureGoTo);
		return this.lastInstance;
	}

	public ExceptionDialog create(Context context, Throwable t,
			Class<? extends Activity> onFailureGoTo) {
		this.lastInstance = new MockExceptionDialog(context, t, onFailureGoTo);
		return this.lastInstance;
	}

}
