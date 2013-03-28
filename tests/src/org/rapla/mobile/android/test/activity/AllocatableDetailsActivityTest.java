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

package org.rapla.mobile.android.test.activity;

import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.activity.AllocatableDetailsActivity;
import org.rapla.mobile.android.test.mock.MockClientFacade;
import org.rapla.mobile.android.test.test.CustomActivityInstrumentationTestCase;
import org.rapla.mobile.android.test.test.FixtureHelper;
import org.rapla.mobile.android.widget.adapter.AllocatableAdapter;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

/**
 * AllocatableDetailsActivityTest
 * 
 * Unit tests for <code>AllocatableDetailsActivity</code>
 * 
 * @see org.rapla.mobile.android.activity.AllocatableDetailsActivity
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class AllocatableDetailsActivityTest extends
		CustomActivityInstrumentationTestCase<AllocatableDetailsActivity> {

	public static final int VALID_ALLOCATABLE_CATEGORY_ID = 0;
	public static final int INVALID_ALLOCATABLE_CATEGORY_ID = -1;

	public AllocatableDetailsActivityTest() {
		super("org.rapla.mobile.android.activity",
				AllocatableDetailsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	private void initializeValidAllocatableCategory() {
		Intent i = new Intent();
		i.putExtra("allocationCategory", VALID_ALLOCATABLE_CATEGORY_ID);
		i.putExtra(AllocatableDetailsActivity.INTENT_STRING_ALLOCATABLE_CATEGORY_ELEMENT_KEY, "default");
		this.setActivityIntent(i);
	}

	private void initializeInvalidAllocatableCategory() {
		Intent i = new Intent();
		i.putExtra("allocationCategory", INVALID_ALLOCATABLE_CATEGORY_ID);
		this.setActivityIntent(i);
	}

	public void testPreConditions() {
		this.initializeValidAllocatableCategory();

		// Check activity class
		AllocatableDetailsActivity activity = this.getActivity();
		assertNotNull(activity);

		// Check application class
		Application app = activity.getApplication();
		assertNotNull(app);
		assertEquals(RaplaMobileApplication.class, app.getClass());

		// Check list view
		View v = activity.findViewById(R.id.allocatable_details_list);
		assertNotNull(v);
		ListView listView = (ListView) v;
		assertNotNull(listView);
		
		// Check create new classification from allocatable dynamic type
		DynamicType dt = FixtureHelper.createDynamicTypeAllocatable();
		dt.newClassification();
	}

	public void testListViewContentShouldComplyWithDataFromFacade()
			throws Exception {
		this.initializeValidAllocatableCategory();
		AllocatableDetailsActivity activity = this.getActivity();

		ListView listView = (ListView) activity
				.findViewById(R.id.allocatable_details_list);

		assertNotNull(listView);

		AllocatableAdapter adapter = null;
		// As list items are being retrieved asynchronously, try five times
		// whether the current number of list items equals the expected number.
		// If not, wait one second and try again.
		for (int i = 0; i < 5; i++) {
			// Get latest adapter
			adapter = (AllocatableAdapter) listView.getAdapter();
			// Check count
			if (adapter != null && adapter.getCount() == MockClientFacade.NUM_ALLOCATABLES) {
				break;
			} else {
				// List not updated, sleep for one second
				Thread.sleep(1000);
			}
		}
		
		assertNotNull(adapter);
		assertEquals(MockClientFacade.NUM_ALLOCATABLES, adapter.getCount());
	}

	/**
	 * 
	 * @TODO In case this activity is called with an invalid allocatable
	 *       category id, an exception dialog should be displayed. Upon pressing
	 *       ok, the user should be rerouted to the
	 *       <code>AllocatableListActivity</code>. Both functionalities should be
	 *       captured in this test case. However, it's apparently not possible
	 *       yet to test dialogs. Don't forget to remove the ignore-prefix in
	 *       the method name.
	 */
	public void ignoretestActivityShouldShowErrorDialogForInvalidAllocatableCategoryId()
			throws Exception {
		this.initializeInvalidAllocatableCategory();
	}
}
