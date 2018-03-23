package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import base.cAppointment;
import expert.cApptMgr;
import expert.cDashboardMgr;

import info.cAppointmentDB;
import info.cJsonConvertor;


@WebServlet("/sDashboardController")
public class sDashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public sDashboardController() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cJsonConvertor lJsonConvertor=new cJsonConvertor();
		JSONObject lJsonRequest=lJsonConvertor.mHttpToJson(request);
		
		String lType = lJsonRequest.get("get").toString();//request.getParameter("get");//
		
		String lNetID = lJsonRequest.get("netid").toString();//request.getParameter("netid");//
		
		JSONObject JSONObject=new JSONObject();
		if(lType.trim().equals("summarytab")){
			
			cDashboardMgr lDashboardMgr=new cDashboardMgr();
			JSONObject=lDashboardMgr.mGetSummaryTabContent(lNetID);
			
			StringWriter out = new StringWriter();
			JSONObject.writeJSONString(out);
			response.getWriter().append(out.toString());
		}
		else if(lType.trim().equals("allappointments")){
			JSONArray lJsonAppointmentList = new JSONArray();
			cAppointmentDB lAppointmentDB=new cAppointmentDB();
			cApptMgr lApptMgr=new cApptMgr();
			ArrayList<cAppointment> lAppointmentList=lApptMgr.mGetAppointmentsList(lNetID);
			
			for(int i=0; i<lAppointmentList.size();i++){
				JSONObject lJsonAppointment=lJsonAppointment=lAppointmentDB.mCovertToJsonFormat(lAppointmentList.get(i));
				lJsonAppointmentList.add(lJsonAppointment);
			}
			StringWriter out = new StringWriter();
			lJsonAppointmentList.writeJSONString(out);
			
			response.getWriter().append(out.toString());
		}
		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
