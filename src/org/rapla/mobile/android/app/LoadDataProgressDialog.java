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

import android.app.ProgressDialog;
import android.content.Context;

/**
 * This class provides functionality to show a consistent progress dialog to the
 * user while data are being retrieved.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class LoadDataProgressDialog extends ProgressDialog {

	/**
	 * @param context Current context
	 */
	public LoadDataProgressDialog(Context context) {
		super(context);

		this.setMessage(context
				.getString(org.rapla.mobile.android.R.string.progress_dialog_loading_data));
		this.setIndeterminate(true);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
	}
}
