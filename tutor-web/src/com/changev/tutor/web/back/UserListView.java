/**
 * 
 */
package com.changev.tutor.web.back;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.View;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * @author zhaoqing
 *
 */
public class UserListView implements View {

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		ListIterator<UserModel> all = getAll();
		List<UserModel> u10 = new ArrayList<UserModel>();
		int count = 0;
		while(all.hasNext()){
			count++;
			if(u10.size() < 10){
				u10.add(all.next());
			}
		}
		request.setAttribute("users", u10);
		request.setAttribute("count", count);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub

	}

	private ListIterator<UserModel> getAll(){
		
		ObjectSet<UserModel> questions = Tutor.getCurrentContainer().query(new Predicate<UserModel>() {
			@Override
			public boolean match(UserModel candidate) {
				return true;
			}
		});
		ListIterator<UserModel> r = questions.listIterator();
		
		return r;
	}
}
