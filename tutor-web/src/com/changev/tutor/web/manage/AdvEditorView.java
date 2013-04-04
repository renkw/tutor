/**
 * 
 */
package com.changev.tutor.web.manage;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.PictureModel;
import com.changev.tutor.util.Rewriter;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * @author zhaoqing
 *
 */
public class AdvEditorView implements View {

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
				response.sendRedirect(request.getContextPath() + "/manage/advEditorList.html");
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
		PictureModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<PictureModel> oset = objc.queryByExample(new PictureModel(id));
			faq = oset.get(0);
			request.setAttribute("adv", faq);
		}
	}
	
	private void delete(HttpServletRequest request){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		PictureModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<PictureModel> oset = objc.queryByExample(new PictureModel(id));
			faq = oset.get(0);
			faq.setDeleted(!faq.getDeleted());
			objc.store(faq);
			objc.commit();
		}
	}
	
	private void edit(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		ObjectContainer objc = Tutor.getCurrentContainer();
		PictureModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<PictureModel> oset = objc.queryByExample(new PictureModel(id));
			faq = oset.get(0);
			faq.setUpdateDateTime(new Date());
		}
		else{
			faq = new PictureModel();
			faq.setId(System.currentTimeMillis());
		}
		String title = request.getParameter("title");
		String url = request.getParameter("url");
		String link2 = request.getParameter("link2");
		String index = request.getParameter("index");
		faq.setOwener(Rewriter.userHash(SessionContainer.get(request).getLoginUserId_str()));
		if(StringUtils.isEmpty(index)){
			faq.setIndex(0);
		}
		else{
			faq.setIndex(Integer.parseInt(index));
		}
		if(StringUtils.isNotEmpty(title)){
			faq.setTitle(title);
		}
		if(StringUtils.isNotEmpty(url)){
			faq.setUrl(url);
		}
		if(StringUtils.isNotEmpty(link2)){
			faq.setLink2(link2);
		}
		objc.store(faq);
		objc.commit();
	}

}
