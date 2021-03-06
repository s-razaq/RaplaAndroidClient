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

import java.util.Locale;

import org.rapla.entities.storage.RefEntity;
import org.rapla.storage.LocalCache;

/**
 * MockLocalCache
 * 
 * Mocked local cache
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class MockLocalCache extends LocalCache {

	public RefEntity<?> getReturn;

	public MockLocalCache(Locale locale) {
		super(locale);
	}

	public RefEntity<?> get(Object id) {
		return this.getReturn;
	}

}
