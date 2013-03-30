package com.changev.tutor.web.manage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.FaqModel;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.util.PageList;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.db4o.query.Predicate;

public class FaqListView implements View {

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

//		SessionContainer container = SessionContainer.get(request);
		PageList<FaqModel> questionList = null;
		if (questionList == null || StringUtils.isEmpty(pageno)) {
			//pageno = "1";
			questionList = null;

			// get answered questions
			questionList = new PageList<FaqModel>(
					new Predicate<FaqModel>() {
						@Override
						public boolean match(FaqModel candidate) {
							return true;
						}
					});

			questionList.setPageItems(20);
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