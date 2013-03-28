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

package org.rapla.mobile.android.widget.adapter;

import java.util.Comparator;

import org.rapla.mobile.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class binds a user's calendar item to a list item.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class UserCalendarAdapter extends ArrayAdapter<String> {

	/**
	 * @param context
	 *            Current context
	 * @param textViewResourceId
	 *            Resource id of list item layout
	 * @param objects
	 *            Calendar objects to be bound
	 */
	public UserCalendarAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		
		// Sort calendars
		this.sort(new Comparator<String>() {

			public int compare(String lhs, String rhs) {
				return lhs.compareTo(rhs);
			}
		});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Make sure that convertView is not null
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.calendar_list_item, null);
		}

		// Get references to TextViews on UI
		TextView itemText = (TextView) convertView
				.findViewById(R.id.calendar_list_item_text);

		// Assign text to list item
		itemText.setText(this.getItem(position));

		return convertView;
	}
}
