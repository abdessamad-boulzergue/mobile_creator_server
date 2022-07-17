package com.creator.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {
	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	 public static Map<String, Object> fromJsonHashMap(String name, String inHashMap)  {
		 Map<String, Object> returned = new HashMap<>();   
		 try {
		    	JSONObject obj = new JSONObject(inHashMap);
		    	if(obj.has(name)) {
				    JSONObject map = obj.getJSONObject(name);
				    returned= fromJsonHashMap(map);
		    	}
			} catch (JSONException e) {
				JsonUtils.logger.error(e.toString());
			}
			return returned;
		  }
	
	  public static Map<String, Object> fromJsonHashMap(JSONObject map) throws JSONException {
	    HashMap<String, Object> returned = new HashMap<>();

	    Iterator<String> it = map.keys();
	    while (it.hasNext()) {
	      String curK = it.next();
	      Object curVal = map.get(curK);
	      returned.put(curK, curVal);
	    }
	    return returned;
	  }

}
