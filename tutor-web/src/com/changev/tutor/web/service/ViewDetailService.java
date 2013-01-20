/**
 * 
 */
package com.changev.tutor.web.service;

import static com.changev.tutor.web.service.ServiceRender.renderJson;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AnswerModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.query.QueryComparator;

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
		Query q = Tutor.getCurrentContainer().query();
		final int index = Integer.parseInt(String.valueOf(input.get("index")));
		final String qId = String.valueOf(input.get("questionId"));
		
		ObjectSet<AnswerModel> answers = Tutor.getCurrentContainer().query(new Predicate<AnswerModel>() {
			@Override
			public boolean match(AnswerModel candidate) {
				return StringUtils.equals(qId, candidate.getQuestion_id()) && (candidate.getDeleted() == false);
			}
		}, 
			new QueryComparator<AnswerModel>() {
				public int compare(AnswerModel first, AnswerModel second) {
					if(first.getCreateDateTime().after(second.getCreateDateTime())) return 1;
					return -1;
				}
			}	);
		if(answers.size() > index){
			return renderJson(answers.get(index));
		}
		else{
			return "没有更多的回复了";
		}
	}
}
