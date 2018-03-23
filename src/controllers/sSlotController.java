package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
import expert.cDashboardMgr;
import expert.cLoginMgr;
import expert.cSlotMgr;
import info.cAppointmentDB;
import info.cJsonConvertor;
import info.cSlotDB;

@WebServlet("/sSlot")
public class sSlotController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public sSlotController() {
		super();
	}
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*cSlot lCSlotObj=new cSlot();
		String lRequest = request.getParameter("request");
		String lNetId = request.getParameter("netId");
		lCSlotObj.gStartDateTime = (Date) request.getAttribute("slotStartDateTime");
		lCSlotObj.gEndDateTime = (Date) request.getAttribute("slotEndDateTime");
		lCSlotObj.gSlotStartTime = (Time) request.getAttribute("slotStartTime");
		lCSlotObj.gSlotEndTime = (Time) request.getAttribute("slotEndTime");
		lCSlotObj.gSlotType = (int) request.getAttribute("slotType");
		lCSlotObj.gSlotFrequency = (int) request.getAttribute("frequency");
		lCSlotObj.gBuildingId=request.getParameter("buildingId");
		lCSlotObj.gRoomNo = request.getParameter("roomNo");
		
		boolean slot = false;
		JSONObject lSlotObj = new JSONObject();
		cSlotMgr lSlotMgr =new cSlotMgr(lNetId);
		
		if (lRequest != null && lNetId != null && lCSlotObj.gStartDateTime != null
				&& lCSlotObj.gEndDateTime != null  && lCSlotObj.gEndDateTime != null
				&& lCSlotObj.gSlotStartTime != null &&  lCSlotObj.gSlotEndTime != null
				&& lCSlotObj.gSlotType!='0' && lCSlotObj.gSlotFrequency != '0'
				&& lCSlotObj.gBuildingId != null && lCSlotObj.gRoomNo != null) {
			
			if (lRequest.equals("1")) {
				slot = lSlotMgr.mCreateSlot(lCSlotObj);
				lSlotObj.put("createSlot", slot);
			} else if (lRequest.equals("2")) {
				slot = lSlotMgr.mUpdateSlot(lCSlotObj);
				lSlotObj.put("updateSlot", slot);			
			}
			else if (lRequest.equals("3")) {
				slot = lSlotMgr.mCancelSlot(lCSlotObj);
				lSlotObj.put("cancelSlot", slot);
			}
		}
		
		StringWriter lOut = new StringWriter();
		lSlotObj.writeJSONString(lOut);	
		response.getWriter().append(lOut.toString());*/
		
		cJsonConvertor lJsonConvertor=new cJsonConvertor();
		JSONObject lJsonRequest=lJsonConvertor.mHttpToJson(request);
		
		String lType = lJsonRequest.get("get").toString();//request.getParameter("get");//
		String lNetID = lJsonRequest.get("netid").toString();//request.getParameter("netid");//
		String lSlotDetails=lJsonRequest.get("slotDetails").toString();
		
		if(lType.trim().equals("slotlist")){
			
			cSlotMgr lSlotMgr=new cSlotMgr(lNetID);
			ArrayList<cSlot> lSlotList=lSlotMgr.mGetSlotList();
			boolean lGetCurrentDateList=lJsonRequest.get("currdatelist").toString().trim().equals("yes")?true:false;
			JSONArray lJsonSlotArray=lSlotMgr.mCovertToJsonFormat(lSlotList,lGetCurrentDateList);
			
			cLoginMgr lLoginMgr=new cLoginMgr(); 
			cUser lUserDetails=lLoginMgr.mUserdetails(lNetID);
			
			JSONObject lJSONUserdetails=new JSONObject();
			lJSONUserdetails.put("professorname", ""+lUserDetails.gName+"");
			lJSONUserdetails.put("depatmentname", ""+lUserDetails.gDepartmentName+"");
			lJSONUserdetails.put("email", ""+lUserDetails.gEmail+"");
			lJSONUserdetails.put("slots", lJsonSlotArray);
			
			StringWriter out = new StringWriter();
			lJSONUserdetails.writeJSONString(out);
			response.getWriter().append(out.toString());
		}
		
		else if(lType.trim().equals("create")) {
			System.out.println("a");
			cSlotMgr lSlotMgr=new cSlotMgr(lNetID);
			cSlot oSlot=new cSlot();
			oSlot=lSlotMgr.mSlotDbFormat(lSlotDetails);
			JSONObject obj=new JSONObject();
			obj.put("created", lSlotMgr.mCreateSlot(oSlot));
			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			response.getWriter().append(out.toString());
		}
		else if(lType.trim().equals("cancel")) {
			cSlotMgr lSlotMgr=new cSlotMgr(lNetID);
			JSONObject obj=new JSONObject();
			//obj.put("cancel", lSlotMgr.mCancelSlot(Integer.parseInt(lslotId)));
			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			response.getWriter().append(out.toString());
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
