package expert;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import base.cMessage;
import base.cUser;
import info.cCommentDB;
import info.cUserDB;

public class cCommentMgr {
	@SuppressWarnings("unchecked")
	public JSONObject mGetChatData(String rNetId) {
		JSONObject lCommentObj = new JSONObject();
		
		try {
			JSONArray lMessageArr = new JSONArray();
			cCommentDB lCommentDB = new cCommentDB();
			ArrayList<cMessage> lMessages = lCommentDB.mGetChatData(rNetId);
			
			for (int i=0; i<lMessages.size(); i++) {
				JSONObject lMessageObj = new JSONObject();
				lMessageObj.put("requestId", lMessages.get(i).gRequestId);
				lMessageObj.put("sNetId", lMessages.get(i).gStudentId);
				lMessageObj.put("pNetId", lMessages.get(i).gProfId);
				lMessageObj.put("comment", lMessages.get(i).gComment);
				lMessageObj.put("senderNetId", lMessages.get(i).gSenderId);
				lMessageObj.put("sentDate", lMessages.get(i).gSentDate.toString());
				lMessageObj.put("sentTime", lMessages.get(i).gSentTime.toString());
				
				cUserDB lUserDB=new cUserDB();
				cUser luserdetails=new cUser();
				
				luserdetails=lUserDB.mGetUser(lMessages.get(i).gProfId);
				lMessageObj.put("proffname", luserdetails.gName);
				luserdetails=lUserDB.mGetUser(lMessages.get(i).gStudentId);
				lMessageObj.put("studentname", luserdetails.gName);
				
				luserdetails=lUserDB.mGetUser(lMessages.get(i).gSenderId);
				lMessageObj.put("sendername", luserdetails.gName);
				
				lMessageArr.add(lMessageObj);
			}
			
			lCommentObj.put("messages", lMessageArr);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return lCommentObj;
	}
	
	public boolean mRequestAppointment( String requesterNetId, String responderNetid, String message)
	{
		String lRequesterNetId=requesterNetId;
		String lResponderNetid=responderNetid;
		String lmessage=message;
		boolean check = false;
		try {
			cCommentDB lOComment = new cCommentDB();
		     check = lOComment.mSendMessage(lRequesterNetId,lResponderNetid,lmessage);
		} catch (Exception e) {
			System.err.println(e);
		}
		return check;
	}
	
	public boolean mCreateAppointment( String requesterNetId, String responderNetid, String message)
	{
		String lRequesterNetId=requesterNetId;
		String lResponderNetid=responderNetid;
		String lmessage=message;
		boolean check = false;
		try {
			cCommentDB lOComment = new cCommentDB();
		     check = lOComment.mCreateMessage(lRequesterNetId,lResponderNetid,lmessage);
		} catch (Exception e) {
			System.err.println(e);
		}
		return check;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject mReplyAppointment(String rRequestId, String rSenderId, String rMessage) {
		JSONObject lReplyObj = new JSONObject();
		boolean check = false;
		
		try {
			cCommentDB lOComment = new cCommentDB();
			check = lOComment.mSendMessage(rRequestId, rSenderId, rMessage);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		lReplyObj.put("reply", check);
		return lReplyObj;
	}
}
