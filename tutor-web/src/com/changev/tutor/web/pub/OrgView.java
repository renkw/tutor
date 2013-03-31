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
import com.changev.tutor.dto.OrgDTO;
import com.changev.tutor.model.OrgInfoModel;
import com.changev.tutor.model.OrganizationModel;
import com.changev.tutor.util.Rewriter;
import com.changev.tutor.web.View;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

/**
 * @author zhaoqing
 *
 */
public class OrgView implements View {

	
	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		String id = Rewriter.getViewId(request.getServletPath());
		final long l_id = Long.parseLong(id);
		OrgDTO orgDTO = new OrgDTO();

		OrganizationModel org = queryOrg(l_id);
		if(org == null){
			return false;
		}
		
		orgDTO.setTeachers(org.getAlivableTeachers(12));
		orgDTO.setOrg(org);
		orgDTO.setActivitys(queryActivity(l_id));
		orgDTO.setNewses(queryNews(l_id));
		
		request.setAttribute("orgDTO", orgDTO);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub

	}

	private OrganizationModel queryOrg(final long l_id){
		
		ObjectSet<OrganizationModel> orgs = Tutor.getCurrentContainer().query(
				new Predicate<OrganizationModel>() {
					@Override
					public boolean match(OrganizationModel candidate) {
						return candidate.getDeleted() == false && Tutor.getCurrentContainerExt().getID(candidate) == l_id;
					}
				});
		return orgs.get(0);
	}
	
	private List<OrgInfoModel> queryNews(final long owener){
		List<OrgInfoModel> news = new ArrayList<OrgInfoModel>();
		ObjectSet<OrgInfoModel> questions = Tutor.getCurrentContainer().query(
		
		new Predicate<OrgInfoModel>() {
			@Override
			public boolean match(OrgInfoModel candidate) {
				return candidate.getType() == 1 && candidate.getDeleted() == false && candidate.getOwerner() == owener;
			}
		},
		new QueryComparator<OrgInfoModel>() {
			public int compare(OrgInfoModel first, OrgInfoModel second) {
				return second.getCreateDateTime().compareTo(first.getCreateDateTime());
			}
		});
		ListIterator<OrgInfoModel> r = questions.listIterator();
		int count = 0;
		while(r.hasNext() && count < 8){
			count++;
			news.add(r.next());
		}
		return news;
	}
	
	
	private List<OrgInfoModel> queryActivity(final long owener){
		List<OrgInfoModel> faq = new ArrayList<OrgInfoModel>();
		ObjectSet<OrgInfoModel> questions = Tutor.getCurrentContainer().query(
		
		new Predicate<OrgInfoModel>() {
			@Override
			public boolean match(OrgInfoModel candidate) {
				return candidate.getType() == 2  && candidate.getDeleted() == false && candidate.getOwerner() == owener;
			}
		},
		new QueryComparator<OrgInfoModel>() {
			public int compare(OrgInfoModel first, OrgInfoModel second) {
				return second.getCreateDateTime().compareTo(first.getCreateDateTime());
			}
		});
		ListIterator<OrgInfoModel> r = questions.listIterator();
		int count = 0;
		while(r.hasNext() && count < 8){
			count++;
			faq.add(r.next());
		}
		return faq;
	}
	
}
