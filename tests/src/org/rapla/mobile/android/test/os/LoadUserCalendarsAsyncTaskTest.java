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

import org.rapla.facade.ClientFacade;
import org.rapla.mobile.android.os.LoadUserCalendarsAsyncTask;
import org.rapla.mobile.android.test.mock.MockClientFacade;
import org.rapla.mobile.android.test.mock.MockExceptionDialogFactory;
import org.rapla.mobile.android.test.mock.MockListView;
import org.rapla.mobile.android.test.mock.MockLoadDataProgressDialogFactory;
import org.rapla.mobile.android.test.mock.MockRaplaPreferences;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.UserCalendarAdapter;

import android.app.Activity;
import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.ListView;

/**
 * LoadUserCalendarsAsyncTaskTest
 * 
 * Unit test class for
 * <code>org.rapla.mobile.android.os.LoadUserCalendarsAsyncTask</code>
 * 
 * @see org.rapla.mobile.android.os.LoadUserCalendarsAsyncTask
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class LoadUserCalendarsAsyncTaskTest extends AndroidTestCase {

	protected TestableLoadUserCalendarsAsyncTask task;
	protected Context context;
	protected MockListView listView;
	protected MockClientFacade facade;
	protected MockExceptionDialogFactory exceptionDialogFactory;
	protected MockLoadDataProgressDialogFactory progressDialogFactory;

	protected void setUp() throws Exception {
		super.setUp();

		this.context = this.getContext(); // MockContext won't work
		this.listView = new MockListView(this.context);
		this.facade = new MockClientFacade();
		this.exceptionDialogFactory = MockExceptionDialogFactory.getInstance();
		this.progressDialogFactory = MockLoadDataProgressDialogFactory
				.getInstance();

		this.task = new TestableLoadUserCalendarsAsyncTask(this.context,
				this.listView, this.facade, "default", this.exceptionDialogFactory,
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

	public void testGetShouldReturnCalendarsFromRaplaUserPreferences()
			throws Exception {
		this.task.execute();
		String[] calendars = this.task.get();
		// +1 as the default calendar should have been added
		assertEquals(
				((MockRaplaPreferences) this.facade.preferences).userCalendars
						.size() + 1,
				calendars.length);
	}

	public void testExecuteShouldAssignCalendarsFromRaplaUserPreferencesToListView()
			throws Exception {
		// Reset exception dialog factory
		this.exceptionDialogFactory.lastInstance = null;

		// Run task
		this.task.execute();

		// Assert that no exception dialog was shown
		assertNull(this.exceptionDialogFactory.lastInstance);

		// Check list adapter
		UserCalendarAdapter adapter = (UserCalendarAdapter) this.listView
				.getAdapter();
		assertNotNull(adapter);
		// +1 as the default calendar should have been added
		assertEquals(
				((MockRaplaPreferences) this.facade.preferences).userCalendars
						.size() + 1,
				adapter.getCount());
	}
	
	public void testExecuteShouldShowExceptionDialogIfAutoExportPluginNotFound() throws Exception {
		// Mock exception
		((MockRaplaPreferences)this.facade.getPreferences()).getEntryThrowsException = true;

		// Reset exception dialog factory
		this.exceptionDialogFactory.lastInstance = null;
		
		// Run task
		this.task.execute();
		
		// Check that exception dialog was shown
		assertNotNull(this.exceptionDialogFactory.lastInstance);
		assertTrue(this.exceptionDialogFactory.lastInstance.shown);
	}

	/**
	 * TestableLoadUserCalendarsAsyncTask
	 * 
	 * As <code>onPostExecute</code> is executed in a different thread, this
	 * class provides a new <code>execute</code> method for executing the task
	 * in a way so that <code>onPostExecute</code> is called as well.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private class TestableLoadUserCalendarsAsyncTask extends
			LoadUserCalendarsAsyncTask {


		public TestableLoadUserCalendarsAsyncTask(Context context,
				ListView listView, ClientFacade facade,
				String defaultCalendarName,
				ExceptionDialogFactory exceptionDialogFactory,
				LoadDataProgressDialogFactory progressDialogFactory,
				Class<? extends Activity> onFailureGoTo) {
			super(context, listView, facade, defaultCalendarName, exceptionDialogFactory,
					progressDialogFactory, onFailureGoTo);
		}

		/**
		 * Custom execute method for testing purposes only. This method calls
		 * <code>onPostExecute</code> as soon as <code>doInBackground</code> has
		 * been completed.
		 * 
		 * @return This instance of TestableLoadUserCalendarsAsyncTask
		 * @throws Exception
		 */
		public TestableLoadUserCalendarsAsyncTask execute() throws Exception {
			// Execute the actual execute method
			super.execute(new Void[0]);

			// Wait for result
			String[] result = this.get();

			// Execute onPostExecute
			this.onPostExecute(result);

			return this;
		}

	}

}
