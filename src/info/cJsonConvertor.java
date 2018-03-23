package info;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class cJsonConvertor {

	public JSONObject mHttpToJson(HttpServletRequest request){
		 StringBuffer jb = new StringBuffer();
		  String line = null;
		  
		  JSONObject jsonObj =new JSONObject();
		  JSONParser parser = new JSONParser();
		  
		  try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }

		  try {
			  
			 
		    jsonObj = (JSONObject) parser.parse(jb.toString());
		  } catch (Exception e) {
		    // crash and burn
			  System.out.println(e);
		  }
		  return jsonObj;
	}

}
