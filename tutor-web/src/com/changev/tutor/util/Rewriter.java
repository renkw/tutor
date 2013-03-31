/**
 * 
 */
package com.changev.tutor.util;

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
}
