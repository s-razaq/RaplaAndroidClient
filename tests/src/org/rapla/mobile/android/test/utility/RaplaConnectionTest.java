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
import org.rapla.framework.RaplaContext;
import org.rapla.mobile.android.RaplaMobileLoginException;
import org.rapla.mobile.android.test.mock.MockClientFacade;
import org.rapla.mobile.android.test.mock.MockClientFacadeFactory;
import org.rapla.mobile.android.test.mock.MockRaplaContextFactory;
import org.rapla.mobile.android.utility.RaplaConnection;
import org.rapla.mobile.android.utility.factory.ClientFacadeFactory;
import org.rapla.mobile.android.utility.factory.RaplaContextFactory;

import android.test.AndroidTestCase;

/**
 * RaplaConnectionTest
 * 
 * Unit test class for org.rapla.mobile.android.utilities.RaplaConnection
 * 
 * @see org.rapla.mobile.android.utilities.RaplaConnection
 * @author Maximilian Lenkeit <dev@lenki.com>
 */	
public class RaplaConnectionTest extends AndroidTestCase {

	private MockRaplaContextFactory contextFactory;
	private MockClientFacadeFactory facadeFactory;
	private RaplaConnection conn;
	
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "$ecret";
	private static final String HOST = "my.host.com";
	private static final int HOST_PORT = 5432;
	
	protected void setUp() throws Exception {
		super.setUp();

		// Inject factories
		contextFactory = new MockRaplaContextFactory();
		RaplaContextFactoryInjector.setSingleton(contextFactory);
		facadeFactory = new MockClientFacadeFactory();
		ClientFacadeFactoryInjector.setSingleton(facadeFactory);
		
		// Initiate instance
		conn = new RaplaConnection(USERNAME, PASSWORD, HOST, HOST_PORT);
	}
	
	public void testGetContextShouldReturnRaplaContextFromFactory() {
		RaplaContext context = conn.getContext();
		assertEquals(this.contextFactory.context, context);
	}
	
	public void testGetFacadeShouldReturnClientFacadeFromFactory() {
		ClientFacade facade = conn.getFacade();
		assertEquals(this.facadeFactory.facade, facade);
	}
	
	public void testLoginShouldAuthenticateUserThroughFacade() throws RaplaMobileLoginException {
		assertTrue(this.conn.login());
		assertEquals(USERNAME, ((MockClientFacade)this.facadeFactory.facade).username);
		assertEquals(PASSWORD, ((MockClientFacade)this.facadeFactory.facade).password);
	}

	/**
	 * RaplaContextFactoryInjector
	 * 
	 * This class is to be used to inject the singleton
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private static class RaplaContextFactoryInjector extends
			RaplaContextFactory {

		private RaplaContextFactoryInjector() {
			super();
		}

		public static void setSingleton(RaplaContextFactory factory) {
			RaplaContextFactory.instance = factory;
		}
	}

	/**
	 * ClientFacadeFactoryInjector
	 * 
	 * This class is to be used to inject the singleton
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private static class ClientFacadeFactoryInjector extends
			ClientFacadeFactory {

		private ClientFacadeFactoryInjector() {
			super();
		}

		public static void setSingleton(ClientFacadeFactory factory) {
			ClientFacadeFactory.instance = factory;
		}
	}
}
