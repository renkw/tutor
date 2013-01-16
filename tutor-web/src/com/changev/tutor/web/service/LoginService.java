/**
 * 
 */
package com.changev.tutor.web.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;

/**
 * <p>用户登陆验证服务</p>
 * 
 * @author zhaoqing
 *
 */
public class LoginService implements Service<Map> {

	
	/**
	 * @return 登陆成功返回succ	失败返回fail
	 */
	@Override
	public String run(UserModel user, Map input) throws Throwable {
		String result = "登陆成功";
//		if(user == null){
//			result = "用户不存在";
//		}
		String password = String.valueOf(input.get("password"));
		//XXX need hash?
		if(!StringUtils.equals(password, user.getPassword())){
			result = "密码不正确";
		}
		return result;
	}

}
