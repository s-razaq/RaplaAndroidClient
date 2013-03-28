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

package org.rapla.mobile.android.test.utility;

import org.rapla.facade.ClientFacade;

import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.test.mock.MockClientFacade;
import org.rapla.mobile.android.test.mock.MockRaplaContext;
import org.rapla.mobile.android.utility.factory.ClientFacadeFactory;

import android.test.AndroidTestCase;

/**
 * ClientFacadeFactoryTest
 * 
 * Unit test class for org.rapla.mobile.android.utilities.factory.ClientFacadeFactory
 * 
 * @see org.rapla.mobile.android.utility.factory.utilities.factory.ClientFacadeFactory
 * @author Maximilian Lenkeit <dev@lenki.com>
 */	
public class ClientFacadeFactoryTest extends AndroidTestCase {

	public void testGetInstanceShouldAlwaysReturnTheSameInstance() {
		ClientFacadeFactory first = ClientFacadeFactory.getInstance();
		ClientFacadeFactory second = ClientFacadeFactory.getInstance();
		assertEquals(first, second);
	}
	
	public void testCreateInstanceShouldReturnFacadeFromRaplaContext() throws RaplaMobileException {
		// Prepare mock objects
		MockRaplaContext context = new MockRaplaContext();
		MockClientFacade facade = new MockClientFacade();
		context.setClientFacade(facade);
		
		ClientFacade clientFacade = ClientFacadeFactory.getInstance().createInstance(context);
		assertEquals(facade, clientFacade);
	}
}
