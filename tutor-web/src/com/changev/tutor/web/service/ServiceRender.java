/**
 * 
 */
package com.changev.tutor.web.service;

import com.google.gson.Gson;

/**
 * @author zhaoqing
 *
 */
public class ServiceRender {

	
	public static final String renderJson(Object o){
		Gson gson = new Gson();
		return gson.toJson(o);
	}
}
