/**
 * 
 */
package com.changev.tutor.web.service;

import java.util.Map;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;

/**
 * @author zhaoqing
 *	提交一个问题 默认的类型是试卷
 *<p>
 *	参数定义
 *	<ul>
 *		<li>type => 类型，默认为试卷，第一期可以不传</li>
 *		<li>resource => (图片或文档，需要单独存储的二进制文件地址集合)</li>
 *		<li>title => 标题</li>
 *		<li>subject => 科目</li>
 *	</ul>
 *</p>
 */
public class SumbitQuestionService implements Service<Map> {

	@Override
	public String run(UserModel user, Map input) throws Throwable {
		QuestionModel question = new QuestionModel();
		question.setCreateDateTime(Tutor.currentTimestamp());
		question.setUser_id(user.getEmail());
		question.setTitle(String.valueOf(input.get("title")));
		question.setType(0);
		question.setSubject(String.valueOf(input.get("subject")));
		Tutor.getCurrentContainer().store(question);
		Tutor.getCurrentContainer().commit();
		return "提交成功";
	}

}
