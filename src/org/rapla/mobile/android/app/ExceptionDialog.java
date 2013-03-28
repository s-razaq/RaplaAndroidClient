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

package org.rapla.mobile.android.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * This class provides functionality to notify the user that the application has
 * reached an unexpected state.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class ExceptionDialog extends AlertDialog {

	protected Class<? extends Activity> onFailureGoTo;

	/**
	 * @param context
	 *            Current context
	 * @param message
	 *            Message to be displayed in the dialog
	 * @param onFailureGoTo
	 *            Activity to be called upon pressing ok
	 */
	public ExceptionDialog(Context context, String message,
			Class<? extends Activity> onFailureGoTo) {
		super(context);

		this.onFailureGoTo = onFailureGoTo;

		this.setTitle(org.rapla.mobile.android.R.string.exception);
		this.setMessage(message);
		this.setIcon(android.R.drawable.ic_dialog_alert);
		this.setCancelable(false);
		this.setButton(BUTTON_NEUTRAL,
				context.getText(org.rapla.mobile.android.R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ExceptionDialog.this.close();
					}
				});
	}

	/**
	 * @param context
	 *            Current context
	 * @param message
	 *            Message to be displayed in the dialog
	 */
	public ExceptionDialog(Context context, String message) {
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
	public ExceptionDialog(Context context, int resId,
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
	public ExceptionDialog(Context context, Throwable t,
			Class<? extends Activity> onFailureGoTo) {
		this(context, t.getMessage(), onFailureGoTo);
	}

	/**
	 * @param context
	 *            Current context
	 * @param resId
	 *            Resource id of message to be displayed in the dialog
	 */
	public ExceptionDialog(Context context, int resId) {
		this(context, context.getString(resId), null);
	}

	/**
	 * @param context
	 *            Current context
	 * @param t
	 *            Throwable to be converted to string to be displayed in the
	 *            dialog
	 */
	public ExceptionDialog(Context context, Throwable t) {
		this(context, t.getMessage(), null);
	}

	/**
	 * Close exception dialog and go to action if specified
	 */
	public void close() {
		if (this.onFailureGoTo != null) {
			this.getContext().startActivity(
					new Intent(this.getContext(), this.onFailureGoTo));
		}
	}

}
