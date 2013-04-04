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
import com.changev.tutor.model.FaqModel;
import com.changev.tutor.util.Rewriter;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * @author zhaoqing
 *
 */
public class DetailView implements View {

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		FaqModel faq = select(request);
		findRelated(faq, request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	}
	
	private FaqModel select(HttpServletRequest request){
		String id = Rewriter.getViewId(request.getServletPath());
		ObjectContainer objc = Tutor.getCurrentContainer();
		FaqModel faq = null;
		if(StringUtils.isNotEmpty(id)){
			ObjectSet<FaqModel> oset = objc.queryByExample(new FaqModel(id));
			faq = oset.get(0);
			request.setAttribute("faq", faq);
			return faq;
		}
		return null;
	}
	
	private void findRelated(FaqModel faq, HttpServletRequest request){
		List<FaqModel> news = new ArrayList<FaqModel>();
		if(faq == null){
			request.setAttribute("relations", news);
			return;
		}
		final int type = faq.getType();
		final long sid = faq.getId();
		ObjectSet<FaqModel> questions = Tutor.getCurrentContainer().query(
		new Predicate<FaqModel>() {
			@Override
			public boolean match(FaqModel candidate) {
				return candidate.getId() != sid && candidate.getType() == type && candidate.getDeleted() == false;
			}
		});
		ListIterator<FaqModel> r = questions.listIterator();
		int count = 0;
		while(r.hasNext() && count < 4){
			count++;
			news.add(r.next());
		}
		request.setAttribute("relations", news);
	}

}
