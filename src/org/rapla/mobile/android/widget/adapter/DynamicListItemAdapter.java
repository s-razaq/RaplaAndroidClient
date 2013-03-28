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

import org.rapla.mobile.android.widget.DynamicListItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Adapter for binding an array of <code>DynamicListItem</code> objects to a
 * list view.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class DynamicListItemAdapter extends ArrayAdapter<DynamicListItem> {

	/**
	 * @param context
	 *            The current context
	 * @param objects
	 *            List items to be bound
	 */
	public DynamicListItemAdapter(Context context, DynamicListItem[] objects) {
		super(context, android.R.layout.test_list_item, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return this.getItem(position).getListItemView();
	}

}
