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

package org.rapla.mobile.android.test.os;

import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.mobile.android.os.LoadAllocatablesAsyncTask;
import org.rapla.mobile.android.test.mock.MockClientFacade;
import org.rapla.mobile.android.test.mock.MockExceptionDialogFactory;
import org.rapla.mobile.android.test.mock.MockListView;
import org.rapla.mobile.android.test.mock.MockLoadDataProgressDialogFactory;
import org.rapla.mobile.android.test.test.FixtureHelper;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.AllocatableAdapter;

import android.app.Activity;
import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.ListView;

/**
 * LoadAllocatablesAsyncTaskTest
 * 
 * Unit test class for
 * <code>org.rapla.mobile.android.os.LoadAllocatablesAsyncTask</code>
 * 
 * @see org.rapla.mobile.android.os.LoadAllocatablesAsyncTask
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class LoadAllocatablesAsyncTaskTest extends AndroidTestCase {

	protected TestableLoadAllocatablesAsyncTask task;
	protected Context context;
	protected MockListView listView;
	protected MockClientFacade facade;
	protected MockExceptionDialogFactory exceptionDialogFactory;
	protected MockLoadDataProgressDialogFactory progressDialogFactory;
	protected ReservationImpl selectedReservation;
	protected static final String ALLOCATABLE_CATEGORY_ELEMENT_KEY = "defaultResource";

	protected void setUp() throws Exception {
		super.setUp();

		this.context = this.getContext(); // MockContext won't work
		this.listView = new MockListView(this.context);
		this.facade = new MockClientFacade();
		this.exceptionDialogFactory = MockExceptionDialogFactory.getInstance();
		this.progressDialogFactory = MockLoadDataProgressDialogFactory
				.getInstance();
		this.selectedReservation = FixtureHelper.createReservation();

		this.task = new TestableLoadAllocatablesAsyncTask(this.context,
				this.selectedReservation, this.listView, this.facade,
				ALLOCATABLE_CATEGORY_ELEMENT_KEY, this.exceptionDialogFactory,
				this.progressDialogFactory, null);
	}

	public void testExecuteRunsInTestEnvironment() throws Exception {
		this.task.execute();
	}

	public void testExecuteShouldShowProgressDialogWhileLoading()
			throws Exception {
		this.progressDialogFactory.lastInstance = null;
		this.task.execute();
		assertNotNull(this.progressDialogFactory.lastInstance);
		assertTrue(this.progressDialogFactory.lastInstance.shown);
	}

	public void testGetShouldReturnAllocatablesFromFacadeAsResultOfTask()
			throws Exception {
		this.task.execute();
		Allocatable[] allocatables = this.task.get();
		assertEquals(MockClientFacade.NUM_ALLOCATABLES, allocatables.length);
	}

	public void testExecuteShouldAssignAllocatablesFromFacadeToListView()
			throws Exception {
		this.task.execute();
		AllocatableAdapter adapter = (AllocatableAdapter) this.listView
				.getAdapter();
		assertNotNull(adapter);
		assertEquals(MockClientFacade.NUM_ALLOCATABLES, adapter.getCount());
	}

	public void testExecuteShouldShowExceptionDialogIfRetrievalOfDynamicTypeFails()
			throws Exception {
		this.exceptionDialogFactory.lastInstance = null;
		this.facade.getDynamicTypeThrowsException = true;
		this.task.execute();
		assertNotNull(this.exceptionDialogFactory.lastInstance);
		assertTrue(this.exceptionDialogFactory.lastInstance.shown);
	}

	public void testExecuteShouldShowExceptionDialogIfRetrievalOfAllocatablesFails()
			throws Exception {
		this.exceptionDialogFactory.lastInstance = null;
		this.facade.getAllocatablesThrowsException = true;
		this.task.execute();
		assertNotNull(this.exceptionDialogFactory.lastInstance);
		assertTrue(this.exceptionDialogFactory.lastInstance.shown);
	}

	public void testExecuteShouldNotShowExceptionDialogIfRetrievalOfAllocatablesSucceeds()
			throws Exception {
		this.exceptionDialogFactory.lastInstance = null;
		this.task.execute();
		assertNull(this.exceptionDialogFactory.lastInstance);
	}

	/**
	 * TestableLoadAllocatablesAsyncTask
	 * 
	 * As <code>onPostExecute</code> is executed in a different thread, this
	 * class provides a new <code>execute</code> method for executing the task
	 * in a way so that <code>onPostExecute</code> is called as well.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private class TestableLoadAllocatablesAsyncTask extends
			LoadAllocatablesAsyncTask {

		public TestableLoadAllocatablesAsyncTask(Context context,
				ReservationImpl selectedReservation, ListView listView,
				ClientFacade facade, String allocatableCategoryElementKey,
				ExceptionDialogFactory exceptionDialogFactory,
				LoadDataProgressDialogFactory progressDialogFactory,
				Class<? extends Activity> onFailureGoTo) {
			super(context, selectedReservation, listView, facade,
					allocatableCategoryElementKey, exceptionDialogFactory,
					progressDialogFactory, onFailureGoTo);
		}

		/**
		 * Custom execute method for testing purposes only. This method calls
		 * <code>onPostExecute</code> as soon as <code>doInBackground</code> has
		 * been completed.
		 * 
		 * @return This instance of TestableLoadAllocatablesAsyncTask
		 * @throws Exception
		 */
		public TestableLoadAllocatablesAsyncTask execute() throws Exception {
			// Execute the actual execute method
			super.execute(new Void[0]);

			// Wait for result
			Allocatable[] result = this.get();

			// Execute onPostExecute
			this.onPostExecute(result);

			return this;
		}

	}

}
