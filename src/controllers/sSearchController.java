package controllers;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import expert.cSearchMgr;
import info.cJsonConvertor;

@WebServlet("/sSearchController")
public class sSearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public sSearchController() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cJsonConvertor lJsonConvertor = new cJsonConvertor();
		JSONObject lObj = lJsonConvertor.mHttpToJson(request);
		
		String lRequest = lObj.get("request").toString();
		
		JSONObject messagesObj = new JSONObject();
		cSearchMgr lSearchMgr = new cSearchMgr();
		
		switch (lRequest) {
		case "search":
			String keyword = lObj.get("keyword").toString();
			messagesObj = lSearchMgr.mSearchProf(keyword);
			break;
		case "get":
			String lNetId = lObj.get("netId").toString();
			messagesObj = lSearchMgr.mGetProf(lNetId);
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
