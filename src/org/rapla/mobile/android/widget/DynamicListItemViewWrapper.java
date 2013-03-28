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

import android.view.View;

/**
 * Wrapper class for a view instance so that it can be attached to a list of
 * dynamic list items.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class DynamicListItemViewWrapper implements DynamicListItem {

	private View v;

	/**
	 * @param v
	 *            View reference to be displayed in the list
	 */
	public DynamicListItemViewWrapper(View v) {
		this.v = v;
	}

	public View getListItemView() {
		return this.v;
	}

}
