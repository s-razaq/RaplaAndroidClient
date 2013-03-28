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

package org.rapla.mobile.android.test.widget;

import java.security.InvalidParameterException;

import org.rapla.mobile.android.test.test.FixtureHelper;
import org.rapla.mobile.android.widget.RaplaAttributeCategory;

import android.test.AndroidTestCase;
import android.view.View;

/**
 * Unit test class for org.rapla.mobile.android.widget.RaplaAttributeCategory
 * 
 * @see org.rapla.mobile.android.utility.factory.widget.RaplaAttributeCategory
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class RaplaAttributeCategoryTest extends AndroidTestCase {

	protected RaplaAttributeCategory attribute;

	protected void setUp() throws Exception {
		super.setUp();
		this.attribute = new RaplaAttributeCategory(this.getContext(),
				FixtureHelper.createAttributeCategory());
	}

	public void testConstructorShouldThrowInvalidParameterExceptionForAttributeOtherThanOfTypeCategory() {
		try {
			new RaplaAttributeCategory(this.getContext(),
					FixtureHelper.createAttributeInteger());
			fail(); // Expected exception
		} catch (InvalidParameterException e) {
			// expected
		} catch (Exception e) {
			fail();
		}
	}

	public void testGetListViewItemShouldNotReturnNull() {
		View v = this.attribute.getListItemView();
		assertNotNull(v);
	}
}
