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

package org.rapla.mobile.android.widget;

import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This class wraps a rapla dynamic type attribute of type boolean and provides
 * the necessary interface for being a list item.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaAttributeBoolean extends RaplaAttribute<Boolean> {

	private CheckBox widget;

	/**
	 * @param context
	 *            Current context
	 * @param attribute
	 *            Rapla attribute
	 */
	public RaplaAttributeBoolean(Context context, Attribute attribute) {
		super(context, attribute, AttributeType.BOOLEAN);
	}

	/**
	 * @see org.rapla.mobile.android.widget.RaplaAttribute#getListItemView()
	 */
	@Override
	public View getListItemView() {
		// Compose relative layout for list item
		RelativeLayout layout = new RelativeLayout(this.getContext());

		// Add label
		TextView label = this.getLabel();
		layout.addView(label, 0);

		// Compose attribute specific widget and add to view
		this.widget = new CheckBox(this.getContext());
		this.setValueToWidget(this.value);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout.addView(this.widget, 1, params);

		return layout;
	}

	/**
	 * @see org.rapla.mobile.android.widget.RaplaAttribute#setValueToWidget()
	 */
	@Override
	protected void setValueToWidget(Boolean value) {
		if (this.widget != null) {
			this.widget.setChecked(value.equals(Boolean.TRUE));
		}
	}

	/**
	 * @see org.rapla.mobile.android.widget.RaplaAttribute#getValueFromWidget()
	 */
	@Override
	protected Boolean getValueFromWidget() {
		if (this.widget == null) {
			return this.value;
		} else {
			return this.widget.isChecked() ? Boolean.TRUE : Boolean.FALSE;
		}
	}
}
