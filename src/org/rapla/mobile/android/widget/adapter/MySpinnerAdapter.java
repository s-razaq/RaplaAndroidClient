/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Saqib Razaq                                           |
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

package org.rapla.mobile.android.widget.adapter;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.widget.ArrayAdapter;

public class MySpinnerAdapter extends ArrayAdapter<String> {

	public MySpinnerAdapter(String[] values, Context context) {
		super(context,
				android.R.layout.simple_spinner_item, values);
	}

	public MySpinnerAdapter(String value, Context context) {
		super(context, android.R.layout.simple_spinner_item);
		super.insert(value, 0);
	}

	public MySpinnerAdapter(Date value, Context context) {
		super(context, android.R.layout.simple_spinner_item);
		String strDate = DateFormat.format("MMMM dd, yyyy", value)
				.toString();
		super.insert(strDate, 0);
	}
	
	public MySpinnerAdapter(Time start, Time end, Context context) {
		super(context, android.R.layout.simple_spinner_item);
		String strStart = DateFormat.format("h:mmaa", start.toMillis(true))
				.toString();
		String strEnd = DateFormat.format("h:mmaa", end.toMillis(true))
				.toString();
		super.insert(strStart + " - " + strEnd, 0);
	}
	

}
