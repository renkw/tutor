/**
 * 
 */
package com.changev.tutor.web.pub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.FaqModel;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * @author zhaoqing
 *
 */
public class DetailView implements View {

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		select(request);
		
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	}
	
	private void select(HttpServletRequest request){
		String id = getViewId(request.getServletPath());
		ObjectContainer objc = Tutor.getCurrentContainer();
		FaqModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<FaqModel> oset = objc.queryByExample(new FaqModel(id));
			faq = oset.get(0);
			request.setAttribute("faq", faq);
		}
	}
	
	/**
	 * <p>
	 * rewrite 取请求的id
	 * </p>
	 * 
	 * @param path
	 * @return
	 */
	protected String getViewId(String path) {
		String id = "";
		int start = path.charAt(0) == '/' ? 1 : 0;
		int end = path.lastIndexOf('.');
		if (end == -1)
			end = path.length();
		id = path.substring(start, end);
		//rewrite
		if(id.contains("_")){
			String[] r_name = id.split("\\_");
			id = r_name[1];
		}
				
		return id;
	}

}
