/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Patrick Zorn                                          |
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

import java.util.Locale;

import org.rapla.entities.dynamictype.DynamicType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * List view adapter class for binding a set of rapla dynamic type objects to a
 * list view.
 * 
 * @author Patrick Zorn <dev@patrickzorn.de>
 * 
 */
public class RaplaDynamicTypeAdapter extends ArrayAdapter<DynamicType> {

	private int resId;
	private int textViewResId;
	private int dropdownViewRes = android.R.layout.simple_spinner_dropdown_item;
	
	/**
	 * @param context
	 *            The current context
	 * @param objects
	 *            Set of dynamic type objects
	 */
	public RaplaDynamicTypeAdapter(Context context, int resId, int textViewResId, DynamicType[] objects) {
		super(context, resId, textViewResId, objects);
		this.resId = resId;
		this.textViewResId = textViewResId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Make sure that convertView is not null
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(this.resId, null);
		}

		// Bind dynamic type name to list item text view
		TextView label = (TextView) convertView
				.findViewById(this.textViewResId);
		label.setText(this.getItem(position).getName(Locale.getDefault()));

		return convertView;
	}
	
	public View getDropDownView (int position, View convertView, ViewGroup parent) {
		// Make sure that convertView is not null
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(this.dropdownViewRes, null);
		}

		// Bind dynamic type name to list item text view
		TextView label = (TextView) convertView
				.findViewById(this.textViewResId);
		label.setText(this.getItem(position).getName(Locale.getDefault()));

		return convertView;
	}
	
	public void setDropDownViewResource(int res) {
		this.dropdownViewRes = res;
		super.setDropDownViewResource(res);
	}

}
