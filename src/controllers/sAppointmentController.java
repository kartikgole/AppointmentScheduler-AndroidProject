package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import base.cAppointment;
import base.cSlot;
import base.cUser;
import expert.cApptMgr;
import expert.cLoginMgr;
import expert.cSlotMgr;
import info.cAppointmentDB;
import info.cCommentDB;
import info.cJsonConvertor;

/**
 * Servlet implementation class sAppointmentController
 */
@WebServlet("/sAppointmentController")
public class sAppointmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	public sAppointmentController() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cJsonConvertor lJsonConvertor=new cJsonConvertor();
		JSONObject lJsonRequest=lJsonConvertor.mHttpToJson(request);
		
		String lType = lJsonRequest.get("get").toString();//request.getParameter("get");//
		String lNetID = lJsonRequest.get("snetid").toString();//request.getParameter("netid");//
		
		//System.out.println("done1");
		if(lType.trim().equals("book")){
		
			try{
				String  lDate= lJsonRequest.get("bookingdate").toString();
				SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				
	        	Date lBookingDate= formatter.parse(lDate);
	       		int lSlotID = (Integer.parseInt(lJsonRequest.get("slotid").toString()));
				
				cApptMgr lApptMgr=new cApptMgr();
				
				boolean create=lApptMgr.mCreateAppointment(lNetID, lSlotID, lBookingDate);
				
				JSONObject lJSONUserdetails=new JSONObject();
				
				if(create){
					lJSONUserdetails.put("bookingconfirm", "yes");
				}else{
					lJSONUserdetails.put("bookingconfirm", "no");
				}
				
				StringWriter out = new StringWriter();
				lJSONUserdetails.writeJSONString(out);
				
				response.getWriter().append(out.toString());
			 }catch(Exception Ex){
				 System.out.println(Ex);
			 }
		}else if(lType.trim().equals("requestbooking")){
		
			try{
				String  rRequestorId= lJsonRequest.get("snetid").toString();
				String  rSenderId= lJsonRequest.get("professorid").toString();
				String  rContent= lJsonRequest.get("comment").toString();
				
	       		cCommentDB lCommentDB=new cCommentDB();
				
				boolean create=lCommentDB.mCreateMessage(rRequestorId, rSenderId, rContent);
				
				JSONObject lJSONUserdetails=new JSONObject();
				
				if(create){
					lJSONUserdetails.put("bookingconfirm", "yes");
				}else{
					lJSONUserdetails.put("bookingconfirm", "no");
				}
				
				StringWriter out = new StringWriter();
				lJSONUserdetails.writeJSONString(out);
				
				response.getWriter().append(out.toString());
			 }catch(Exception Ex){
				 System.out.println(Ex);
			 }
		} else if (lType.trim().equals("pastAppts")) {
			try {
				cApptMgr lApptMgr = new cApptMgr();
				JSONObject lPastAppts = lApptMgr.mGetApptHistory(lNetID);
				StringWriter out = new StringWriter();
				lPastAppts.writeJSONString(out);
				response.getWriter().append(out.toString());
			} catch (Exception Ex) {
				System.out.println(Ex);
			}
		}
		else if(lType.trim().equals("appointmentlist")){
			
			try{
				JSONArray lJsonAppointmentList = new JSONArray();
				
				String  rSlotId= lJsonRequest.get("slotid").toString();
				String  rAppointmentdate= lJsonRequest.get("date").toString();
				
				SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
				
	        	Date lApptDate= formatter.parse(rAppointmentdate);
	        	
				JSONObject lDashboardObj = new JSONObject();
				cApptMgr lApptMgr=new cApptMgr();;
				cAppointmentDB lAppointmentDB=new cAppointmentDB();
				//System.out.println(Integer.parseInt(rSlotId)+"--"+ lApptDate);
				ArrayList<cAppointment> lAppointmentList=lApptMgr.mGetAppointmentSlotList(Integer.parseInt(rSlotId), lApptDate);
				
				int i=0;
				while(i<lAppointmentList.size()){
					JSONObject lJsonAppointment=lJsonAppointment=lAppointmentDB.mCovertToJsonFormat(lAppointmentList.get(i));
					lJsonAppointmentList.add(lJsonAppointment);
					i++;
				}
				
				StringWriter out = new StringWriter();
				lJsonAppointmentList.writeJSONString(out);
				
				response.getWriter().append(out.toString());
			
			 }catch(Exception Ex){
				 System.out.println(Ex);
			 }
		}
		else if(lType.trim().equals("liveupdate")){
			
			try{
				JSONObject lJsonAppointmentList = new JSONObject();
				
				String  rSlotId= lJsonRequest.get("slotid").toString();
				String  rAppointmentdate= lJsonRequest.get("date").toString();
				
				SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
				Date lApptDate= formatter.parse(rAppointmentdate);
	        	cApptMgr lApptMgr=new cApptMgr();;
				
			
				boolean lUpdate=lApptMgr.mUpdateAppointmentLiveStatus(rSlotId, lApptDate);
				lJsonAppointmentList.put("updated", ""+lUpdate);
				StringWriter out = new StringWriter();
				lJsonAppointmentList.writeJSONString(out);
				
				response.getWriter().append(out.toString());
			
			 }catch(Exception Ex){
				 System.out.println(Ex);
			 }
		}
		else if(lType.trim().equals("cancelbooking")){
			
			try{
				String  rRequestorId= lJsonRequest.get("snetid").toString();
				String  rAppointmentId= lJsonRequest.get("appointmentid").toString();//appointment-id
				String  rContent= lJsonRequest.get("comment").toString();
				
				cApptMgr lApptMgr=new cApptMgr();
				
				boolean create=lApptMgr.mCancelAppointment(Integer.parseInt(rAppointmentId), rContent);
				
				JSONObject lJSONUserdetails=new JSONObject();
				
				if(create){
					lJSONUserdetails.put("cancelconfirm", "yes");
				}else{
					lJSONUserdetails.put("cancelconfirm", "no");
				}
				
				StringWriter out = new StringWriter();
				lJSONUserdetails.writeJSONString(out);
				
				response.getWriter().append(out.toString());
			 }catch(Exception Ex){
				 System.out.println(Ex);
			 }
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
