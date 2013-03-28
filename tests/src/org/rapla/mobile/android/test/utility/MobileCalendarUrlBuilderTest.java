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

import org.rapla.mobile.android.utility.MobileCalendarUrlBuilder;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * EncrypterTest
 * 
 * Unit test class for
 * <code>org.rapla.mobile.android.utility.MobileCalendarUrlBuilder</code>.
 * 
 * @see org.rapla.mobile.android.utility.MobileCalendarUrlBuilder
 * @author Maximilian Lenkeit <dev@lenki.com>
 */

public class MobileCalendarUrlBuilderTest extends AndroidTestCase {

	protected MobileCalendarUrlBuilder builder;
	public static final String USERNAME = "username";
	public static final String HOST = "10.0.0.1";
	public static final int PORT = 8082;
	public static final String FILE = "my-file";

	protected void setUp() throws Exception {
		super.setUp();
		this.builder = new MobileCalendarUrlBuilder(HOST, PORT, USERNAME, FILE);
	}

	public void testBuildStringShouldReturnValidUrlToMobileView() {
		String expected = this.getExpectedString();

		assertEquals(expected, this.builder.buildString());
	}
	
	public void testBuildUriShouldReturnValidUriToMobileView() {
		Uri expected = Uri.parse(this.getExpectedString());
		
		assertEquals(expected, this.builder.buildUri());
	}

	private String getExpectedString() {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(HOST);
		sb.append(":");
		sb.append(PORT);
		sb.append("/rapla?page=mobile&user=");
		sb.append(USERNAME);
		sb.append("&file=");
		sb.append(FILE);
		sb.append("&android-client=1");
		return new String(sb);
	}
}
