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

package org.rapla.mobile.android.test.test;

import java.util.Date;

import org.rapla.entities.Category;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.domain.internal.AllocatableImpl;
import org.rapla.entities.domain.internal.AppointmentImpl;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.ConstraintIds;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.entities.dynamictype.internal.AttributeImpl;
import org.rapla.entities.dynamictype.internal.DynamicTypeImpl;
import org.rapla.entities.internal.CategoryImpl;
import org.rapla.entities.storage.internal.SimpleIdentifier;

/**
 * This class provide utility functions for creating reservations, appointments,
 * allocatables and so forth to be used in unit tests.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class FixtureHelper {

	/**
	 * Create valid allocatable with name attribue
	 * 
	 * @return Valid <code>AllocatableImpl</code> instance
	 * @throws RuntimeException
	 */
	public static AllocatableImpl createAllocatable() throws RuntimeException {
		try {
			AllocatableImpl a = new AllocatableImpl(null, null);
			DynamicTypeImpl dt = new DynamicTypeImpl();
			dt.setAnnotation(DynamicTypeAnnotations.KEY_CLASSIFICATION_TYPE,
					DynamicTypeAnnotations.VALUE_RESOURCE_CLASSIFICATION);
			dt.addAttribute(createAttributeString());
			dt.setAnnotation(DynamicTypeAnnotations.KEY_NAME_FORMAT, "{name}");
			AttributeImpl attr = new AttributeImpl(AttributeType.STRING);
			attr.setId(new SimpleIdentifier(Attribute.TYPE, rand()));
			attr.setKey("name");
			dt.addAttribute(attr);
			dt.setReadOnly(true);
			Classification c = dt.newClassification();
			a.setClassification(c);
			a.setId(new SimpleIdentifier(Allocatable.TYPE, rand()));
			return a;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a valid reservation
	 * 
	 * @return Valid <code>ReservationImpl</code> instance
	 * @throws RuntimeException
	 */
	public static ReservationImpl createReservation() throws RuntimeException {
		try {
			ReservationImpl reservation = new ReservationImpl(null, null);
			reservation.setId(new SimpleIdentifier(Reservation.TYPE, rand()));
			reservation.setClassification(createDynamicTypeReservation().newClassification());
			return reservation;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a rapla string attribute
	 * 
	 * @return Valid <code>AttributeImpl</code> instance of type string
	 * @throws RuntimeException
	 */
	public static AttributeImpl createAttributeString() throws RuntimeException {
		try {
			AttributeImpl attribute = new AttributeImpl(AttributeType.STRING);
			attribute.setKey("name");
			attribute.setDefaultValue("Example string");
			attribute.setId(new SimpleIdentifier(DynamicType.TYPE, rand()));
			return attribute;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a rapla integer attribute
	 * 
	 * integer * @throws RuntimeException
	 */
	public static AttributeImpl createAttributeInteger()
			throws RuntimeException {
		try {
			AttributeImpl attribute = new AttributeImpl(AttributeType.INT);
			attribute.setDefaultValue(new Long(2));
			return attribute;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a rapla string boolean
	 * 
	 * @return Valid <code>AttributeImpl</code> instance of type boolean
	 * @throws RuntimeException
	 */
	public static AttributeImpl createAttributeBoolean()
			throws RuntimeException {
		try {
			AttributeImpl attribute = new AttributeImpl(AttributeType.BOOLEAN);
			attribute.setDefaultValue(new Boolean(true));
			return attribute;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a rapla date attribute
	 * 
	 * @return Valid <code>AttributeImpl</code> instance of type date
	 * @throws RuntimeException
	 */
	public static AttributeImpl createAttributeDate() throws RuntimeException {
		try {
			AttributeImpl attribute = new AttributeImpl(AttributeType.DATE);
			attribute.setDefaultValue(new Date());
			return attribute;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a rapla category attribute
	 * 
	 * @return Valid <code>AttributeImpl</code> instance of type category
	 * @throws RuntimeException
	 */
	public static AttributeImpl createAttributeCategory()
			throws RuntimeException {
		try {
			AttributeImpl attribute = new AttributeImpl(AttributeType.CATEGORY);
			CategoryImpl root = createCategoryTreeWithThreeLevel();
			attribute.setConstraint(ConstraintIds.KEY_ROOT_CATEGORY, root);
			return attribute;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static int rand() {
		return (int) (Math.random() * 9999);
	}

	/**
	 * Create a rapla category
	 * 
	 * @return Valid <code>CategoryImpl</code> instance
	 * @throws RuntimeException
	 */
	public static CategoryImpl createCategory() throws RuntimeException {
		try {
			CategoryImpl category = new CategoryImpl();
			category.setId(new SimpleIdentifier(Category.TYPE, rand()));
			return category;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static CategoryImpl createCategoryTreeWithThreeLevel()
			throws RuntimeException {
		try {
			CategoryImpl root = createCategory();
			CategoryImpl levelOneA = createCategory();
			CategoryImpl levelOneB = createCategory();
			CategoryImpl levelTwoA = createCategory();
			CategoryImpl levelTwoB = createCategory();
			CategoryImpl levelTwoC = createCategory();
			CategoryImpl levelTwoD = createCategory();

			// Add to root
			root.addCategory(levelOneA);
			root.addCategory(levelOneB);

			// Add to level one A
			levelOneA.addCategory(levelTwoA);
			levelOneA.addCategory(levelTwoB);

			// Add to level one B
			levelOneB.addCategory(levelTwoC);
			levelOneB.addCategory(levelTwoD);

			return root;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static DynamicTypeImpl createDynamicTypeAllocatable()
			throws RuntimeException {
		try {
			DynamicTypeImpl dt = new DynamicTypeImpl();
			dt.setElementKey("tempResource");
			dt.setAnnotation(DynamicTypeAnnotations.KEY_CLASSIFICATION_TYPE,
					DynamicTypeAnnotations.VALUE_RESOURCE_CLASSIFICATION);
			dt.setReadOnly(true);
			return dt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static DynamicTypeImpl createDynamicTypeReservation()
			throws RuntimeException {
		try {
			DynamicTypeImpl dt = new DynamicTypeImpl();
			dt.setElementKey("tempReservation");
			dt.setAnnotation(DynamicTypeAnnotations.KEY_CLASSIFICATION_TYPE,
					DynamicTypeAnnotations.VALUE_RESERVATION_CLASSIFICATION);
			dt.setReadOnly(true);
			return dt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static AppointmentImpl createAppointment(Date startDate, Date endDate) {
		try {
			AppointmentImpl appointment = new AppointmentImpl(startDate, endDate);
			appointment.setReadOnly(true);
			return appointment;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
