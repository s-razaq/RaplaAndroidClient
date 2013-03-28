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

package org.rapla.mobile.android.test.app;

import org.rapla.mobile.android.R;
import org.rapla.mobile.android.app.ExceptionDialog;
import org.rapla.mobile.android.test.mock.MockActivity;
import org.rapla.mobile.android.test.mock.MockContext;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * ExceptionDialogTest
 * 
 * Unit test class for org.rapla.modile.android.app.ExceptionDialog
 * 
 * @see org.rapla.mobile.android.app.ExceptionDialog
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class ExceptionDialogTest extends AndroidTestCase {

	protected Context context;

	protected void setUp() throws Exception {
		super.setUp();

		this.context = this.getContext();
	}

	public void testConstructorWithContextAndString() {
		ExceptionDialog d = new ExceptionDialog(this.context, "Message");
		assertNotNull(d);
	}

	public void testConstructorWithContextAndResourceAndActivityClass() {
		ExceptionDialog d = new ExceptionDialog(this.context, R.string.app_name,
				MockActivity.class);
		assertNotNull(d);
	}

	public void testConstructorWithContextAndThrowableAndActivityClass() {
		ExceptionDialog d = new ExceptionDialog(this.context, new Exception(),
				MockActivity.class);
		assertNotNull(d);
	}

	public void testConstructorWithContextAndResource() {
		ExceptionDialog d = new ExceptionDialog(this.context, R.string.app_name);
		assertNotNull(d);
	}

	public void testConstructorWithContextAndThrowable() {
		ExceptionDialog d = new ExceptionDialog(this.context, new Exception());
		assertNotNull(d);
	}
	
	public void testCloseShouldStartActivityIfProvided() {
		MockContext context = new MockContext(this.getContext());
		context.startedActivity = false;
		
		ExceptionDialog d = new ExceptionDialog(context, "Message", MockActivity.class);
		d.close();
		assertTrue(context.startedActivity);
	}
	
	public void testCloseShouldNotStartActivityIfNotProvided() {
		MockContext context = new MockContext(this.getContext());
		context.startedActivity = false;
		
		ExceptionDialog d = new ExceptionDialog(context, "Message");
		d.close();
		assertFalse(context.startedActivity);
	}
}
