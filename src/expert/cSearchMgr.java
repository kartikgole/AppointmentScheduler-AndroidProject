package expert;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import base.cUser;
import info.cSearchDB;

public class cSearchMgr {
	@SuppressWarnings("unchecked")
	public JSONObject mGetProf(String rNetId) {
		JSONObject lProfsObj = new JSONObject();
		
		try {
			JSONArray lProfArr = new JSONArray();
			cSearchDB lSearchDB = new cSearchDB();
			ArrayList<cUser> lProfs = lSearchDB.mGetProf(rNetId);
			
			for (int i=0; i<lProfs.size(); i++) {
				JSONObject lProfObj = new JSONObject();
				lProfObj.put("pNetId",lProfs.get(i).gNetId);
				lProfObj.put("name", lProfs.get(i).gName);
				lProfObj.put("email", lProfs.get(i).gEmail);
				lProfArr.add(lProfObj);
			}
			
			lProfsObj.put("professors", lProfArr);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return lProfsObj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject mSearchProf(String rNetId) {
		JSONObject lProfsObj = new JSONObject();
		
		try {
			JSONArray lProfArr = new JSONArray();
			cSearchDB lSearchDB = new cSearchDB();
			ArrayList<cUser> lProfs = lSearchDB.mSearchProf(rNetId);
			
			for (int i=0; i<lProfs.size(); i++) {
				System.out.println(lProfs.get(i).gName);
				JSONObject lProfObj = new JSONObject();
				lProfObj.put("pNetId",lProfs.get(i).gNetId);
				lProfObj.put("name", lProfs.get(i).gName);
				//lProfObj.put("email", lProfs.get(i).gEmail);
				//lProfObj.put("department", lProfs.get(i).gDepartmentName);
				lProfArr.add(lProfObj);
			}
			
			lProfsObj.put("professors", lProfArr);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return lProfsObj;
	}
}
