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

import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import org.rapla.mobile.android.widget.RaplaAttribute;
import org.rapla.mobile.android.widget.RaplaAttributeBoolean;
import org.rapla.mobile.android.widget.RaplaAttributeCategory;
import org.rapla.mobile.android.widget.RaplaAttributeDate;
import org.rapla.mobile.android.widget.RaplaAttributeInt;
import org.rapla.mobile.android.widget.RaplaAttributeString;

import android.content.Context;

/**
 * Factory for dynamically creating rapla attribute widgets based on dynamic
 * type attributes
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaAttributeWidgetFactory {

	protected static RaplaAttributeWidgetFactory instance;

	protected RaplaAttributeWidgetFactory() {

	}

	/**
	 * @return Singleton instance
	 */
	public static RaplaAttributeWidgetFactory getInstance() {
		if (instance == null) {
			instance = new RaplaAttributeWidgetFactory();
		}
		return instance;
	}

	/**
	 * @param context
	 *            The current context
	 * @param attribute
	 *            Attribute to be wrapped
	 * @return View wrapper for attribute
	 */
	public RaplaAttribute<?> create(Context context, Attribute attribute) {
		AttributeType type = attribute.getType();
		if (type.equals(AttributeType.BOOLEAN)) {
			return new RaplaAttributeBoolean(context, attribute);
		} else if (type.equals(AttributeType.CATEGORY)) {
			return new RaplaAttributeCategory(context, attribute);
		} else if (type.equals(AttributeType.DATE)) {
			return new RaplaAttributeDate(context, attribute);
		} else if (type.equals(AttributeType.INT)) {
			return new RaplaAttributeInt(context, attribute);
		} else if (type.equals(AttributeType.STRING)) {
			return new RaplaAttributeString(context, attribute);
		} else {
			return null;
		}
	}
}
