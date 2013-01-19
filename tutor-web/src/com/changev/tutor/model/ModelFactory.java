/*
 * File   ModelFactory.java
 * Create 2013/01/01
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import com.changev.tutor.Tutor;

/**
 * <p>
 * 模型常用方法。
 * </p>
 * 
 * @author ren
 * 
 */
public final class ModelFactory {

	public static String getUserSemaphore(String email) {
		return "com.changev.tutor.model.UserModel:" + email;
	}

	public static UserModel getUserExample(String email) {
		UserModel userExample = new UserModel();
		userExample.setEmail(email);
		userExample.setDeleted(Boolean.FALSE);
		return userExample;
	}

	public static UserModel getUserExample(String email, UserRole role) {
		UserModel userExample = new UserModel();
		userExample.setEmail(email);
		userExample.setRole(role);
		userExample.setDeleted(Boolean.FALSE);
		return userExample;
	}

	public static UserModel getUserExample(String email, String password) {
		UserModel userExample = new UserModel();
		userExample.setEmail(email);
		userExample.setPassword(password);
		userExample.setDeleted(Boolean.FALSE);
		return userExample;
	}

	public static StudentModel getParentStudentExample(String email) {
		ParentModel parentExample = new ParentModel();
		parentExample.setEmail(email);
		StudentModel studentExample = new StudentModel();
		studentExample.setRole(UserRole.Student);
		studentExample.setParent(parentExample);
		studentExample.setDeleted(Boolean.FALSE);
		return studentExample;
	}

	public static TeacherModel getOrganizationTeacherExample(String email) {
		OrganizationModel organizationExample = new OrganizationModel();
		organizationExample.setEmail(email);
		TeacherModel model = new TeacherModel();
		model.setRole(UserRole.Teacher);
		model.setOrganization(organizationExample);
		model.setDeleted(Boolean.FALSE);
		return model;
	}

	public static String encryptPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			String s = "\u0162\u1ee7" + password + "\u0236\u2c7a\ua775";
			return Tutor.toHex(digest.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String generateSecureCode() {
		return Tutor.randomHex(128);
	}

	public static String encryptUserCode(String email, String token) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, Tutor.AES_KEY);
			String s = email + "::" + token;
			return Tutor.toHex(cipher.doFinal(s.getBytes()));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public static String[] decryptUserCode(String code) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, Tutor.AES_KEY);
			String s = new String(cipher.doFinal(Tutor.fromHex(code)));
			int i = s.indexOf("::");
			return i <= 0 || i >= s.length() - 1 ? null : new String[] {
					s.substring(0, i), s.substring(i + 2) };
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
