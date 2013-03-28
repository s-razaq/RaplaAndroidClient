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

import org.rapla.entities.RaplaType;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.storage.LocalCache;
import org.rapla.storage.impl.AbstractCachableOperator;

/**
 * MockCachableOperator
 * 
 * Mocked cachable operator
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class MockCachableOperator extends AbstractCachableOperator {
	
	public MockLocalCache localCache = null;

	public MockCachableOperator(RaplaContext context) throws RaplaException {
		super(context);
	}

	public void saveData() throws RaplaException {
		
	}

	public void connect() throws RaplaException {
		

	}
	
	public LocalCache getCache() {
		if(this.localCache == null) {
			this.localCache = new MockLocalCache(null);
		}
		return this.localCache;
	}

	public void connect(String username, char[] password) throws RaplaException {
		

	}

	public boolean isConnected() {
		
		return false;
	}

	public void refresh() throws RaplaException {
		

	}

	public void disconnect() throws RaplaException {
		

	}

	public Object createIdentifier(RaplaType raplaType) throws RaplaException {
		
		return null;
	}

	public boolean supportsActiveMonitoring() {
		
		return false;
	}

}
