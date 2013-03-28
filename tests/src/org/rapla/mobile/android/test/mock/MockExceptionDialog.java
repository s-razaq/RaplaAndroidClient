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

import android.app.Activity;
import android.content.Context;

/**
 * MockExceptionDialog
 * 
 * Mocked exception dialog.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockExceptionDialog extends ExceptionDialog {
	
	public boolean shown = false;

	public MockExceptionDialog(Context context, String message,
			Class<? extends Activity> onFailureGoTo) {
		super(context, message, onFailureGoTo);
	}

	/**
	 * @param context
	 *            Current context
	 * @param message
	 *            Message to be displayed in the dialog
	 */
	public MockExceptionDialog(Context context, String message) {
		this(context, message, null);
	}

	/**
	 * @param context
	 *            Current context
	 * @param resId
	 *            Resource id of message to be displayed in the dialog
	 * @param onFailureGoTo
	 *            Activity to be called upon pressing ok
	 */
	public MockExceptionDialog(Context context, int resId,
			Class<? extends Activity> onFailureGoTo) {
		this(context, context.getString(resId), onFailureGoTo);
	}

	/**
	 * @param context
	 *            Current context
	 * @param t
	 *            Throwable to be converted to string to be displayed in the
	 *            dialog
	 * @param onFailureGoTo
	 *            Activity to be called upon pressing ok
	 */
	public MockExceptionDialog(Context context, Throwable t,
			Class<? extends Activity> onFailureGoTo) {
		this(context, t.getMessage(), onFailureGoTo);
	}

	/**
	 * @param context
	 *            Current context
	 * @param resId
	 *            Resource id of message to be displayed in the dialog
	 */
	public MockExceptionDialog(Context context, int resId) {
		this(context, context.getString(resId), null);
	}

	/**
	 * @param context
	 *            Current context
	 * @param t
	 *            Throwable to be converted to string to be displayed in the
	 *            dialog
	 */
	public MockExceptionDialog(Context context, Throwable t) {
		this(context, t.getMessage(), null);
	}
	
	/**
	 * Overwrite show to work in unit test environment
	 */
	public void show() {
		this.shown = true;
	}
}
