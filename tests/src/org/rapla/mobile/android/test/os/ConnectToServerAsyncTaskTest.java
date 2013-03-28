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

import org.rapla.mobile.android.RaplaMobileApplication;
import org.rapla.mobile.android.os.ConnectToServerAsyncTask;
import org.rapla.mobile.android.test.mock.MockExceptionDialogFactory;
import org.rapla.mobile.android.test.mock.MockLoadDataProgressDialogFactory;
import org.rapla.mobile.android.test.mock.MockRaplaConnection;
import org.rapla.mobile.android.test.mock.MockRaplaMobileApplication;
import org.rapla.mobile.android.utility.RaplaConnection;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.test.AndroidTestCase;

/**
 * ConnectToServerAsyncTaskTest
 * 
 * Unit test class for
 * <code>org.rapla.mobile.android.os.ConnectToServerAsyncTask</code>
 * 
 * @see org.rapla.mobile.android.os.ConnectToServerAsyncTask
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class ConnectToServerAsyncTaskTest extends AndroidTestCase {

	protected TestableConnectToServerAsyncTask task;
	protected Context context;
	protected MockRaplaMobileApplication application;
	protected MockRaplaConnection conn;
	protected MockExceptionDialogFactory exceptionDialogFactory;
	protected MockLoadDataProgressDialogFactory progressDialogFactory;

	protected void setUp() throws Exception {
		super.setUp();

		this.context = this.getContext(); // MockContext won't work
		this.application = new MockRaplaMobileApplication();
		this.conn = new MockRaplaConnection();
		this.exceptionDialogFactory = MockExceptionDialogFactory.getInstance();
		this.progressDialogFactory = MockLoadDataProgressDialogFactory
				.getInstance();

		this.task = new TestableConnectToServerAsyncTask(this.context,
				this.application, this.conn, this.exceptionDialogFactory, null,
				this.progressDialogFactory, null);
		// TODO check method
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

	public void testGetShouldReturnMockRaplaConnection() throws Exception {
		this.task.execute();
		RaplaConnection conn = this.task.get();
		assertEquals(this.conn, conn);
	}

	public void failingtestGetShouldReturnNullIfLoginFails() throws Exception {
		this.conn.loginReturn = false;
		this.task.execute();
		RaplaConnection conn = this.task.get();
		assertNull(conn);
	}

	public void failingtestGetShouldReturnNullIfLoginThrowsException()
			throws Exception {
		this.conn.loginThrowsException = true;
		this.task.execute();
		RaplaConnection conn = this.task.get();
		assertNull(conn);
	}

	public void failingtestExecuteShouldShowExceptionDialogIfLoginFails()
			throws Exception {
		this.exceptionDialogFactory.lastInstance = null;
		this.conn.loginReturn = false;
		this.task.execute();
		assertNotNull(this.exceptionDialogFactory.lastInstance);
	}

	public void failingtestExecuteShouldShowExceptionDialogIfLoginThrowsException()
			throws Exception {
		this.exceptionDialogFactory.lastInstance = null;
		this.conn.loginThrowsException = true;
		this.task.execute();
		assertNotNull(this.exceptionDialogFactory.lastInstance);
	}

	/**
	 * As <code>onPostExecute</code> is executed in a different thread, this
	 * class provides a new <code>execute</code> method for executing the task
	 * in a way so that <code>onPostExecute</code> is called as well.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private class TestableConnectToServerAsyncTask extends
			ConnectToServerAsyncTask {

		public TestableConnectToServerAsyncTask(Context context,
				RaplaMobileApplication application, RaplaConnection conn,
				ExceptionDialogFactory exceptionDialogFactory,
				Dialog waitdialog,
				LoadDataProgressDialogFactory progressDialogFactory,
				Activity onSuccessFinish) {
			super(context, application, conn, exceptionDialogFactory,
					waitdialog, progressDialogFactory, onSuccessFinish);
		}

		/**
		 * Custom execute method for testing purposes only. This method calls
		 * <code>onPostExecute</code> as soon as <code>doInBackground</code> has
		 * been completed.
		 * 
		 * @return This instance of TestableConnectToServerAsyncTask
		 * @throws Exception
		 */
		public TestableConnectToServerAsyncTask execute() throws Exception {
			// Execute the actual execute method
			super.execute(new Void[0]);

			// Wait for result
			RaplaConnection result = this.get();

			// Execute onPostExecute
			this.onPostExecute(result);

			return this;
		}

	}

}
