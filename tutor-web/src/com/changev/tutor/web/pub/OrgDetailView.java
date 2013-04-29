/**
 * 
 */
package com.changev.tutor.web.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.OrgInfoModel;
import com.changev.tutor.util.Rewriter;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * @author zhaoqing
 *
 */
public class OrgDetailView implements View {

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		OrgInfoModel faq = select(request);
		findRelated(faq, request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	}
	
	private OrgInfoModel select(HttpServletRequest request){
		String id = Rewriter.getViewId(request.getServletPath());
		ObjectContainer objc = Tutor.getCurrentContainer();
		OrgInfoModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<OrgInfoModel> oset = objc.queryByExample(new OrgInfoModel(id));
			faq = oset.get(0);
			request.setAttribute("faq", faq);
			return faq;
		}
		return null;
	}
	
	private void findRelated(OrgInfoModel faq, HttpServletRequest request){
		List<OrgInfoModel> news = new ArrayList<OrgInfoModel>();
		if(faq == null){
			request.setAttribute("relations", news);
			return;
		}
		final int type = faq.getType();
		final long sid = faq.getId();
		final long owern = faq.getOwerner(); 
		ObjectSet<OrgInfoModel> questions = Tutor.getCurrentContainer().query(
		new Predicate<OrgInfoModel>() {
			@Override
			public boolean match(OrgInfoModel candidate) {
				return owern == candidate.getOwerner() && candidate.getId() != sid && candidate.getType() == type && candidate.getDeleted() == false;
			}
		});
		ListIterator<OrgInfoModel> r = questions.listIterator();
		int count = 0;
		while(r.hasNext() && count < 4){
			count++;
			news.add(r.next());
		}
		request.setAttribute("relations", news);
	}

}
