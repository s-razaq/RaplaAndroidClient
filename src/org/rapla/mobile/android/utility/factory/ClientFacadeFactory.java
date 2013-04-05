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

package org.rapla.mobile.android.utility.factory;

import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;
import org.rapla.mobile.android.RaplaMobileException;

/**
 * Client Facade Factory (Singleton)
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class ClientFacadeFactory {

	protected static ClientFacadeFactory instance;

	protected ClientFacadeFactory() {

	}

	/**
	 * Get facade factory instance (singleton)
	 * 
	 * @return Singleton
	 */
	public static ClientFacadeFactory getInstance() {
		if (instance == null) {
			instance = new ClientFacadeFactory();
		}
		return instance;
	}

	/**
	 * Create ClientFacade instance
	 */
	public ClientFacade createInstance(RaplaContext context)
			throws RaplaMobileException {

		try {
			ClientFacade facade = context.lookup(ClientFacade.class);
			return facade;
		} catch (Exception ex) {
			throw new RaplaMobileException("Initializing client facade failed",
					ex);
		}
	}
}
