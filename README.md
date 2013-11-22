Rapla Mobile Client for Android
===============================

Resource scheduling and event planing from your Android device

General Information
===================

[Rapla](https://code.google.com/p/rapla/) is a flexible multi-user resource and event planing system. It features multiple calendars views, conflict-management, fully configurable resource and event types and many import/export functions.

Unfortunately, you can only perform changes in the Rapla system from a computer with Internet access.

This mobile application for Android removes the obstacle and enables users to manage their event planing from smart phone.

User Interface
==============

Login
-----

The login screen enables you to change your Rapla server credentials, which are necessary to establish a connection to the server.

![Login](https://dl.dropboxusercontent.com/u/3672489/Rapla/login.png)

Calendar Overview
-----------------

The calendar overview lists all calendars maintained for you on the Rapla server. Clicking a calendar opens the integrated HTML5
calendar view. Additionally, you can create a new event or go settings from the menu.

![Calendar Overview](https://dl.dropboxusercontent.com/u/3672489/Rapla/calcoverwie.png)

HTML5 Calendar
--------------

The HTML5 calendar displays all reservations of the selected calendar for the given period of time. Clicking a reservation opens the
reservation details screen.

![HTML5 Calendar](https://dl.dropboxusercontent.com/u/3672489/Rapla/htmlcalc.png)

HTML5 Reservation Details
-------------------------

The HTML5 reservation details list the reservation’s name, the assigned resources and the corresponding appointments.
An edit button allows for entering the edit mode in the native client application.

![HTML5 Reservation Details](https://dl.dropboxusercontent.com/u/3672489/Rapla/htmlreservation.png)

Edit Reservation
----------------

The edit reservation screen lists all dynamic attributes that are maintained for the selected event type. If you change the event type,
the list of attributes might change.

You can go to the resources and appointments by clicking the corresponding button. The number in brackets indicates how many
items are currently assigned to the reservation. The save button saves the reservation and then returns to the HTML5
reservation details. The cancel button cancels all changes made to the reservation, the assigned resources and appointments. This action needs to be confirmed and then returns to the HTML5 reservation details. The reservation can be deleted from the menu after confirming the action.

![Edit Reservation](https://dl.dropboxusercontent.com/u/3672489/Rapla/editreservation.png)

Resource Categories Overview
----------------------------

The resource categories overview lists all available resource categories. By clicking a category, all related resources are displayed.
The second line of each list item specifies the number of resources selected and number of resources available on that category.

![Resource Categories Overview](https://dl.dropboxusercontent.com/u/3672489/Rapla/resourcecatagories.png)

Resource Overview
-----------------

The resource overview lists all resources related to the previously selected resource category. A resource can be assigned by clicking the check box on the right. In a pop-up, you can specify the appointments for which the resource should be used. If you want to edit the
appointment assignments for a resource, you can call the pop-up by clicking the resource item. If you want to remove a resource from the
reservation, long-click a resource item and selected “Undo booking”. You will need to confirm this action.

![Resource Overview](https://dl.dropboxusercontent.com/u/3672489/Rapla/resoruceoverwie.png)

Appointment Overview
--------------------

The appointment overview lists all appointments assigned to the reservation. You can go to the appointment details by clicking an
appointment item. A new appointment can be created from the menu. If you want to delete an appointment, long-click an
appointment item and select “Delete”. You will need to confirm this action.

![Appointment Overview](https://dl.dropboxusercontent.com/u/3672489/Rapla/appointmentoverview.png)

Appointment Details
-------------------

The appointment details display all data for an appointment. The appointment type can be changed with the first drop-down element.
The other fields adjust accordingly. The changes are applied by pressing the back button.

![Appointment Details](https://dl.dropboxusercontent.com/u/3672489/Rapla/appointmentdetails.png)

App Setup
=========

Prerequisites
-------------

The following prerequisites must be met in order to run the application:

*Android version 2.2 or greater
*A network connection must be available at all times
*The Rapla server must be enabled for the client application

Initial Start
-------------

When first starting the app, you are requested to enter your credentials and the Rapla server url with port. Please contact your Rapla administrator, if you don’t know the server url or port.

Technical Documentation
=======================

This gives an overview of the development of the Rapla mobile client for Android smartphones and is intended for developers who want to continue working on the client.

General Information
-------------------

The Rapla mobile client for Android smartphones follows the regular Android development principals. There are a couple of activities, each representing an application screen. A base activity class offers functionality across all other activities and a custom application class is used for keeping data across the application’s lifecycle. Operations that might take some time are usually executed in background threads (AsyncTask). Otherwise the Android UI will be blocked until the operation finished.

Internationalization and Localization
-------------------------------------

The application’s default language is English. Internationalization is achieved by providing additional language files in the corresponding resource folder (e.g. strings-de).

Server Interaction
------------------

The client interacts with the server via the regular client façade which is also used for the desktop client. Hence, the interaction is not optimized for mobile scenarios.

Additionally, the server version must be the same as for the client (see doc.properties file in the
Rapla root directory).

Application Runtime Storage
---------------------------

If the login succeeds, the ConnectToServerBackgroundAsyncTask stores a RaplaConnection instance
in the application’s runtime storage (RuntimeStorage). This enables methods like getFacade of the
BaseActivity to provide a reference to the ClientFacade.

If a reservation is edited, a reference is stored in the runtime storage via setSelectedReservation and retrieved via getSelectedReservation (see BaseActivity). This reference is later used to retrieve data like assigned resources or appointments.

Unit Tests
----------

The Android test project is located in the tests directory within the project. With Eclipse, the test project can be imported as a separate project. Usually, it is not associated with version control so changes in the test project need to be committed via the actual project.

To execute the unit tests, there is no need to configure or start a Rapla server. All unit tests run without a server by using mock objects.

Naming Convention
-----------------

Each test class corresponds to a class in production. E.g. the corresponding unit test class for class BaseActivity would be BaseActivityTest. The test classes are also located in the corresponding package, adding test after org.rapla.mobild.android.

Test cases (methods) start with test, most often followed by the method name that is tested and the
expected behavior or output.

There are also some integration test cases with usually start with testIntegrationScenario followed by a more detailed description of the test case.

Set Up Test Environment
-----------------------

In order to set up a test environment, you first need to start a Rapla server. For development, this can usually be done on the local machine.

If you start the application on a local emulator, you can use IP 10.0.2.2 to access the localhost of your actual computer. This will not work, if you use a device with USB connection.

For a quick login, you can add the user credentials in the PreferencesHandler constructor by calling setUsername, etc. Do not forget to remove this code for production.

To skip the HTML5 integration, set the USE_DEMO_HOME attribute of class RaplaMobileApplication
to true and set the DummyHomeActivity as start activity in the Android manifest. The
DummyHomeActivity simply lists all available reservations for the logged in user. That way, you can
avoid calling the HTML5 calendar and entering the edit mode via selecting a reservation.