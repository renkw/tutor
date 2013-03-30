/**
 * 
 */
package com.changev.tutor.web.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changev.tutor.Tutor;
import com.changev.tutor.dto.MainPageDTO;
import com.changev.tutor.model.FaqModel;
import com.changev.tutor.web.View;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

/**
 * @author zhaoqing
 *
 */
public class MainView implements View {

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		MainPageDTO man = new MainPageDTO();
		
		man.setFaqs(queryFaq());
		man.setNewses(queryNews());
		
		request.setAttribute("mainDTO", man);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	}

	
	private List<FaqModel> queryNews(){
		List<FaqModel> news = new ArrayList<FaqModel>();
		ObjectSet<FaqModel> questions = Tutor.getCurrentContainer().query(
		
		new Predicate<FaqModel>() {
			@Override
			public boolean match(FaqModel candidate) {
				return candidate.getType() == 1;
			}
		},
		new QueryComparator<FaqModel>() {
			public int compare(FaqModel first, FaqModel second) {
				return first.getCreateDateTime().compareTo(second.getCreateDateTime());
			}
		});
		ListIterator<FaqModel> r = questions.listIterator();
		int count = 0;
		while(r.hasNext() && count < 8){
			count++;
			news.add(r.next());
		}
		return news;
	}
	
	
	private List<FaqModel> queryFaq(){
		List<FaqModel> faq = new ArrayList<FaqModel>();
		ObjectSet<FaqModel> questions = Tutor.getCurrentContainer().query(
		
		new Predicate<FaqModel>() {
			@Override
			public boolean match(FaqModel candidate) {
				return candidate.getType() == 0;
			}
		},
		new QueryComparator<FaqModel>() {
			public int compare(FaqModel first, FaqModel second) {
				return first.getCreateDateTime().compareTo(second.getCreateDateTime());
			}
		});
		ListIterator<FaqModel> r = questions.listIterator();
		int count = 0;
		while(r.hasNext() && count < 8){
			count++;
			faq.add(r.next());
		}
		return faq;
	}
	
}
