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

import org.rapla.mobile.android.app.ExceptionDialog;

import android.app.Activity;
import android.content.Context;

/**
 * ExceptionDialogFactory (Singleton)
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 *
 */
public class ExceptionDialogFactory {

	protected static ExceptionDialogFactory instance;

	protected ExceptionDialogFactory() {

	}

	public static ExceptionDialogFactory getInstance() {
		if (instance == null) {
			instance = new ExceptionDialogFactory();
		}
		return instance;
	}

	public ExceptionDialog create(Context context, int resId) {
		return new ExceptionDialog(context, resId);
	}

	public ExceptionDialog create(Context context, String message) {
		return new ExceptionDialog(context, message);
	}

	public ExceptionDialog create(Context context, Throwable t) {
		return new ExceptionDialog(context, t);
	}

	public ExceptionDialog create(Context context, int resId,
			Class<? extends Activity> onFailureGoTo) {
		return new ExceptionDialog(context, resId, onFailureGoTo);
	}

	public ExceptionDialog create(Context context, String message,
			Class<? extends Activity> onFailureGoTo) {
		return new ExceptionDialog(context, message, onFailureGoTo);
	}

	public ExceptionDialog create(Context context, Throwable t,
			Class<? extends Activity> onFailureGoTo) {
		return new ExceptionDialog(context, t, onFailureGoTo);
	}

}
