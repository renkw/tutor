/**
 * 
 */
package com.changev.tutor.web.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.FaqModel;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;

/**
 * @author zhaoqing
 *
 */
public class EditFaq implements View {

	/* (non-Javadoc)
	 * @see com.changev.tutor.web.View#preRender(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (StringUtils.isNotEmpty(request.getParameter("submit")))
			edit(request, response);
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
	
	
	private void edit(HttpServletRequest request, HttpServletResponse response){
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		FaqModel faq = new FaqModel();
		faq.setContent(content);
		faq.setTitle(title);
		ObjectContainer objc = Tutor.getCurrentContainer();
		objc.store(faq);
		objc.commit();
	}

}
