package controllers;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import info.cJsonConvertor;
import info.cUserDB;

import org.json.simple.JSONObject;

import base.cUser;
import expert.cLoginMgr;

@WebServlet("/sLogin")
public class sLoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public sLoginController() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cJsonConvertor lJsonConvertor=new cJsonConvertor();
		JSONObject objl=lJsonConvertor.mHttpToJson(request);
		
		String lNetId = objl.get("username").toString();
		String lPassword = objl.get("password").toString();
		
		
		
		
		cLoginMgr lLoginMgr = new cLoginMgr();
		JSONObject obj = new JSONObject();
		boolean checklogin=lLoginMgr.mVerifyPassword(lNetId, lPassword);
		obj.put("login",checklogin );
		if(checklogin){
			cUserDB lUserDB=new cUserDB();
			cUser userDetails=lUserDB.mGetUser(lNetId);
			obj.put("user_name",userDetails.gName );
			obj.put("user_dept",userDetails.gDepartmentId );
			obj.put("user_type",userDetails.gUserType );
			obj.put("user_email",userDetails.gEmail );
			obj.put("user_deptname",userDetails.gDepartmentName );
		}
		
		
		StringWriter out = new StringWriter();
		obj.writeJSONString(out);
		response.getWriter().append(out.toString());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
