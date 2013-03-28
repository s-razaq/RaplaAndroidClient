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

import java.util.Comparator;
import java.util.Locale;

import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.mobile.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class binds an allocatable category array and a string array to a list
 * view
 * 
 * @author Patrick Zorn <dev@patrickzorn.de>
 * 
 */
public class AllocatableCategoryAdapter
		extends
		ArrayAdapter<org.rapla.mobile.android.widget.adapter.AllocatableCategoryAdapter.AllocatableCategoryListItem> {

	public AllocatableCategoryAdapter(Context context,
			AllocatableCategoryListItem[] objects) {
		super(context, R.layout.allocation_list_item, 0, objects);
		this.sort(new AllocatableCategoryListItemComparator());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Make sure that convertView is not null
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.allocation_list_item, null);
		}

		// Get references to TextViews on UI
		TextView itemText = (TextView) convertView
				.findViewById(R.id.allocation_list_item_text);
		TextView itemDescription = (TextView) convertView
				.findViewById(R.id.allocation_list_item_details);

		// Get current item
		AllocatableCategoryListItem item = this.getItem(position);

		// Set text views
		itemText.setText(item.getDynamicType().getName(Locale.getDefault())
				.toString());
		itemDescription
				.setText(String
						.format(this
								.getContext()
								.getString(
										R.string.allocatable_category_x_of_y_allocatables_selected),
								item.getNumSelectedAllocatables(), item
										.getNumAllocatables()));

		return convertView;
	}

	/**
	 * List item to capture a dynamic type, the number of actual allocatables
	 * and the number of allocatables selected by the reservation.
	 * 
	 * @author Patrick Zorn <dev@patrickzorn.de>
	 * 
	 */
	public static class AllocatableCategoryListItem {

		private DynamicType dt;
		private int numAllocatables;
		private int numSelectedAllocatables;

		public AllocatableCategoryListItem(DynamicType dt) {
			this.dt = dt;
		}

		public DynamicType getDynamicType() {
			return dt;
		}

		public void setDynamicType(DynamicType dt) {
			this.dt = dt;
		}

		public int getNumAllocatables() {
			return numAllocatables;
		}

		public void setNumAllocatables(int numAllocatables) {
			this.numAllocatables = numAllocatables;
		}

		public int getNumSelectedAllocatables() {
			return numSelectedAllocatables;
		}

		public void setNumSelectedAllocatables(int numSelectedAllocatables) {
			this.numSelectedAllocatables = numSelectedAllocatables;
		}
	}

	/**
	 * Comparator class for sorting allocatable category list item by name.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 */
	public static class AllocatableCategoryListItemComparator
			implements
			Comparator<org.rapla.mobile.android.widget.adapter.AllocatableCategoryAdapter.AllocatableCategoryListItem> {

		public int compare(AllocatableCategoryListItem lhs,
				AllocatableCategoryListItem rhs) {
			try {
				return lhs
						.getDynamicType()
						.getName(Locale.getDefault())
						.compareTo(
								rhs.getDynamicType().getName(
										Locale.getDefault()));
			} catch (RuntimeException e) {
				return 0;
			}
		}

	}

}
