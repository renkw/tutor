/**
 * 
 */
package com.changev.tutor.web.service;

import java.util.Map;

import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;

/**
 * @author zhaoqing
 *查看一个提问的回答详情
 *<p>
 *	回答时间倒序后取第一个，之后传递index获取，可以考虑用缓存
 *	<ul>
 *		<li>questionId => 问题id</li>
 *		<li>index => 请求的index</li>
 *	</ul>
 *</p>
 */
public class ViewDetailService implements Service<Map> {

	@Override
	public String run(UserModel user, Map input) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
}
