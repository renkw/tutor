/*
 * File   ParentModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;

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

	public ParentModel() {
		super.setRole(UserRole.Parent);
	}

	@Override
	public void setRole(UserRole role) {
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

}
