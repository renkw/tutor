package com.changev.tutor.web.manage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.model.PictureModel;
import com.changev.tutor.util.PageList;
import com.changev.tutor.util.Rewriter;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.db4o.query.Predicate;

public class AdvEditorListView implements View {

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

	private void listFaq(final HttpServletRequest request) {

		// parameters
		String pageno = request.getParameter("pageno");

//		SessionContainer container = SessionContainer.get(request);
		PageList<PictureModel> questionList = null;
		if (questionList == null || StringUtils.isEmpty(pageno)) {
			//pageno = "1";
			questionList = null;

			// get answered questions
			questionList = new PageList<PictureModel>(
					new Predicate<PictureModel>() {
						long hashId = Rewriter.userHash(SessionContainer.get(request).getLoginUserId_str());
						@Override
						public boolean match(PictureModel candidate) {
							return candidate.getOwener() == hashId;
						}
					});

			questionList.setPageItems(8);
		}

		// set variables
		int pages = questionList.getTotalPages();
		int pn = StringUtils.isEmpty(pageno) ? 1 : Math.max(1,
				Math.min(Integer.parseInt(pageno), pages));

		request.setAttribute("pageno", pn);
		request.setAttribute("total", questionList.getTotalItems());
		request.setAttribute("totalPages", pages);
		List<PictureModel> pl = questionList.getPage(pn, false);
		request.setAttribute("questions", pl);

//		container.setQuestionListQuery(new StringBuilder("?pageno=").append(pn).toString());
	}

}