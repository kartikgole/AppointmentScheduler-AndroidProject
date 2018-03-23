package expert;

import base.cUser;
import info.cUserDB;

public class cLoginMgr {
	
	public cLoginMgr(){
		
	}
	
	public boolean mVerifyPassword (String rNetId, String rPassword) {
		cUserDB lUserDB = new cUserDB();
		String lPassword = lUserDB.mGetPassword(rNetId);
		
		return lPassword.equals(rPassword);
	}
	
	public cUser mUserdetails (String rNetId) {
		cUserDB lUserDB = new cUserDB();
		cUser lUser = lUserDB.mGetUser(rNetId);
		return lUser;
	}
}
