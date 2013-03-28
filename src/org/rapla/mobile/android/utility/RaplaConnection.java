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

package org.rapla.mobile.android.utility;

import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.mobile.android.PreferencesHandler;
import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.RaplaMobileLoginException;
import org.rapla.mobile.android.utility.factory.ClientFacadeFactory;
import org.rapla.mobile.android.utility.factory.RaplaContextFactory;

/**
 * Manages the connection to a rapla server.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaConnection {

	public static final String IDENTIFIER = "RaplaConnection";
	private RaplaContext context;
	private ClientFacade facade;
	private String username;
	private String password;

	public RaplaConnection(String username, String password, String host,
			int hostPort, boolean isSecure) throws RaplaMobileException {

		this.context = RaplaContextFactory.getInstance().createInstance(host,
				hostPort, isSecure);
		this.facade = ClientFacadeFactory.getInstance().createInstance(
				this.context);
		this.username = username;
		this.password = password;
	}

	public RaplaConnection(PreferencesHandler preferences)
			throws RaplaMobileException {
		this(preferences.getUsername(), preferences.getPassword(), preferences
				.getHost(), preferences.getHostPort(), preferences.isSecure());
	}

	public boolean login() throws RaplaMobileLoginException {
		try {
			return this.facade
					.login(this.username, this.password.toCharArray());
		} catch (RaplaException ex) {
			throw new RaplaMobileLoginException(ex);
		}
	}

	public RaplaContext getContext() {
		return this.context;
	}

	public ClientFacade getFacade() {
		return this.facade;
	}
}
