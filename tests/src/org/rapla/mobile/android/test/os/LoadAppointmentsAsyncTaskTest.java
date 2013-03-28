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

import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.entities.domain.internal.AppointmentImpl;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.mobile.android.os.LoadAppointmentsAsyncTask;
import org.rapla.mobile.android.test.mock.MockAppointmentFormatter;
import org.rapla.mobile.android.test.mock.MockExceptionDialogFactory;
import org.rapla.mobile.android.test.mock.MockListView;
import org.rapla.mobile.android.test.mock.MockLoadDataProgressDialogFactory;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;
import org.rapla.mobile.android.utility.factory.LoadDataProgressDialogFactory;
import org.rapla.mobile.android.widget.adapter.AppointmentAdapter;

import android.app.Activity;
import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.ListView;

/**
 * LoadAppointmentsAsyncTaskTest
 * 
 * Unit test class for
 * <code>org.rapla.mobile.android.os.LoadAppointmentsAsyncTask</code>
 * 
 * @see org.rapla.mobile.android.os.LoadAppointmentsAsyncTask
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class LoadAppointmentsAsyncTaskTest extends AndroidTestCase {

	protected TestableLoadAppointmentsAsyncTask task;
	protected Context context;
	protected MockListView listView;
	protected MockExceptionDialogFactory exceptionDialogFactory;
	protected MockLoadDataProgressDialogFactory progressDialogFactory;
	protected MockAppointmentFormatter formatter;
	protected ReservationImpl reservation;

	protected void setUp() throws Exception {
		super.setUp();

		this.context = this.getContext(); // MockContext won't work
		this.listView = new MockListView(this.context);
		this.exceptionDialogFactory = MockExceptionDialogFactory.getInstance();
		this.progressDialogFactory = MockLoadDataProgressDialogFactory
				.getInstance();
		this.formatter = new MockAppointmentFormatter();
		this.reservation = new ReservationImpl(null, null);
		this.reservation.addAppointment(new AppointmentImpl(null, null));
		this.reservation.addAppointment(new AppointmentImpl(null, null));

		this.task = new TestableLoadAppointmentsAsyncTask(this.context,
				this.formatter, this.listView, this.reservation,
				this.exceptionDialogFactory, this.progressDialogFactory, null);
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

	public void testGetShouldReturnAppointmentsFromReservationAsResultOfTask()
			throws Exception {
		this.task.execute();
		Appointment[] appointments = this.task.get();
		assertEquals(this.reservation.getAppointments().length,
				appointments.length);
	}

	public void testExecuteShouldAssignAppointmentsFromReservationToListView()
			throws Exception {
		this.task.execute();
		AppointmentAdapter adapter = (AppointmentAdapter) this.listView
				.getAdapter();
		assertNotNull(adapter);
		assertEquals(this.reservation.getAppointments().length, adapter.getCount());
	}

	/**
	 * TestableLoadAppointmentsAsyncTask
	 * 
	 * As <code>onPostExecute</code> is executed in a different thread, this
	 * class provides a new <code>execute</code> method for executing the task
	 * in a way so that <code>onPostExecute</code> is called as well.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private class TestableLoadAppointmentsAsyncTask extends
			LoadAppointmentsAsyncTask {

		public TestableLoadAppointmentsAsyncTask(Context context,
				AppointmentFormater formatter, ListView listView,
				ReservationImpl reservation,
				ExceptionDialogFactory exceptionDialogFactory,
				LoadDataProgressDialogFactory progressDialogFactory,
				Class<? extends Activity> onFailureGoTo) {
			super(context, formatter, listView, reservation,
					exceptionDialogFactory, progressDialogFactory,
					onFailureGoTo);
		}

		/**
		 * Custom execute method for testing purposes only. This method calls
		 * <code>onPostExecute</code> as soon as <code>doInBackground</code> has
		 * been completed.
		 * 
		 * @return This instance of TestableLoadAppointmentsAsyncTask
		 * @throws Exception
		 */
		public TestableLoadAppointmentsAsyncTask execute() throws Exception {
			// Execute the actual execute method
			super.execute(new Void[0]);

			// Wait for result
			Appointment[] result = this.get();

			// Execute onPostExecute
			this.onPostExecute(result);

			return this;
		}

	}

}
