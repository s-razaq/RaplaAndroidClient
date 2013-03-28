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
import org.rapla.mobile.android.utility.factory.ClientFacadeFactory;

/**
 * MockClientFacadeFactory
 * 
 * Mocked client facade factory class to create MockClientFacade instances.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockClientFacadeFactory extends ClientFacadeFactory {

	public ClientFacade facade = new MockClientFacade();
	
	public ClientFacade createInstance(RaplaContext context) {
		return this.facade;
	}
}
