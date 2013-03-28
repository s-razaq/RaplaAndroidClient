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

import org.rapla.facade.ClientFacade;
import org.rapla.mobile.android.PreferencesHandler;
import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.RaplaMobileLoginException;
import org.rapla.mobile.android.utility.RaplaConnection;

/**
 * MockRaplaConnection
 * 
 * Mocked rapla connection
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 *
 */
public class MockRaplaConnection extends RaplaConnection {

	private MockClientFacade facade;
	public boolean loginReturn = true;
	public boolean loginThrowsException = false;
	
	public MockRaplaConnection(PreferencesHandler preferences)
			throws RaplaMobileException {
		super(preferences);
	}
	
	public MockRaplaConnection() throws RaplaMobileException {
		super("", "", "", 0);
	}
	
	public ClientFacade getFacade() {
		if(this.facade == null) {
			this.facade = new MockClientFacade();
		}
		return this.facade;
	}
	
	public boolean login() throws RaplaMobileLoginException {
		if(this.loginThrowsException) {
			throw new RaplaMobileLoginException(new Exception());
		}
		return this.loginReturn;
	}

}
