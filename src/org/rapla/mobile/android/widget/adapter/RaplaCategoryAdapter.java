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

import java.util.Locale;

import org.rapla.entities.Category;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.app.RaplaCategoryChoiceDialog;
import org.rapla.mobile.android.app.RaplaCategoryChoiceDialog.RaplaCategoryOnClickListenerFactory;
import org.rapla.mobile.android.utility.CompoundButtonGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * The adapter binds a set of rapla categories to a list view. A list item
 * consists of the category name and a checkbox which allows for selecting one
 * category at a time.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaCategoryAdapter extends ArrayAdapter<Category> {

	private Category selected;
	private CompoundButtonGroup buttonGroup;
	private RaplaCategoryOnClickListenerFactory onClickListenerFactory;
	private RaplaCategoryChoiceDialog dialog;

	/**
	 * @param context
	 *            The current context
	 * @param resId
	 *            The resource id of the item layout
	 * @param objects
	 *            Array of category objects to be displayed
	 * @param selected
	 *            Selected category
	 * @param buttonGroup
	 *            Reference to button group
	 * @param onClickListenerFactory
	 *            Reference to the click listener factory
	 * @param dialog
	 *            Reference to the category choice dialog
	 */
	public RaplaCategoryAdapter(Context context, int resId, Category[] objects,
			Category selected, CompoundButtonGroup buttonGroup,
			RaplaCategoryOnClickListenerFactory onClickListenerFactory,
			RaplaCategoryChoiceDialog dialog) {
		super(context, resId, objects);

		// Set instance variables
		this.selected = selected;
		this.buttonGroup = buttonGroup;
		this.onClickListenerFactory = onClickListenerFactory;
		this.dialog = dialog;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Make sure that convertView is not null
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(
					R.layout.rapla_category_drill_down_list_item, null);
		}

		// Get category instance
		Category item = this.getItem(position);
		int subCategories = item.getCategories().length;

		// Get ui widget references
		CheckBox radio = (CheckBox) convertView.findViewById(R.id.radio);
		TextView label = (TextView) convertView
				.findViewById(android.R.id.text1);
		TextView sublabel = (TextView) convertView
				.findViewById(android.R.id.text2);

		// Set category text to first list item line
		label.setText(item.getName(Locale.getDefault()));

		// Set either the number of sub categories or 'no categories' to second
		// list item line
		if (subCategories == 0) {
			sublabel.setText(R.string.no_subcategories);
		} else {
			sublabel.setText(String.format(
					this.getContext().getString(R.string.x_subcategories),
					subCategories));
		}

		// Register decorated compound button at button group and pass the
		// checked-status
		boolean value = item.isIdentical(this.selected);
		this.buttonGroup.addCompoundButton(radio,
				value, item);
		
		// Register listener
		label.setOnClickListener(this.onClickListenerFactory.create(
				this.dialog, position));

		return convertView;
	}
}