/**
 * 
 */
package com.changev.tutor.web.service;

import static com.changev.tutor.web.service.ServiceRender.renderJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

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
		List result = new ArrayList();
		int current_page = Integer.parseInt(input.get("current_page").toString());
		Query q = Tutor.getCurrentContainer().query();
		QuestionModel question = new QuestionModel();
		question.setUser_id(user.getEmail());
		question.setDeleted(false);
		q.constrain(question);
		ObjectSet<QuestionModel> questions = q.descend("createDateTime").orderDescending().execute();
		//TODO add to cache{}
		for(int index = 0; index < 10; index++){
			result.add(questions.get(current_page * 10 + index));
		}
		return renderJson(result);
	}
	
}
