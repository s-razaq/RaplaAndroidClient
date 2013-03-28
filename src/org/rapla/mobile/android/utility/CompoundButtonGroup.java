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

import java.util.HashMap;

import android.widget.CompoundButton;
import android.widget.RadioButton;

/**
 * A compound button group is a set of compound buttons (radio button or check
 * box) of that only one button can be selected at a time. If the user selects
 * an unselected button, the previously selected button is automatically being
 * unchecked.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class CompoundButtonGroup implements RadioButton.OnCheckedChangeListener {

	/**
	 * Reference to the currently checked button
	 */
	private CompoundButton checkedButton = null;

	/**
	 * Hash map for compount button attachments
	 */
	private HashMap<CompoundButton, Object> attachments = new HashMap<CompoundButton, Object>();

	/**
	 * Add compound button to compound button group
	 * 
	 * @param cb
	 *            Compound button to be added
	 * @param checked
	 *            Whether the compound button should be checked or not
	 */
	public void addCompoundButton(CompoundButton cb, boolean checked) {
		this.addCompoundButton(cb, checked, null);
	}

	/**
	 * Add compound button to compound button group
	 * 
	 * @param cb
	 *            Compound button to be added
	 * @param checked
	 *            Whether the compound button should be checked or not
	 * @param attachment
	 *            Object to be attached to the compound button
	 */
	public void addCompoundButton(CompoundButton cb, boolean checked,
			Object attachment) {

		// Reset listener
		cb.setOnCheckedChangeListener(null);

		// Process check flag
		if (checked) {
			cb.setChecked(true);
			this.checkedButton = cb;
		} else {
			cb.setChecked(false);
		}

		// Register this class as on checked change listener so that the
		// previously checked button is being unchecked
		cb.setOnCheckedChangeListener(this);

		// Associate attachment with compoud button
		this.attachments.put(cb, attachment);
	}

	/**
	 * Add compound button to compound button group
	 * 
	 * @param cb
	 *            Compound button to be added
	 */
	public void addCompoundButton(CompoundButton cb) {
		this.addCompoundButton(cb, false, null);
	}

	/**
	 * Called when a compound button changes its state and unchecks the previous
	 * checked compound button
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			// If new state of button is checked, uncheck the previously chcked
			// button
			if (this.checkedButton != null) {
				// Uncheck the previously checked button
				this.checkedButton.setChecked(false);
			}
			// Set current button as new checked button
			this.checkedButton = buttonView;
		} else if (buttonView == this.checkedButton) {
			// If current button was unchecked and the button is the previously
			// checked button, no button of the group is checked so reset the
			// checkedButton reference
			this.checkedButton = null;
		}
	}

	/**
	 * @return Reference to the currently checked button
	 */
	public CompoundButton getCheckedButton() {
		return this.checkedButton;
	}

	/**
	 * @return The attachment associated with the checked button
	 */
	public Object getAttachmentOfCheckedButton() {
		if (this.getCheckedButton() == null) {
			return null;
		} else {
			return this.attachments.get(this.getCheckedButton());
		}
	}
}