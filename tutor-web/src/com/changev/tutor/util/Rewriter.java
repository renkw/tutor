/**
 * 
 */
package com.changev.tutor.util;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.web.SessionContainer;


/**
 * @author zhaoqing
 *
 */
public class Rewriter {

	
	/**
	 * <p>
	 * rewrite 取请求的id
	 * </p>
	 * 
	 * @param path
	 * @return
	 */
	public static final String getViewId(String path) {
		String id = "";
		int start = path.charAt(0) == '/' ? 1 : 0;
		int end = path.lastIndexOf('.');
		if (end == -1)
			end = path.length();
		id = path.substring(start, end);
		//rewrite
		if(id.contains("_")){
			String[] r_name = id.split("\\_");
			id = r_name[1];
		}
				
		return id;
	}
	
	public static final String str2Asii(String source){
		StringBuffer buffer = new StringBuffer();
		for(char c : source.toCharArray()){
			buffer.append((int)c);
			
		}
		
		return buffer.toString();
	}
	
	public static final long userHash(String userEmail){
		long hashId;
		if(StringUtils.isEmpty(userEmail)){
			hashId = "admin@localhost".hashCode();
		}
		else{
			hashId = userEmail.hashCode();
		}
		return hashId;	
	}
}
