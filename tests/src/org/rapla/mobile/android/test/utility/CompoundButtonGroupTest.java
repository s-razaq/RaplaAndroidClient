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

import org.rapla.mobile.android.utility.CompoundButtonGroup;

import android.content.Context;
import android.test.AndroidTestCase;
import android.widget.CompoundButton;

/**
 * Unit test class for org.rapla.mobile.android.utility.CompoundButtonGroup
 * 
 * @see org.rapla.mobile.android.utility.factory.utility.CompoundButtonGroup
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class CompoundButtonGroupTest extends AndroidTestCase {

	protected CompoundButtonGroup buttonGroup;
	protected MockCompoundButton buttonOne;
	protected MockCompoundButton buttonTwo;

	protected void setUp() throws Exception {
		this.buttonGroup = new CompoundButtonGroup();
		this.buttonOne = new MockCompoundButton(this.getContext());
		this.buttonTwo = new MockCompoundButton(this.getContext());
	}

	public void testAddCompoundButtonShouldRegisterOnClickListener() {
		// Reset button
		this.buttonOne.listener = null;

		// Add button
		this.buttonGroup.addCompoundButton(this.buttonOne);

		// Check button
		assertNotNull(this.buttonOne.listener);
	}

	public void testAddCompountButtonWithCheckedFlagShouldCheckButton() {
		// Reset button
		this.buttonOne.checked = false;

		// Add button with flag
		this.buttonGroup.addCompoundButton(this.buttonOne, true);

		// Check button
		assertTrue(this.buttonOne.checked);
	}

	public void testGetCheckedButtonShouldReturnButtonPreviouslyAddedWithCheckedFlag() {
		// Reset button
		this.buttonOne.checked = false;

		// Add button with flag
		this.buttonGroup.addCompoundButton(this.buttonOne, true);

		// Get button
		CompoundButton button = this.buttonGroup.getCheckedButton();

		// Compare buttons
		assertSame(this.buttonOne, button);
	}

	public void testGetAttachmentOfCheckedButtonShouldReturnAttachmentOfButtonPreviouslyAddedWitCheckedFlagAndAttachment() {
		// Reset button
		this.buttonOne.checked = false;
		Object attachment = new Object();

		// Add button with flag and attachment
		this.buttonGroup.addCompoundButton(this.buttonOne, true, attachment);

		// Get attachment
		Object receivedAttachment = this.buttonGroup
				.getAttachmentOfCheckedButton();

		// Compare attachment
		assertSame(attachment, receivedAttachment);
	}
	
	public void testGetCheckedButtonShouldReturnNullIfNoButtonIsChecked() {
		// Reset button
		this.buttonOne.checked = false;
		
		// Add button
		this.buttonGroup.addCompoundButton(this.buttonOne);
		
		assertNull(this.buttonGroup.getCheckedButton());
	}
	
	public void testGetAttachmentOfCheckedButtonShouldReturnNullIfNoButtonIsChecked() {
		// Reset button
		this.buttonOne.checked = false;
		
		// Add button
		this.buttonGroup.addCompoundButton(this.buttonOne);
		
		assertNull(this.buttonGroup.getAttachmentOfCheckedButton());
	}
	
	public void testButtonOneShouldBeUncheckedIfButtonTwoGetsChecked() {
		// Prepare button
		this.buttonOne.checked = false;
		this.buttonOne.enableListener = true;
		this.buttonTwo.checked = false;
		this.buttonTwo.enableListener = true;
		
		// Add buttons
		this.buttonGroup.addCompoundButton(this.buttonOne, true);
		this.buttonGroup.addCompoundButton(this.buttonTwo, false);
		
		// Check button two
		this.buttonTwo.setChecked(true);
		
		// Check buttons
		assertTrue(this.buttonTwo.checked);
		assertFalse(this.buttonOne.checked);
	}
	
	public void testGetCheckedButtonShouldReturnNullIfPreviouslyCheckedButtonIsUnchecked() {
		// Prepare button
		this.buttonOne.checked = false;
		this.buttonOne.enableListener = true;
		this.buttonTwo.checked = false;
		this.buttonTwo.enableListener = true;
		
		// Add buttons
		this.buttonGroup.addCompoundButton(this.buttonOne, true);
		this.buttonGroup.addCompoundButton(this.buttonTwo, false);
		
		// Check checked button
		assertNotNull(this.buttonGroup.getCheckedButton());
		
		// Uncheck button one
		this.buttonOne.setChecked(false);
		
		// There's no checked button now
		assertNull(this.buttonGroup.getCheckedButton());
		
		
		
	}

	/**
	 * Mocked compount button to be used for tests
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	private static class MockCompoundButton extends CompoundButton {

		public CompoundButton.OnCheckedChangeListener listener;
		public boolean checked = false;
		public boolean enableListener = false;

		public MockCompoundButton(Context context) {
			super(context);
		}

		public void setOnCheckedChangeListener(
				CompoundButton.OnCheckedChangeListener l) {
			this.listener = l;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
			if(listener != null && this.enableListener) {
				this.listener.onCheckedChanged(this, this.checked);
			}
		}

	}

}
