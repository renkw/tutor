/**
 * 
 */
package com.changev.tutor.web.service;

import java.util.Map;

import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;

/**
 * <p>家长登陆后返回自己的提问列表</p>
 * <p>关于分页：每次请求提交当前拿到第几页了，程序返回下一页的数量，之前的数据需要客户端自己缓存</p>
 * @author zhaoqing
 *<ul>客户端数据说明
 *	<li><b>current_page</b>当前第几页</li>
 *	<li><b>page_size</b>一页多少条数据</li>
 *</ul>
 */
public class ListMyQuestionService implements Service<Map> {

	
	
	@Override
	public String run(UserModel user, Map input) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
	
}
