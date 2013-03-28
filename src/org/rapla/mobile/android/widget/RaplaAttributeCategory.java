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

import org.rapla.entities.Category;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import org.rapla.entities.dynamictype.ConstraintIds;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This class wraps a rapla dynamic type attribute of type category and provides
 * the necessary interface for being a list item.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaAttributeCategory extends RaplaAttribute<Category> {

	/**
	 * Associated widget
	 */
	private RaplaCategorySpinner widget;

	/**
	 * The root category of the attribute (top-most category that can be
	 * selected)
	 */
	private Category root;

	/**
	 * @param context The current context
	 * @param attribute The rapla attribute
	 */
	public RaplaAttributeCategory(Context context, Attribute attribute) {
		super(context, attribute, AttributeType.CATEGORY);

		// Set root category
		this.root = (Category) this.getAttribute().getConstraint(
				ConstraintIds.KEY_ROOT_CATEGORY);
	}

	@Override
	protected void setValueToWidget(Category value) {
		if (this.widget != null) {
			this.widget.refresh(value);
		}
	}

	@Override
	public View getListItemView() {
		// Compose relative layout for list item
		RelativeLayout layout = new RelativeLayout(this.getContext());

		// Add label
		TextView label = this.getLabel();
		layout.addView(label, 0);

		this.widget = new RaplaCategorySpinner(this.getContext(), this.root,
				this.value);
		this.setValueToWidget(this.value);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, label.getId());
		layout.addView(this.widget, 1, params);

		return layout;
	}

	@Override
	protected Category getValueFromWidget() {
		if (this.widget == null) {
			return (Category) this.getAttribute().defaultValue();
		} else {
			return this.widget.getSelectedCategory();
		}
	}
}
