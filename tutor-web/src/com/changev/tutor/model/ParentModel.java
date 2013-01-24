/*
 * File   ParentModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;
import java.util.Set;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableHashSet;

/**
 * <p>
 * 家长。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParentModel extends UserModel {

	private static final long serialVersionUID = 5756233700028997499L;

	private Set<UserModel> contacters;

	public ParentModel() {
		super.setRole(UserRole.Parent);
	}

	public List<StudentModel> getChildren() {
		return Tutor.getCurrentContainer().queryByExample(
				ModelFactory.getParentStudentExample(getEmail()));
	}

	public List<QuestionModel> getQuestions() {
		// TODO
		throw new UnsupportedOperationException();
	}

	public List<QuestionModel> getUnclosedQuestions() {
		// TODO
		throw new UnsupportedOperationException();
	}

	public List<QuestionModel> getClosedQuestions() {
		// TODO
		throw new UnsupportedOperationException();
	}

	@Override
	public void objectOnActivate(ObjectContainer container) {
		super.objectOnActivate(container);
	}

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
	}

	@Override
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
	}

	@Override
	public ParentModel clone() {
		return (ParentModel) super.clone();
	}

	@Override
	public void setRole(UserRole role) {
	}

	/**
	 * @return the contacters
	 */
	public Set<UserModel> getContacters() {
		beforeGet();
		return contacters;
	}

	/**
	 * @return the contacters
	 */
	public Set<UserModel> getContactersFor() {
		beforeGet();
		if (contacters == null) {
			beforeSet();
			contacters = new ActivatableHashSet<UserModel>();
		}
		return contacters;
	}

}
