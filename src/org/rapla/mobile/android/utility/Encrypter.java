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

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class provides encrypt and decrypt logic. Strings are being encrypted
 * with AES and a 16 byte secret key.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class Encrypter {

	private Cipher cipher;
	private String secretKey;
	private SecretKeySpec secretKeySpec;

	public Encrypter() {

		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public Encrypter(String secretKey) {
		this();
		this.setSecretKey(secretKey);
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;

		secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(), "AES");
	}

	public String encrypt(String text) throws Exception {
		if (text == null)
			throw new Exception("Empty string");

		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] cipherText = new byte[cipher
					.getOutputSize(text.getBytes().length)];
			int ctLength = cipher.update(text.getBytes(), 0,
					text.getBytes().length, cipherText, 0);
			ctLength += cipher.doFinal(cipherText, ctLength);
			return bytesToHex(cipherText);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String decrypt(String code) throws Exception {
		if (code == null)
			throw new Exception("Empty string");

		try {
			byte[] byteCode = hexToBytes(code);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] plainText = new byte[cipher.getOutputSize(byteCode.length)];
			int ptLength = cipher.update(byteCode, 0, byteCode.length,
					plainText, 0);
			ptLength += cipher.doFinal(plainText, ptLength);
			return new String(plainText).trim();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String bytesToHex(byte[] data) {
		StringBuffer strbuf = new StringBuffer(data.length * 2);
		int i;

		for (i = 0; i < data.length; i++) {
			if (((int) data[i] & 0xff) < 0x10) {
				strbuf.append("0");
			}

			strbuf.append(Long.toString((int) data[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	public byte[] hexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}