package controllers;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import expert.cCommentMgr;
import info.cJsonConvertor;

@WebServlet("/sMessengerController")
public class sMessengerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public sMessengerController() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cJsonConvertor lJsonConvertor = new cJsonConvertor();
		JSONObject lObj = lJsonConvertor.mHttpToJson(request);
		
		String lRequest = lObj.get("request").toString();
		String lNetId = lObj.get("netId").toString();
		
		JSONObject messagesObj = new JSONObject();
		cCommentMgr lCommentMgr = new cCommentMgr();
		
		switch (lRequest) {
		case "1":
			String lRequestId = lObj.get("requestId").toString();
			String lMessage = lObj.get("message").toString();
			messagesObj = lCommentMgr.mReplyAppointment(lRequestId, lNetId, lMessage);
			break;
		case "2":
			messagesObj = lCommentMgr.mGetChatData(lNetId);
			break;
		default:
			break;
		}
		
		StringWriter out2 = new StringWriter();
		messagesObj.writeJSONString(out2);
		response.getWriter().append(out2.toString());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
