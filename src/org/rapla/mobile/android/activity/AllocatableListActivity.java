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

package org.rapla.mobile.android.activity;

import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.framework.RaplaException;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.widget.adapter.AllocatableCategoryAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * The allocatable list screen allows the user to select a specific category then
 * the screen will call a detail screen with all resources which are belong to
 * chosen category.
 * 
 * @author Patrick Zorn <dev@patrickzorn.de>
 */
public class AllocatableListActivity extends BaseActivity {

	private ListView allocationListView;
	private AllocatableCategoryAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set content and custom title
		this.setContentView(R.layout.allocation_list);
		this.setTitle(R.string.titlebar_title_allocatable_list);

		// Initialize references to view
		allocationListView = (ListView) findViewById(R.id.allocation_list);
		allocationListView.setEmptyView(findViewById(android.R.id.empty));
		allocationListView
				.setOnItemClickListener(new AllocationItemClickListener());

	}

	@Override
	public void onResume() {
		super.onResume();
		refreshListView();

	}

	/**
	 * This method refreshes the list view by retrieving the latest data from
	 * the selected reservation
	 */
	public void refreshListView() {
		try {
			AllocatableCategoryAdapter.AllocatableCategoryListItem[] listItems = null;

			// get resources
			DynamicType[] resources = getSelectedAllocatableCategory(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);
			DynamicType[] persons = getSelectedAllocatableCategory(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_PERSON);

			// Initialize list item array
			listItems = new AllocatableCategoryAdapter.AllocatableCategoryListItem[resources.length
					+ persons.length];

			// Buffer selected allocatables
			Allocatable[] selectedAllocatables = this.getSelectedReservation()
					.getAllocatables();

			// Fill list item array initially
			int offset = 0;
			int numSelectedAllocatables = 0;
			int numAllocatables = 0;
			DynamicType dt;
			AllocatableCategoryAdapter.AllocatableCategoryListItem listItem;
			for (int j = 0; j < listItems.length; j++) {
				// Get dyanmic type
				if (j < resources.length) {
					dt = resources[j];
				} else {
					offset = resources.length;
					dt = persons[j - offset];
				}

				// Get number of allocatables
				numAllocatables = this.getFacade().getAllocatables(
						new ClassificationFilter[] { dt
								.newClassificationFilter() }).length;

				// Get number of selected allocatables
				numSelectedAllocatables = 0;
				for (int k = 0; k < selectedAllocatables.length; k++) {
					if (selectedAllocatables[k].getClassification().getType()
							.isIdentical(dt))
						numSelectedAllocatables++;
				}

				// Create list item
				listItem = new AllocatableCategoryAdapter.AllocatableCategoryListItem(
						dt);
				listItem.setNumAllocatables(numAllocatables);
				listItem.setNumSelectedAllocatables(numSelectedAllocatables);
				listItems[j] = listItem;
			}

			// Set list adapter
			this.adapter = new AllocatableCategoryAdapter(this, listItems);
			this.allocationListView.setAdapter(this.adapter);

		} catch (RaplaException e) {
			ExceptionDialogFactory.getInstance().create(this,
					R.string.exception_internal_error);
			finish();
		}

	}

	/**
	 * This method returns the dynamic types of all resource categories
	 * 
	 * @param type
	 *            stands for the DynamicType
	 * 
	 * @return DynamicType[]
	 * @throws RaplaException
	 */
	public DynamicType[] getSelectedAllocatableCategory(String type)
			throws RaplaException {
		return this.getFacade().getDynamicTypes(type);
	}

	/**
	 * AllocationItemClickListener
	 * 
	 * This class handles click events on allocation resource list items. A
	 * regular click displays the details screen for the selected resource
	 * category.
	 * 
	 */
	private class AllocationItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			// Get list item
			AllocatableCategoryAdapter.AllocatableCategoryListItem listItem = adapter
					.getItem(position);

			// Go to edit view for the selected allocation
			Intent i = new Intent(getApplicationContext(),
					AllocatableDetailsActivity.class);
			i.putExtra(
					AllocatableDetailsActivity.INTENT_STRING_ALLOCATABLE_CATEGORY_ELEMENT_KEY,
					listItem.getDynamicType().getElementKey());
			startActivity(i);
		}

	}

}
