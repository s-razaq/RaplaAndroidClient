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

import java.util.Locale;

import org.rapla.entities.Category;
import org.rapla.mobile.android.app.RaplaCategoryChoiceDialog;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * The rapla category spinner is a drop-down widget. Clicking the widget opens a
 * a <code>RaplaCategoryChoiceDialog</code> that allows for drilling into the
 * category structure and selecting a category.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaCategorySpinner extends Spinner {

	/**
	 * The top level category that can be selected
	 */
	private Category root;

	/**
	 * Currently selected category
	 */
	private Category selected;

	/**
	 * @param context
	 *            The current context
	 * @param root
	 *            The top level category that can be selected
	 * @param selected
	 *            The currently selected category
	 */
	public RaplaCategorySpinner(Context context, Category root,
			Category selected) {
		super(context);

		// Check whether the supplied combination of root and category is valid
		if (!root.isAncestorOf(selected) && root.isIdentical(selected)) {
			throw new RuntimeException("Invalid parent-child combination");
		}

		// Select instance variables
		this.root = root;
		this.selected = selected;

		// Register on touch listener for displaying the dialog
		this.setOnTouchListener(new SpinnerOnTouchListener(this.getContext(),
				this, this.root, this.selected));

		// Initialize the widget with the currently selected category
		this.refresh();
	}

	/**
	 * The selected category will automatically be set to the root category
	 * 
	 * @param context
	 *            The current context
	 * @param root
	 *            The top level category that can be selected
	 */
	public RaplaCategorySpinner(Context context, Category root) {
		this(context, root, root);
	}

	/**
	 * @return The currently selected and displayed category
	 */
	public Category getSelectedCategory() {
		return this.selected;
	}

	/**
	 * Set selected category
	 * 
	 * @param selected
	 *            Category to be selected
	 */
	public void setSelectedCategory(Category selected) {
		this.selected = selected;
	}

	/**
	 * Refresh the widget with the selected category
	 */
	public void refresh() {
		this.refresh(this.selected);
	}

	/**
	 * Refresh the widget with the given category
	 * 
	 * @param category
	 *            Category to be displayed on the widget
	 */
	public void refresh(Category category) {
		this.selected = category;
		String label;
		if (this.selected != null) {
			label = this.selected.getName(Locale.getDefault());
		} else {
			label = this.getContext().getString(
					org.rapla.mobile.android.R.string.no_selection);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getContext(), android.R.layout.simple_spinner_item,
				new String[] { label });
		this.setAdapter(adapter);
	}

	/**
	 * The listener class shows a <code>RaplaCategoryChoiceDialog</code> and
	 * thereby allows for selecting a category from the tree.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class SpinnerOnTouchListener implements OnTouchListener {

		private Context context;
		private Category root;
		private Category selected;
		private RaplaCategorySpinner spinner;
		private RaplaCategoryChoiceDialog dialog;

		/**
		 * @param context
		 *            The current context
		 * @param root
		 *            The top level category that can be selected
		 * @param selected
		 *            The currently selected category
		 */
		public SpinnerOnTouchListener(Context context,
				RaplaCategorySpinner spinner, Category root, Category selected) {
			this.context = context;
			this.spinner = spinner;
			this.root = root;
			this.selected = selected;
		}

		public boolean onTouch(View v, MotionEvent event) {
			// Check whether dialog has already been created and is still
			// showing, then do not create a new dialog
			if (this.dialog != null && this.dialog.isShowing()) {
				return true;
			}

			// Create Dialog instance and show dialog
			this.dialog = new RaplaCategoryChoiceDialog(this.context,
					this.root, this.selected);

			// Register listener for apply button
			this.dialog.setApplyButtonOnClickListener(new ApplyOnClickListener(
					this.spinner, this.dialog));
			this.dialog
					.setCancelButtonOnClickListener(new CancelOnClickListener(
							this.dialog));
			
			this.dialog.show();

			return true;
		}

	}

	/**
	 * Listener for the dialog's apply button. The handler reads the selected
	 * category from the list view, stores it in the spinner and refreshes the
	 * spinner.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class ApplyOnClickListener implements View.OnClickListener {

		private RaplaCategorySpinner spinner;
		private RaplaCategoryChoiceDialog dialog;

		/**
		 * @param spinner
		 *            Reference to the associated spinner
		 * @param dialog
		 *            Rerefence to the dialog
		 */
		public ApplyOnClickListener(RaplaCategorySpinner spinner,
				RaplaCategoryChoiceDialog dialog) {
			this.spinner = spinner;
			this.dialog = dialog;
		}

		public void onClick(View v) {
			this.spinner.setSelectedCategory(this.dialog.getSelected());
			this.spinner.refresh(this.dialog.getSelected());
			this.dialog.dismiss();

		}
	}

	/**
	 * Listener for the dialog's cancel button. The handler simply closed the
	 * dialog.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class CancelOnClickListener implements View.OnClickListener {
		private RaplaCategoryChoiceDialog dialog;

		/**
		 * @param dialog
		 *            Rerefence to the dialog
		 */
		public CancelOnClickListener(RaplaCategoryChoiceDialog dialog) {
			this.dialog = dialog;
		}

		public void onClick(View v) {
			this.dialog.dismiss();

		}
	}

}
