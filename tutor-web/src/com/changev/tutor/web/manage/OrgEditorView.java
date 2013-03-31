/**
 * 
 */
package com.changev.tutor.web.manage;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.OrgInfoModel;
import com.changev.tutor.model.OrgInfoModel;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * @author zhaoqing
 *
 */
public class OrgEditorView implements View {

	private ParamValidator submitValidator;
	
	public ParamValidator getSubmitValidator() {
		return submitValidator;
	}
	
	public void setSubmitValidator(ParamValidator submitValidator) {
		this.submitValidator = submitValidator;
	}
	
	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (StringUtils.isNotEmpty(request.getParameter("submit"))){
			//if (submitValidator == null || submitValidator.validate(request)) {
				edit(request, response);
				response.sendRedirect(request.getContextPath() + "/manage/orgEditorList.html");
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

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub

	}
	
	private void select(HttpServletRequest request){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		OrgInfoModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<OrgInfoModel> oset = objc.queryByExample(new OrgInfoModel(id));
			faq = oset.get(0);
			request.setAttribute("faq", faq);
		}
	}
	
	private void delete(HttpServletRequest request){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		OrgInfoModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<OrgInfoModel> oset = objc.queryByExample(new OrgInfoModel(id));
			faq = oset.get(0);
			faq.setDeleted(!faq.getDeleted());
			objc.store(faq);
			objc.commit();
		}
	}
	
	private void edit(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		OrgInfoModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<OrgInfoModel> oset = objc.queryByExample(new OrgInfoModel(id));
			faq = oset.get(0);
			faq.setUpdateDateTime(new Date());
		}
		else{
			faq = new OrgInfoModel();
			faq.setId(System.currentTimeMillis());
		}
		String type = request.getParameter("type");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String tags = request.getParameter("tags");
		faq.setOwerner(SessionContainer.get(request).getLoginUserId());
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
			faq.setContent(content.trim());
		}
		if(StringUtils.isNotEmpty(tags)){
			faq.setTags(tags);
		}
		objc.store(faq);
		objc.commit();
	}

}
