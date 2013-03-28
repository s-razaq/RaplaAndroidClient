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
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaContextException;
import org.rapla.framework.RaplaLocale;

/**
 * MockRaplaContext
 * 
 * Mocked rapla context
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockRaplaContext implements RaplaContext {

	public ClientFacade clientFacade = new MockClientFacade();
	public boolean lookupThrowsException = false;

	public Object lookup(String key) throws RaplaContextException {
		if (this.lookupThrowsException) {
			throw new RaplaContextException("test", "thrown with purpose");
		} else {
			if (key.equals(ClientFacade.ROLE)) {
				return this.clientFacade;
			} else if (key.equals(RaplaLocale.ROLE)) {
				return new MockRaplaLocale();
			}
			return null;
		}
	}

	public boolean has(String key) {
		return false;
	}

	public void setClientFacade(ClientFacade facade) {
		this.clientFacade = facade;
	}

}
