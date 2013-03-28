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

package org.rapla.mobile.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.rapla.mobile.android.utility.Encrypter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

/**
 * PreferencesHandler (Singleton)
 * 
 * This class handles access to the preferences such as username, password and
 * server url. The password is being encrypted before being stored.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class PreferencesHandler {

	protected static PreferencesHandler instance;
	private SharedPreferences preferences = null;
	private Encrypter encrypter = null;

	public final static String PREFERENCES_NAMESPACE = "RaplaClient";
	public final static String KEY_SECRET_KEY = "SecretKey";
	public final static String KEY_USERNAME = "Username";
	public final static String KEY_PASSWORD = "Password";
	public final static String KEY_HOST = "Host";
	public final static String KEY_HOST_POST = "HostPort";
	public final static String KEY_IS_SECURE = "IsSecure";

	/**
	 * Private constructor to enable singleton
	 */
	private PreferencesHandler(Context c, Encrypter e) {
		this.preferences = c.getSharedPreferences(PREFERENCES_NAMESPACE,
				Context.MODE_PRIVATE);

		// Check whether a secret key has to be initialized
		if (this.preferences.getString(KEY_SECRET_KEY, null) == null) {
			this.initializeSecretKey();
		}

		// Set encrypter
		this.encrypter = e;
		this.encrypter.setSecretKey(this.getSecretKey());
	}
	
	/**
	 * This constructor is only to be used within unit tests
	 */
	protected PreferencesHandler() {
		// To be used only within unit tests
	}

	/**
	 * Initialize secret key for encrypting user password
	 * 
	 * @throws RuntimeException
	 */
	private void initializeSecretKey() {
		// Random string
		String baseKey = "" + SystemClock.currentThreadTimeMillis()
				+ new Random().nextInt();

		// md5 random string
		MessageDigest digest;
		String secretKey;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(baseKey.getBytes());
			byte messageDigest[] = digest.digest();
			int len = messageDigest.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character
						.forDigit((messageDigest[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(messageDigest[i] & 0x0f, 16));
			}
			secretKey = sb.substring(0, 32);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		// Set secret key
		this.setSecretKey(secretKey);
	}

	/**
	 * Get instance of PreferencesHandler
	 * 
	 * @return Singleton instance
	 * @throws RuntimeException
	 */
	public static PreferencesHandler getInstance() {
		if (instance == null) {
			throw new RuntimeException(
					"Preferences handler hasn't been initialized.");
		}
		return instance;
	}

	/**
	 * Get instance of PreferencesHandler
	 * 
	 * @param c
	 *            Current Context
	 * @return Singleton instance
	 */
	public static PreferencesHandler getInstance(Context c, Encrypter e) {
		if (instance == null) {
			instance = new PreferencesHandler(c, e);
		}
		return instance;
	}

	public String getUsername() {
		return this.preferences.getString(KEY_USERNAME, null);
	}

	public void setUsername(String username) {
		this.preferences.edit().putString(KEY_USERNAME, username).commit();
	}

	public String getPassword() {
		try {
			return new String(this.encrypter.decrypt(this.preferences
					.getString(KEY_PASSWORD, null)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setPassword(String password) {
		try {
			String encryptedPassword = this.encrypter.encrypt(password);
			this.preferences.edit()
					.putString(KEY_PASSWORD, encryptedPassword)
					.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getHost() {
		return this.preferences.getString(KEY_HOST, null);
	}

	public void setHost(String host) {
		this.preferences.edit().putString(KEY_HOST, host).commit();
	}

	public boolean hasHost() {
		return this.preferences.contains(KEY_HOST);
	}

	public int getHostPort() {
		return this.preferences.getInt(KEY_HOST_POST, 0);
	}

	public void setHostPort(int port) {
		this.preferences.edit().putInt(KEY_HOST_POST, port).commit();
	}

	public boolean hasHostPort() {
		return this.preferences.contains(KEY_HOST_POST);
	}

	/**
	 * @return Whether username, password, host and port are set
	 */
	public boolean hasConnectionPreferences() {
		return this.hasUsername() && this.hasPassword() && this.hasHost()
				&& this.hasHostPort();
	}

	private void setSecretKey(String key) {
		this.preferences.edit().putString(KEY_SECRET_KEY, key).commit();
	}

	private String getSecretKey() {
		return this.preferences.getString(KEY_SECRET_KEY, null);
	}

	public boolean hasUsername() {
		return this.preferences.contains(KEY_USERNAME);
	}

	public boolean hasPassword() {
		return this.preferences.contains(KEY_PASSWORD);
	}
	
	public void setSecure(boolean secured) {
		this.preferences.edit().putBoolean(KEY_IS_SECURE, secured).commit();
	}
	
	public boolean isSecure() {
		return this.preferences.getBoolean(KEY_IS_SECURE, false);
	}

}
