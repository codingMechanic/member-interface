package de.lsvn.util;

import javax.servlet.http.HttpServletRequest;

public class LinkRootHelper {

	public static String getLinkRoot(HttpServletRequest request){
		
		String linkRoot = "";
        int serverPort = request.getServerPort();
		
		// if port is default or 0, just use the default port.
	     String appUrl = request.getScheme() + "://" + request.getServerName();

	     // if it's not the default port, append the port to your url
	     if(serverPort != 80 || serverPort != 443 || serverPort != 0) {
	         appUrl += ":" + new Integer(serverPort).toString();
	     }
	     
	     linkRoot = appUrl + request.getContextPath();
	     
	     return linkRoot;
	}
}
