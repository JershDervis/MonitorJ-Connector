/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.monitorj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Josh
 */
public class WebUtil {
    
    	/**
	 * Outputs the source code of a website
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> getUrlSource(String url) throws IOException {
		ArrayList<String> returnList = new ArrayList<String>();
		returnList.clear();
		URL site = new URL(url);
		HttpURLConnection yc = (HttpURLConnection) site.openConnection();
		yc.addRequestProperty("User-Agent", "MonitorJ/0.01"); 
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			returnList.add(inputLine);
		}
		in.close();

		return returnList;
	}
    
}
