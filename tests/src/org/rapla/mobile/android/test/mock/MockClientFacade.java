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

package org.rapla.mobile.android.test.mock;

/**
 * MockClientFacade
 * 
 * Mocked client facade
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.rapla.entities.Category;
import org.rapla.entities.Entity;
import org.rapla.entities.User;
import org.rapla.entities.configuration.CalendarModelConfiguration;
import org.rapla.entities.configuration.Preferences;
import org.rapla.entities.configuration.RaplaMap;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Period;
import org.rapla.entities.domain.RepeatingType;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.domain.internal.AllocatableImpl;
import org.rapla.entities.domain.internal.AppointmentImpl;
import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.internal.DynamicTypeImpl;
import org.rapla.facade.AllocationChangeListener;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.Conflict;
import org.rapla.facade.ModificationListener;
import org.rapla.facade.PeriodModel;
import org.rapla.facade.UpdateErrorListener;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.mobile.android.test.test.FixtureHelper;
import org.rapla.storage.StorageOperator;

public class MockClientFacade implements ClientFacade {

	public String username;
	public String password;
	public boolean loginReturn = true;
	public boolean getAllocatablesThrowsException = false;
	public boolean getDynamicTypesThrowsException = false;
	public boolean getDynamicTypeThrowsException = false;
	public Preferences preferences;
	public static final int NUM_ALLOCATABLES = 2;
	public static final int NUM_DYNAMIC_TYPES = 2;
	public RaplaContext context;
	public MockCachableOperator operator;

	public boolean login(String username, char[] password)
			throws RaplaException {
		this.username = username;
		this.password = new String(password);
		return loginReturn;
	}

	public void logout() throws RaplaException {
	}

	public boolean isSessionActive() {
		return false;
	}

	public User getUser() throws RaplaException {
		return null;
	}

	public void switchTo(User user) {

	}

	public boolean canSwitchBack() {
		return false;
	}

	public void changePassword(User user, char[] oldPassword, char[] newPassword)
			throws RaplaException {

	}

	public boolean canChangePassword() {
		return false;
	}

	public void checkReservation(Reservation reservation) throws RaplaException {

	}

	@SuppressWarnings("rawtypes")
	public RaplaMap newRaplaMap(Map map) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public RaplaMap newRaplaMap(Collection col) {
		return null;
	}

	public CalendarModelConfiguration newRaplaCalendarModel(RaplaMap selected,
			ClassificationFilter[] allocatableFilter,
			ClassificationFilter[] eventFilter, String title, Date startDate,
			Date endDate, Date selectedDate, String view, RaplaMap optionMap) {
		return null;
	}

	public Reservation newReservation() throws RaplaException {
		return null;
	}

	public Appointment newAppointment(Date startDate, Date endDate)
			throws RaplaException {
		AppointmentImpl a = FixtureHelper.createAppointment(startDate, endDate);
		a.setReadOnly(false);
		return a;
	}

	public Appointment newAppointment(Date startDate, Date endDate,
			RepeatingType repeatingType, int repeatingDuration)
			throws RaplaException {
		return null;
	}

	public Allocatable newResource() throws RaplaException {
		return null;
	}

	public Allocatable newPerson() throws RaplaException {
		return null;
	}

	public Period newPeriod() throws RaplaException {
		return null;
	}

	public Category newCategory() throws RaplaException {
		return null;
	}

	public Attribute newAttribute(AttributeType attributeType)
			throws RaplaException {
		return null;
	}

	public DynamicType newDynamicType(String classificationType)
			throws RaplaException {
		return null;
	}

	public User newUser() throws RaplaException {
		return null;
	}

	public Entity clone(Entity obj) throws RaplaException {
		return null;
	}

	public Entity edit(Entity obj) throws RaplaException {
		if(obj instanceof Reservation) {
			return (ReservationImpl)obj;
		}
		return null;
	}

	public Entity getPersistant(Entity working) throws RaplaException {
		return null;
	}

	public void storeObjects(Entity[] obj) throws RaplaException {

	}

	public void store(Entity obj) throws RaplaException {

	}

	public void removeObjects(Entity[] obj) throws RaplaException {

	}

	public void remove(Entity obj) throws RaplaException {

	}

	public void storeAndRemove(Entity[] storedObjects, Entity[] removedObjects)
			throws RaplaException {

	}

	public DynamicType[] getDynamicTypes(String classificationType)
			throws RaplaException {
		if (this.getDynamicTypesThrowsException) {
			throw new RaplaException("Mocked exception");
		}
		DynamicType[] dynamicTypes = new DynamicType[NUM_DYNAMIC_TYPES];
		for (int i = 0; i < NUM_DYNAMIC_TYPES; i++) {
			DynamicTypeImpl dt = new DynamicTypeImpl();
			dt.setReadOnly(true);
			dynamicTypes[i] = dt;
		}
		return dynamicTypes;
	}

	public DynamicType getDynamicType(String elementKey) throws RaplaException {
		if (this.getDynamicTypeThrowsException) {
			throw new RaplaException("Mocked exception");
		}
		return FixtureHelper.createDynamicTypeAllocatable();
	}

	public Category getSuperCategory() {
		return null;
	}

	public Category getUserGroupsCategory() throws RaplaException {
		return null;
	}

	public User[] getUsers() throws RaplaException {
		return null;
	}

	public User getUser(String username) throws RaplaException {
		return null;
	}

	public Allocatable[] getAllocatables(ClassificationFilter[] filters)
			throws RaplaException {
		if (this.getAllocatablesThrowsException) {
			throw new RaplaException("Mocked exception");
		}
		Allocatable[] allocatables = new Allocatable[NUM_ALLOCATABLES];
		for (int i = 0; i < NUM_ALLOCATABLES; i++) {
			AllocatableImpl a = new AllocatableImpl(null, null);
			// Classification c = a.getClassification();
			// c.setValue("name", "alloc " + i);
			// a.setClassification(c);
			// a.setReadOnly(true);
			allocatables[i] = a;
		}
		return allocatables;
	}

	public Allocatable[] getAllocatables() throws RaplaException {
		if (this.getAllocatablesThrowsException) {
			throw new RaplaException("Mocked exception");
		}
		return null;
	}

	public Reservation[] getReservations(User user, Date start, Date end,
			ClassificationFilter[] filters) throws RaplaException {
		return new Reservation[0];
	}

	public Reservation[] getReservations(Allocatable[] allocatables,
			Date start, Date end) throws RaplaException {
		return null;
	}

	public Reservation[] getReservationsForAllocatable(
			Allocatable[] allocatables, Date start, Date end,
			ClassificationFilter[] filters) throws RaplaException {
		return null;
	}

	public Period[] getPeriods() throws RaplaException {
		return null;
	}

	public PeriodModel getPeriodModel() throws RaplaException {
		return null;
	}

	public Date today() {
		return null;
	}

	public Allocatable[] getAllocatableBindings(Appointment appointment)
			throws RaplaException {
		return null;
	}

	public Conflict[] getConflicts(Reservation reservation)
			throws RaplaException {
		return null;
	}

	public Conflict[] getConflicts(Date startDate) throws RaplaException {
		return null;
	}

	public boolean hasPermissionToAllocate(Appointment appointment,
			Allocatable allocatable) {
		return false;
	}

	public Preferences getPreferences(User user) throws RaplaException {
		return this.getPreferences();
	}

	public Preferences getPreferences() throws RaplaException {
		if(this.preferences == null) {
			this.preferences = new MockRaplaPreferences();
		}
		return this.preferences;
	}

	public boolean canExchangeAllocatables(Reservation reservation) {
		return false;
	}

	public boolean canReadReservationsFromOthers(User user) {
		return false;
	}

	public void refresh() throws RaplaException {

	}

	public boolean isClientForServer() {
		return false;
	}

	public void addModificationListener(ModificationListener listener) {

	}

	public void removeModificationListener(ModificationListener listener) {

	}

	public void addUpdateErrorListener(UpdateErrorListener listener) {

	}

	public void removeUpdateErrorListener(UpdateErrorListener listener) {

	}

	public void addAllocationChangedListener(
			AllocationChangeListener triggerListener) {

	}

	public void removeAllocationChangedListener(
			AllocationChangeListener triggerListener) {

	}

	public StorageOperator getOperator() {
		if(this.operator == null) {
			try {
				this.operator = new MockCachableOperator(this.context);
			} catch (RaplaException e) {
			}
		}
		return this.operator;
	}

}
