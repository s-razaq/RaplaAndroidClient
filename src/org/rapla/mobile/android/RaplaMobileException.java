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

package org.rapla.mobile.android;

import android.util.AndroidException;

/**
 * Base exception class for all exception in the rapla mobile context.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class RaplaMobileException extends AndroidException {

	private static final long serialVersionUID = 1759459440790317099L;
	private Exception previous;

	public RaplaMobileException() {
		super();
	}

	public RaplaMobileException(String name) {
		super(name);
	}

	public RaplaMobileException(String name, Exception cause) {
		super(name);
		this.previous = cause;
	}

	public RaplaMobileException(Exception cause) {
		super(cause);
	}

	public Exception getPrevious() {
		return this.previous;
	}

}
