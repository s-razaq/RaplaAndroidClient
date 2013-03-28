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

package org.rapla.mobile.android.test.mock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

/**
 * MockContext
 * 
 * Custom mocked context class to inject other mocked objects.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockContext extends android.test.mock.MockContext {

	private MockSharedPreferences preferences = null;
	public boolean startedActivity = false;
	private Context nativeMock;
	
	public MockContext() {
		
	}
	
	public MockContext(Context nativeMock) {
		this.nativeMock = nativeMock;
	}
	
	public Resources getResources() {
		return this.nativeMock.getResources();
	}
	
	public Object getSystemService(String name) {
		return this.nativeMock.getSystemService(name);
	}

	public void setSharedPreferences(MockSharedPreferences p) {
		this.preferences = p;
	}

	public SharedPreferences getSharedPreferences(String name, int mode) {
		return this.preferences;
	}

	public String getPackageName() {
		return "org.rapla.mobile.android";
	}
	
	public void startActivity(Intent intent) {
		this.startedActivity = true;
	}
}