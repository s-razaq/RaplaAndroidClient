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

package org.rapla.mobile.android.app;

import java.util.Locale;

import org.rapla.entities.Category;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.utility.CompoundButtonGroup;
import org.rapla.mobile.android.widget.adapter.RaplaCategoryAdapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The dialog allows for selecting a category (either the root category or one
 * of its child categories) by displaying a list view with check boxes. Clicking
 * a category drills down into its sub-categories whereas a click on the icon in
 * the dialog title bar navigates one level up.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaCategoryChoiceDialog extends Dialog {

	private Category root;
	private Category selected;
	private Category parent;
	private Context context;
	private RaplaCategoryAdapter adapter;
	private CompoundButtonGroup buttonGroup;
	private TextView title;
	private ImageView upIcon;
	private Button buttonSave;
	private Button buttonCancel;

	public RaplaCategoryChoiceDialog(Context context, Category root,
			Category selected) {
		super(context, R.style.CustomDialogTheme);

		// Set content view for dialog (list view)
		this.setContentView(R.layout.rapla_category_drill_down_list);

		// Get ui widget references
		this.title = (TextView) this.findViewById(R.id.title_text);
		this.upIcon = (ImageView) this.findViewById(R.id.title_icon);
		this.upIcon.setOnClickListener(new IconAscendOnClickListener(this));
		this.buttonSave = (Button) this.findViewById(android.R.id.button1);
		this.buttonCancel = (Button) this.findViewById(android.R.id.button3);

		// Prepare null variables
		if (selected == null) {
			selected = root;
		}

		// Set instance variables
		this.context = context;
		this.root = root;
		this.selected = selected;
		this.parent = this.selected.getParent();

		// Initiate the dialog with the parent of the selected category
		this.reload(this.parent);
	}

	/**
	 * @return Reference to the list view element
	 */
	protected ListView getListView() {
		return (ListView) this.findViewById(android.R.id.list);
	}

	/**
	 * Set adapter for list view
	 * 
	 * @param adapter
	 *            Adapter for list view
	 */
	protected void setAdapter(ListAdapter adapter) {
		this.getListView().setAdapter(adapter);
	}

	/**
	 * @return Name of parent category of selected category
	 */
	public String createTitle() {
		// Check whether parent category is super category (has no parent)
		if (this.parent.getParent() == null) {
			return this.getContext().getString(R.string.root_category);
		} else {
			return this.parent.getName(Locale.getDefault());
		}
	}

	/**
	 * Set title in custom title bar
	 * 
	 * @param title
	 *            Title
	 */
	public void setTitle(String title) {
		this.title.setText(title);
	}

	/**
	 * @return True if the current level is below the parent of the root
	 *         category, false otherwise.
	 */
	public boolean canAscend() {
		// parent should become a child and be selectable
		Category topMostParent = this.root.getParent();
		Category tagetParent = this.parent.getParent();
		return !topMostParent.isIdentical(tagetParent);
	}

	/**
	 * @param category
	 *            Category to navigate to
	 * @return True if the given category has child categories.
	 */
	public boolean canDescendOn(Category category) {
		return category.getCategories().length > 0;
	}

	/**
	 * Go up one level if possible and reload dialog
	 */
	public void ascend() {
		// Check whether level up navigation is possible
		if (!this.canAscend()) {
			return;
		}

		// Store currently selected value in selected
		this.pullSelected();

		this.reload(this.parent.getParent());
	}

	/**
	 * Drill down on the given category if possible
	 * 
	 * @param category
	 *            Category to navigate to
	 */
	public void descendOn(Category category) {
		// Check whether level down navigation is possible
		if (!this.canDescendOn(category)) {
			return;
		}

		// Store currently selected value in selected
		this.pullSelected();

		this.reload(category);
	}

	/**
	 * Pull selected category from button group. Remark: This is a dirty hack
	 * and should be solved differently.
	 */
	protected void pullSelected() {
		CompoundButton button = this.buttonGroup.getCheckedButton();
		// Continue only if button reference is not null. The button reference
		// is null, if no check box has been selected on the currently shown
		// list.
		if (button != null) {
			this.selected = (Category) this.buttonGroup
					.getAttachmentOfCheckedButton();
		} else {
			this.selected = null;
		}
	}

	/**
	 * Reload the dialog with the given category as parent (title). This will
	 * display all associated sub categories
	 * 
	 * @param newParent
	 *            Category to become parent
	 */
	protected void reload(Category newParent) {
		// Set parent
		this.parent = newParent;

		// Set title
		this.setTitle(this.createTitle());

		// Initiate compound button group so that only one button of the
		// displayed list item can be checked at a time. Radio buttons would be
		// much nicer and more natural but for some reason, the custom compound
		// button group class didn't work for them.
		this.buttonGroup = new CompoundButtonGroup();

		// Load child categories and instantiate adapter (redundance for better
		// readability
		Category[] categories;
		if (this.parent.getParent() == null
				&& !this.root.isIdentical(this.parent)) {
			// If parent is super category (no parent) and the top most category
			// allowed is not the parent (super) category, display only the root
			// category
			categories = new Category[] { this.root };
		} else if (this.parent.getParent() == null
				&& this.root.isIdentical(this.parent)) {
			// If parent is super category (no parent) and the top most category
			// allowed is the parent (super) category, display all sub
			// categories of parent
			categories = this.parent.getCategories();
		} else {
			categories = this.parent.getCategories();
		}

		// Create adapter
		this.adapter = new RaplaCategoryAdapter(this.context,
				R.layout.rapla_category_drill_down_list_item, categories,
				this.selected, this.buttonGroup,
				RaplaCategoryOnClickListenerFactory.getInstance(), this);

		// Assign adapter to list view
		this.setAdapter(this.adapter);

		// this.buttonGroup.getButtonForAttachment(this.selected).setChecked(true);

		// Register on click listener on category name for drilling into the
		// selected category
		this.getListView().setFocusable(false);
		this.getListView().setClickable(true);
		this.getListView().setSelector(R.drawable.window_background);
	}

	/**
	 * Set on click listener for the apply button
	 * 
	 * @param l
	 *            Listener called on apply
	 */
	public void setApplyButtonOnClickListener(View.OnClickListener l) {
		this.buttonSave.setOnClickListener(l);
	}

	/**
	 * Set on click listener for the cancel button
	 * 
	 * @param l
	 *            Listener called on cancel
	 */
	public void setCancelButtonOnClickListener(View.OnClickListener l) {
		this.buttonCancel.setOnClickListener(l);
	}

	/**
	 * Get selected category from list view
	 * 
	 * @return Selected category
	 */
	public Category getSelected() {
		this.pullSelected();
		return this.selected;
	}

	/**
	 * This listener class listens to clicks on rapla category names and allows
	 * for drilling down on the clicked category.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class RaplaCategoryOnClickListener implements
			View.OnClickListener {

		/**
		 * Reference to the calling dialog
		 */
		private RaplaCategoryChoiceDialog dialog;

		/**
		 * Corresponding list item index
		 */
		private int index;

		/**
		 * @param dialog
		 *            Reference to calling dialog
		 */
		public RaplaCategoryOnClickListener(RaplaCategoryChoiceDialog dialog,
				int index) {
			this.dialog = dialog;
			this.index = index;
		}

		public void onClick(View v) {
			// Get selected category object
			Category selected = this.dialog.adapter.getItem(this.index);

			// Drill down on selected category
			this.dialog.descendOn(selected);
		}

	}

	/**
	 * Factory class for <code>RaplaCategoryOnClickListener</code> to ease
	 * injection into the list view logic.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class RaplaCategoryOnClickListenerFactory {

		/**
		 * Singleton reference
		 */
		private static RaplaCategoryOnClickListenerFactory instance;

		protected RaplaCategoryOnClickListenerFactory() {

		}

		/**
		 * @return Singleton of factory
		 */
		public static RaplaCategoryOnClickListenerFactory getInstance() {
			if (instance == null) {
				instance = new RaplaCategoryOnClickListenerFactory();
			}
			return instance;
		}

		/**
		 * Create new instance of listener
		 * 
		 * @param dialog
		 *            Reference to the currently displayed dialog
		 * @param index
		 *            List item index
		 * @return New listener instance
		 */
		public RaplaCategoryOnClickListener create(
				RaplaCategoryChoiceDialog dialog, int index) {
			return new RaplaCategoryOnClickListener(dialog, index);
		}
	}

	/**
	 * This listener class listens to clicks on the icon in the title bar and
	 * thereby allows for navigating to the upper category.
	 * 
	 * @author Maximilian Lenkeit <dev@lenki.com>
	 * 
	 */
	public static class IconAscendOnClickListener implements
			View.OnClickListener {

		/**
		 * Reference to the calling dialog
		 */
		private RaplaCategoryChoiceDialog dialog;

		/**
		 * @param dialog
		 *            Reference to calling dialog
		 */
		public IconAscendOnClickListener(RaplaCategoryChoiceDialog dialog) {
			this.dialog = dialog;
		}

		public void onClick(View v) {
			this.dialog.ascend();
		}

	}

}