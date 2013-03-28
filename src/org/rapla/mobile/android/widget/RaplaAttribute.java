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

import java.security.InvalidParameterException;
import java.util.Locale;

import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import org.rapla.entities.dynamictype.Classification;

import android.R;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Base class for all rapla attributes of dynamic types.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 * @param <V>
 *            Attribute value type
 */
@SuppressWarnings("unchecked")
public abstract class RaplaAttribute<V> implements RaplaReservationAttribute,
		DynamicListItem {

	private Context context;
	private Attribute attribute;
	protected V value;
	protected ReservationImpl boundReservation;

	/**
	 * @param context
	 *            The current context
	 * @param attribute
	 *            Attribute of dynamic type
	 * @param type
	 *            Attribute type for validation
	 */
	public RaplaAttribute(Context context, Attribute attribute,
			AttributeType type) {
		this.assertAttributeType(attribute, type);

		this.context = context;
		this.attribute = attribute;
		this.value = (V) this.attribute.defaultValue();
	}

	/**
	 * Assert that the supplied attribute and the attribute type match
	 * 
	 * @param attribute
	 *            Attribute to be checked
	 * @param type
	 *            Attribute type
	 */
	protected void assertAttributeType(Attribute attribute, AttributeType type) {
		if (!attribute.getType().equals(type)) {
			throw new InvalidParameterException();
		}
	}

	/**
	 * Pull value from reservation and set widget value
	 * 
	 * @param reservation
	 *            Reservation to retrieve values from
	 */
	public void pullValueFromReservation(ReservationImpl reservation) {
		Classification classification = reservation.getClassification();
		this.value = (V) classification.getValue(this.attribute);
		this.setValueToWidget(this.value);
	}

	/**
	 * Get value from widget and put it to reservation
	 * 
	 * @param reservation
	 *            Reservation to assign value to
	 * @return Reference of input reservation
	 */
	public ReservationImpl putValueToReservation(ReservationImpl reservation) {
		Classification classification = reservation.getClassification();
		V value = this.getValueFromWidget();
		classification.setValue(this.attribute, value);
		return reservation;
	}

	/**
	 * Set widget value depending on widget
	 * 
	 * @param value
	 *            Value of the type described in the attribute metadata
	 */
	abstract protected void setValueToWidget(V value);

	/**
	 * @return Current context
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * @return The attribute
	 */
	public Attribute getAttribute() {
		return this.attribute;
	}

	/**
	 * Default logic for creating a label text view
	 * 
	 * @return Text view with attribute name as text
	 */
	protected TextView getLabel() {
		// Compose label layout parameters
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		// Compose label
		TextView label = new TextView(this.getContext());
		label.setId(R.id.text1);
		label.setText(this.attribute.getName(Locale.getDefault()));
		label.setLayoutParams(params);
		return label;
	}

	/**
	 * Called by the list view adapter and hence must return composed view for
	 * each respective list item.
	 */
	abstract public View getListItemView();

	/**
	 * Read value from widget and transform it to type described in the
	 * attribute metadata
	 * 
	 * @return Value from widget
	 */
	abstract protected V getValueFromWidget();
	
	public void bindReservation(ReservationImpl reservation) {
		this.boundReservation = reservation;
	}
	
	public boolean isBoundToReservation() {
		return this.boundReservation != null;
	}
}
