/**
 * 
 */
package com.changev.tutor.web.manage;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.FaqModel;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * @author zhaoqing
 *
 */
public class FaqView implements View {

	private ParamValidator submitValidator;
	
	public ParamValidator getSubmitValidator() {
		return submitValidator;
	}
	
	public void setSubmitValidator(ParamValidator submitValidator) {
		this.submitValidator = submitValidator;
	}
	
	/* (non-Javadoc)
	 * @see com.changev.tutor.web.View#preRender(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (StringUtils.isNotEmpty(request.getParameter("submit"))){
			//if (submitValidator == null || submitValidator.validate(request)) {
				edit(request, response);
				response.sendRedirect(request.getContextPath() + "/manage/faqList.html");
			//}
		}
		if(StringUtils.equals("edit", request.getParameter("m"))){
			select(request);
		}
		if(StringUtils.equals("delete", request.getParameter("m"))){
			delete(request);
		}
			
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
	
	private void select(HttpServletRequest request){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		FaqModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<FaqModel> oset = objc.queryByExample(new FaqModel(id));
			faq = oset.get(0);
			request.setAttribute("faq", faq);
		}
	}
	
	private void delete(HttpServletRequest request){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		FaqModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<FaqModel> oset = objc.queryByExample(new FaqModel(id));
			faq = oset.get(0);
			faq.setDeleted(!faq.getDeleted());
			objc.store(faq);
			objc.commit();
		}
	}
	
	private void edit(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		FaqModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<FaqModel> oset = objc.queryByExample(new FaqModel(id));
			faq = oset.get(0);
			faq.setUpdateDateTime(new Date());
		}
		else{
			faq = new FaqModel();
			faq.setId(System.currentTimeMillis());
		}
		String type = request.getParameter("type");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String tags = request.getParameter("tags");
		if(StringUtils.isEmpty(type)){
			faq.setType(0);
		}
		else{
			faq.setType(Integer.parseInt(type));
		}
		if(StringUtils.isNotEmpty(title)){
			faq.setTitle(title);
		}
		if(StringUtils.isNotEmpty(content)){
			faq.setContent(content);
		}
		if(StringUtils.isNotEmpty(tags)){
			faq.setTags(tags);
		}
		objc.store(faq);
		objc.commit();
	}

}
