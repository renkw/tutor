/**
 * 
 */
package com.changev.tutor.web.pub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AnswerModel;
import com.changev.tutor.model.FaqModel;
import com.changev.tutor.web.View;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

/**
 * @author zhaoqing
 *
 */
public class FaqView implements View {

	/* (non-Javadoc)
	 * @see com.changev.tutor.web.View#preRender(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		view(request, response);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.changev.tutor.web.View#postRender(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub

	}
	
	private void view(HttpServletRequest request, HttpServletResponse response){
		String title = "系统繁忙，请稍后访问";
		String content = "";
		final long id = Long.parseLong(request.getParameter("nid"));
		Query q = Tutor.getCurrentContainer().query();
		ObjectSet<FaqModel> faq = Tutor.getCurrentContainer().query(new Predicate<FaqModel>() {
			@Override
			public boolean match(FaqModel candidate) {
				return id == candidate.getId();
			}
		});
		if(faq.size() > 0){
			title = faq.get(0).getTitle();
			content = faq.get(0).getContent();
		}
		request.setAttribute("title", title);
		request.setAttribute("content", content);
	}

}
