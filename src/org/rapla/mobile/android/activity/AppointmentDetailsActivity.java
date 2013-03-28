/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Saqib Razaq                                           |
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

package org.rapla.mobile.android.activity;

import java.util.Calendar;
import java.util.Date;

import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Repeating;
import org.rapla.entities.domain.RepeatingType;
import org.rapla.entities.domain.internal.AppointmentImpl;
import org.rapla.framework.RaplaContextException;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.mobile.android.R;
import org.rapla.mobile.android.widget.adapter.MySpinnerAdapter;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AppointmentDetailsActivity extends BaseActivity {

	private RaplaLocale loc;
	private Spinner spinnerType, spinnerNumber, spinnerStartDate, spinnerTime,
			spinnerEndDate;
	private EditText repeatingInterval;
	private LinearLayout appLayout; // app = appointment

	// This defines three unique Dialog identifiers for our Activity class. The
	// values are arbitrary, but need to be unique within the Activity.
	private static final int DATE_END_DIALOG_ID = 0;
	private static final int DATE_START_DIALOG_ID = 1;
	private static final int TIME_START_DIALOG_ID = 2;
	private static final int TIME_END_DIALOG_ID = 3;
	
	public static final String INTENT_INT_APPOINTMENT_ID = "appointmentId";

	int repeatingType, indexOfSelectedItemInNumberSpinner;
	Date appointmentDate = new Date(), endRepeatingDate = new Date();
	Time startTime = new Time(), endTime = new Time();
	Repeating repeating;
	// Appointment a;
	String weekday;
	int repeatingFrequency;
	ArrayAdapter<CharSequence> repeatingTypeAdapter;
	AppointmentImpl appointment;

	public static final String daysOfWeek[] = { "Sun", "Mon", "Tue", "Wed",
			"Thu", "Fri", "Sat" };
	public static final String months[] = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	public static final String repeatingTypes[] = { "no repeating", "weekly",
			"daily", "monthly", "yearly" };
	public static Time ALLDAY = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set content and custom title
		this.setContentView(R.layout.appointment_details);
		this.setTitle(R.string.titlebar_title_appointment);

		// Lazy load appointment formatter
		if (this.loc == null) {
			try {
				this.loc = (RaplaLocale) this.getRaplaContext().lookup(
						RaplaLocale.ROLE);
			} catch (RaplaContextException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ALLDAY = new Time();
		ALLDAY.set(0, 0, 0, 0, 0, 0);

		int id = getIntent().getExtras().getInt(INTENT_INT_APPOINTMENT_ID);
		if (id == -1) {
			appointment = (AppointmentImpl) createAppointment();
		} else {
			Appointment[] app = this.getSelectedReservation().getAppointments();
			appointment = (AppointmentImpl) app[id];
		}

		getSummary(appointment);

		initView();
	}

	@Override
	public void onStart() {
		super.onStart();
		displayView();
	}

	public Appointment createAppointment() {
		Appointment emptyAppointment = null;
		try {
			emptyAppointment = getFacade().newAppointment(appointmentDate,
					endRepeatingDate);
			emptyAppointment.setRepeatingEnabled(false);
			this.getSelectedReservation().addAppointment(emptyAppointment);
		} catch (RaplaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emptyAppointment;

	}

	private void storeAppointmentData() {
		// TODO implement function to store changes that have been made to
		// appointment
		if (repeatingType == 0) {
			appointment.setRepeatingEnabled(false);
		} else {
			appointment.setRepeatingEnabled(true);
			appointment.getRepeating().setType(
					RepeatingType.findForString(repeatingTypes[repeatingType]));

			appointment.getRepeating().setEnd(endRepeatingDate);

			// Set Interval for how often a appointment occurs but only when
			// repeating is weekly
			if (appointment.getRepeating().isWeekly()) {
				repeatingFrequency = Integer.valueOf(repeatingInterval
						.getText().toString());
				appointment.getRepeating().setInterval(repeatingFrequency);
			}

		}

		// Set new date to appointment
		appointmentDate.setMinutes(startTime.minute);
		appointmentDate.setHours(startTime.hour);
		appointment.getStart().setTime(appointmentDate.getTime());

		// Appointment ends on the same day as it is started
		appointment.getEnd().setTime(appointmentDate.getTime());
		appointment.getEnd().setMinutes(endTime.minute);
		appointment.getEnd().setHours(endTime.hour);

		try {
			getFacade().store(appointment);
		} catch (RaplaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displayView() {
		spinnerType.setSelection(repeatingType);

		repeatingTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNumber.setAdapter(repeatingTypeAdapter);
		spinnerNumber.setSelection(indexOfSelectedItemInNumberSpinner, true);

		spinnerStartDate
				.setAdapter(new MySpinnerAdapter(appointmentDate, this));
		
		// When repeating has no end date
		if(endRepeatingDate == null){
			spinnerEndDate.setAdapter(new MySpinnerAdapter(getString(R.string.forever_repeating), this));
		}else{
			spinnerEndDate.setAdapter(new MySpinnerAdapter(endRepeatingDate, this));
		}
        
		spinnerTime.setAdapter(new MySpinnerAdapter(startTime, endTime, this));

		repeatingInterval.setText(String.valueOf(repeatingFrequency));
		
		repeating = appointment.getRepeating();
		if (repeating == null || repeating.isDaily()) {
			appLayout.setVisibility(View.GONE);
		} else if (repeating.isYearly()) {
			spinnerNumber.setEnabled(false);
			appLayout.setVisibility(View.VISIBLE);
			
			repeatingInterval.setEnabled(false);
			repeatingInterval.setInputType(InputType.TYPE_NULL);
			//repeatingInterval.setFocusable(false);
		} else if (repeating.isWeekly()) {
			appLayout.setVisibility(View.VISIBLE);
			spinnerNumber.setEnabled(false);
			repeatingInterval.setEnabled(true);
			repeatingInterval.setInputType(InputType.TYPE_CLASS_NUMBER);
			repeatingInterval.setFocusable(true);
		} else if (repeating.isMonthly()) {
			spinnerNumber.setEnabled(false);
			repeatingInterval.setEnabled(false);
			repeatingInterval.setInputType(InputType.TYPE_NULL);
			//repeatingInterval.setFocusable(false);
			appLayout.setVisibility(View.VISIBLE);
		}

	}

	private void initView() {
		// Get a Spinner and bind it to an ArrayAdapter that
		// references a String array.
		spinnerType = (Spinner) findViewById(R.id.spinnerType);
		spinnerType.setOnItemSelectedListener(new MyOnItemSelectedListener());

		spinnerNumber = (Spinner) findViewById(R.id.spinnerNumber);
		if (repeating != null && repeating.isYearly()) {
			repeatingTypeAdapter = ArrayAdapter.createFromResource(this,
					R.array.months, android.R.layout.simple_spinner_item);
		} else {
			repeatingTypeAdapter = ArrayAdapter.createFromResource(this,
					R.array.days, android.R.layout.simple_spinner_item);
		}

		spinnerStartDate = (Spinner) findViewById(R.id.spinnerStartDate);
		spinnerStartDate.setOnTouchListener(new MyOnTouchListener());

		spinnerEndDate = (Spinner) findViewById(R.id.spinnerEndDate);
		spinnerEndDate.setOnTouchListener(new MyOnTouchListener());

		spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
		spinnerTime.setAdapter(new MySpinnerAdapter(startTime, endTime, this));
		spinnerTime.setOnTouchListener(new MyOnTouchListener());

		repeatingInterval = (EditText) findViewById(R.id.editInterval);

		appLayout = (LinearLayout) findViewById(R.id.appointmentLayout);

	}

	public void getSummary(AppointmentImpl a) {
		repeating = a.getRepeating();
		final boolean wholeDaysSet = a.isWholeDaysSet();
		repeatingType = repeating != null ? getItemIndex(repeatingTypes,
				repeating.getType().toString()) : getItemIndex(repeatingTypes,
				"no repeating");
		if (repeating == null) {
			this.appointmentDate = a.getStart();
			this.endRepeatingDate = a.getEnd(); // getMaxEnd() and getEnd()
												// from Repeating return the
												// same value
			indexOfSelectedItemInNumberSpinner = 0;
			if (!wholeDaysSet) {
				startTime.set(a.getStart().getTime());
				endTime.set(a.getEnd().getTime());
			} else {
				startTime.set(ALLDAY);
				endTime.set(ALLDAY);
			}

		} else if (repeating.isWeekly() || repeating.isMonthly()
				|| repeating.isYearly()) {
			this.appointmentDate = a.getStart();
			this.endRepeatingDate = repeating.getEnd();
			if (repeating.isMonthly()) {
				repeatingFrequency = Integer.valueOf(getWeekdayOfMonth(
						a.getStart()).split("\\.")[0]);
				indexOfSelectedItemInNumberSpinner = getItemIndex(daysOfWeek,
						loc.getWeekday(a.getStart()));
			}
			if (repeating.isWeekly()) {
				repeatingFrequency = repeating.getInterval();
				indexOfSelectedItemInNumberSpinner = getItemIndex(daysOfWeek,
						loc.getWeekday(a.getStart()));
				if (repeatingFrequency > 4) {
					repeatingFrequency = 1;
				}
			}
			if (repeating.isYearly()) {
				repeatingFrequency = Integer
						.valueOf(getDayOfMonth(a.getStart()).split("\\.")[0]);
				indexOfSelectedItemInNumberSpinner = getItemIndex(months, loc
						.getMonth(a.getStart()).toString());
			}
			if (wholeDaysSet) {
				startTime.set(ALLDAY);
				endTime.set(ALLDAY);
			} else {
				startTime.set(a.getStart().getTime());
				endTime.set(a.getEnd().getTime());
			}
		} else if (repeating.isDaily()) {
			this.appointmentDate = a.getStart();
			this.endRepeatingDate = repeating.getEnd();
			if (!wholeDaysSet) {
				startTime.set(a.getStart().getTime());
				endTime.set(a.getEnd().getTime());

			} else {
				startTime.set(ALLDAY);
				endTime.set(ALLDAY);
			}

		}

	}

	private int getItemIndex(String[] arrayname, String value) {
		for (int i = 0; i < arrayname.length; i++) {
			if (arrayname[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}

	private String getWeekdayOfMonth(Date date) {
		StringBuffer b = new StringBuffer();
		Calendar cal = loc.createCalendar();
		cal.setTime(date);
		int numb = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		b.append(String.valueOf(numb));
		b.append('.');
		b.append(' ');
		return b.toString();
	}

	private String getDayOfMonth(Date date) {
		StringBuffer b = new StringBuffer();
		Calendar cal = loc.createCalendar();
		cal.setTime(date);
		int numb = cal.get(Calendar.DAY_OF_MONTH);
		b.append(String.valueOf(numb));
		b.append('.');
		b.append(' ');
		return b.toString();
	}

	/**
	 * uses the internal calendar object for date comparison.
	 * 
	 * @see DateTools#isSameDay(java.util.Calendar, Date, Date)
	 */
	private boolean isSameDay(Date d1, Date d2) {
		return DateTools.isSameDay(loc.createCalendar(), d1, d2);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		DatePickerDialog dateDlg = null;
		TimePickerDialog timeDlg = null;
		switch (id) {
		case DATE_START_DIALOG_ID:
			dateDlg = new DatePickerDialog(this, new MyOnDateSetListener(
					DATE_START_DIALOG_ID), 2011, 0, 1);
			dateDlg.setMessage(getString(R.string.start_date));
			break;
		case DATE_END_DIALOG_ID:
			dateDlg = new DatePickerDialog(this, new MyOnDateSetListener(
					DATE_END_DIALOG_ID), 2011, 0, 1);
			dateDlg.setMessage(getString(R.string.end_date));
			break;
		case TIME_START_DIALOG_ID:
			timeDlg = new TimePickerDialog(this, new myOnTimeSetListener(
					TIME_START_DIALOG_ID), startTime.hour, startTime.minute,
					true);
			timeDlg.setMessage("Start Time?");
			break;

		case TIME_END_DIALOG_ID:
			timeDlg = new TimePickerDialog(this, new myOnTimeSetListener(
					TIME_END_DIALOG_ID), endTime.hour, endTime.minute, true);
			timeDlg.setMessage("End Time?");
			break;
		}
		if (dateDlg == null) {
			return timeDlg;
		} else {
			return dateDlg;
		}

	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		DatePickerDialog dateDlg = null;
		TimePickerDialog timeDlg = null;

		int iDay, iMonth, iYear;
		Calendar cal = loc.createCalendar();

		super.onPrepareDialog(id, dialog);

		switch (id) {
		case DATE_START_DIALOG_ID:
			dateDlg = (DatePickerDialog) dialog;
			cal.setTime(appointmentDate);
			iDay = cal.get(Calendar.DAY_OF_MONTH);
			iMonth = cal.get(Calendar.MONTH);
			iYear = cal.get(Calendar.YEAR);
			dateDlg.updateDate(iYear, iMonth, iDay);
			break;
		case DATE_END_DIALOG_ID:
			dateDlg = (DatePickerDialog) dialog;
			
			// When repeating has no end date
			if (endRepeatingDate == null){
				endRepeatingDate = new Date();
			}
			cal.setTime(endRepeatingDate);
			iDay = cal.get(Calendar.DAY_OF_MONTH);
			iMonth = cal.get(Calendar.MONTH);
			iYear = cal.get(Calendar.YEAR);
			dateDlg.updateDate(iYear, iMonth, iDay);
			break;
		case TIME_START_DIALOG_ID:
			timeDlg = (TimePickerDialog) dialog;
			timeDlg.updateTime(this.startTime.hour, this.startTime.minute);
			break;
		case TIME_END_DIALOG_ID:
			timeDlg = (TimePickerDialog) dialog;
			timeDlg.updateTime(this.endTime.hour, this.endTime.minute);
			break;
		}
		return;
	}

	private class MyOnItemSelectedListener implements
			AdapterView.OnItemSelectedListener {
		Spinner spinnerNumber;
		ArrayAdapter a;
		LinearLayout appLayout;

		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			switch (parent.getId()) {
			case R.id.spinnerType:
				// lazy loading
				if (spinnerNumber == null || appLayout == null) {
					spinnerNumber = (Spinner) findViewById(R.id.spinnerNumber);
					appLayout = (LinearLayout) findViewById(R.id.appointmentLayout);
				}

				if (position == 4) {
					repeatingTypeAdapter = ArrayAdapter.createFromResource(
							getApplicationContext(), R.array.months,
							android.R.layout.simple_spinner_item);
				} else if (position == 1 || position == 3) {
					repeatingTypeAdapter = ArrayAdapter.createFromResource(
							getApplicationContext(), R.array.days,
							android.R.layout.simple_spinner_item);
				}
				repeatingType = position;
				storeAppointmentData();
				getSummary(appointment);
				displayView();

			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// Method provided by the interface that we have to implement but
			// doesn't do anything

		}

	}

	private class MyOnTouchListener implements View.OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			// depending on which spinner was touched show the appropriate
			// dialog :)
			switch (v.getId()) {
			case R.id.spinnerStartDate:
				showDialog(DATE_START_DIALOG_ID);
				break;
			case R.id.spinnerEndDate:
				showDialog(DATE_END_DIALOG_ID);
				break;

			case R.id.spinnerTime:
				showDialog(TIME_START_DIALOG_ID);
				break;
			}
			return true;
		}

	}

	private class MyOnDateSetListener implements OnDateSetListener {
		Time chosenDate;
		long dtDob;
		int dialogId;

		public MyOnDateSetListener(int id) {
			dialogId = id;
		}

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			chosenDate = new Time();
			chosenDate.set(dayOfMonth, monthOfYear, year);
			dtDob = chosenDate.toMillis(true);
			switch (dialogId) {
			case DATE_START_DIALOG_ID:
				// if (appointment.getRepeating() != null
				// && appointment.getRepeating().isYearly()) {
				// indexOfSelectedItemInNumberSpinner = monthOfYear;
				// repeatingFrequency = dayOfMonth;
				// } else if (appointment.getRepeating() != null
				// && appointment.getRepeating().isWeekly()
				// || appointment.getRepeating().isMonthly()) {
				// indexOfSelectedItemInNumberSpinner = getItemIndex(
				// daysOfWeek, loc.getWeekday(new Date(dtDob)));
				// }
				appointmentDate = new Date(dtDob);
				break;
			case DATE_END_DIALOG_ID:
				endRepeatingDate = new Date(dtDob);
				break;
			}
			storeAppointmentData();
			getSummary(appointment);
			displayView();

		}

	}

	private class myOnTimeSetListener implements OnTimeSetListener {
		int dialogId;

		public myOnTimeSetListener(int id) {
			dialogId = id;
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			Time chosenTime = new Time();
			chosenTime.set(0, minute, hourOfDay, chosenTime.monthDay,
					chosenTime.month, chosenTime.year);
			switch (dialogId) {
			case TIME_START_DIALOG_ID:
				startTime = chosenTime;
				showDialog(TIME_END_DIALOG_ID);
				break;
			case TIME_END_DIALOG_ID:
				endTime = chosenTime;
				storeAppointmentData();
				getSummary(appointment);
				displayView();
				break;
			}

		}

	}

}
