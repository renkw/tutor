/**
 * 
 */
package com.changev.tutor.web.pub;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.model.FaqModel;
import com.changev.tutor.util.PageList;
import com.changev.tutor.web.View;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

/**
 * @author zhaoqing
 *
 */
public class ListView  implements View {

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		listFaq(request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		
	}

	
	private void listFaq(HttpServletRequest request) {

		// parameters
		String pageno = request.getParameter("pageno");
		String type = request.getParameter("type");
		if(StringUtils.isEmpty(type)){
			type = "0";
		}
		final int i_type = Integer.parseInt(type);
		PageList<FaqModel> questionList = null;
		if (questionList == null || StringUtils.isEmpty(pageno)) {
			//pageno = "1";
			questionList = null;

			// get answered questions
			questionList = new PageList<FaqModel>(
					new Predicate<FaqModel>() {
						@Override
						public boolean match(FaqModel candidate) {
							return candidate.getType() == i_type && candidate.getDeleted()==false;
						}
					},
					new QueryComparator<FaqModel>() {
						public int compare(FaqModel first, FaqModel second) {
							return first.getCreateDateTime().compareTo(second.getCreateDateTime());
						}
					}	
			);

			questionList.setPageItems(8);
		}

		// set variables
		int pages = questionList.getTotalPages();
		int pn = StringUtils.isEmpty(pageno) ? 1 : Math.max(1,
				Math.min(Integer.parseInt(pageno), pages));

		request.setAttribute("pageno", pn);
		request.setAttribute("total", questionList.getTotalItems());
		request.setAttribute("totalPages", pages);
		List<FaqModel> pl = questionList.getPage(pn, false);
		request.setAttribute("questions", pl);

//		container.setQuestionListQuery(new StringBuilder("?pageno=").append(pn).toString());
	}
	
}
