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

package org.rapla.mobile.android.test.utility.factory;

import org.rapla.mobile.android.R;
import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.app.ExceptionDialog;
import org.rapla.mobile.android.test.mock.MockActivity;
import org.rapla.mobile.android.utility.factory.ExceptionDialogFactory;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * ExceptionDialogFactoryTest
 * 
 * Unit test class for
 * org.rapla.mobile.android.utilities.factory.ExceptionDialogFactory
 * 
 * @see org.rapla.mobile.android.utility.factory.utilities.factory.ClientFacadeFactory
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class ExceptionDialogFactoryTest extends AndroidTestCase {

	protected Context context;

	protected void setUp() throws Exception {
		super.setUp();

		this.context = this.getContext();
	}

	public void testGetInstanceShouldAlwaysReturnTheSameInstance() {
		ExceptionDialogFactory first = ExceptionDialogFactory.getInstance();
		ExceptionDialogFactory second = ExceptionDialogFactory.getInstance();
		assertEquals(first, second);
	}

	public void testCreateShouldAlwaysReturnNewInstance()
			throws RaplaMobileException {
		Context context = this.getContext();
		String message = "test";
		ExceptionDialog first = ExceptionDialogFactory.getInstance().create(
				context, message);
		ExceptionDialog second = ExceptionDialogFactory.getInstance().create(
				context, message);
		assertNotSame(first, second);
	}

	public void testCreateWithContextAndResource() {
		ExceptionDialog d = ExceptionDialogFactory.getInstance().create(
				this.context, R.string.app_name);
		assertNotNull(d);
	}

	public void testCreateWithContextAndThrowable() {
		ExceptionDialog d = ExceptionDialogFactory.getInstance().create(
				this.context, new Exception());
		assertNotNull(d);
	}

	public void testCreateWithContextAndResourceAndActivity() {
		ExceptionDialog d = ExceptionDialogFactory.getInstance().create(
				this.context, R.string.app_name, MockActivity.class);
		assertNotNull(d);
	}

	public void testCreateWithContextAndStringAndActivity() {
		ExceptionDialog d = ExceptionDialogFactory.getInstance().create(
				this.context, "message", MockActivity.class);
		assertNotNull(d);
	}

	public void testCreateWithContextAndThrowableAndActivity() {
		ExceptionDialog d = ExceptionDialogFactory.getInstance().create(
				this.context, new Exception(), MockActivity.class);
		assertNotNull(d);
	}
}
