package expert;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import base.cAppointment;
import base.cCourse;
import base.cSlot;
import base.cUser;
import info.cAppointmentDB;
import info.cCourseDB;
import info.cUserDB;

public class cDashboardMgr {
	public JSONObject mGetSummaryTabContent (String rNetId) {
		
		JSONObject lDashboardObj = new JSONObject();
		JSONArray lJsonAppointmentList = new JSONArray();
		JSONArray lCourseData = new JSONArray();
		cApptMgr lApptMgr = new cApptMgr();
		
		cAppointmentDB lAppointmentDB=new cAppointmentDB();
		ArrayList<cAppointment> lAppointmentList=new ArrayList<cAppointment>();
		cLoginMgr lLoginMgr=new cLoginMgr(); 
		cUser lUserDetails=lLoginMgr.mUserdetails(rNetId);
		//System.out.println(lUserDetails.gUserType);
		if(lUserDetails.gUserType==1){
			lAppointmentList=lApptMgr.mGetAppointmentsList(rNetId);
		}
		else{
			lAppointmentList=mGetAppointmentSlotList(rNetId);
		}
		
		
		
		int i=0;
		while(i<lAppointmentList.size()){
			JSONObject lJsonAppointment=lJsonAppointment=lAppointmentDB.mCovertToJsonFormat(lAppointmentList.get(i));
			lJsonAppointmentList.add(lJsonAppointment);
			i++;
		}
		
		lDashboardObj.put("apptData", lJsonAppointmentList);
		
		cCourseDB lCourseDB = new cCourseDB();
		ArrayList<cCourse> lCourseArray = lCourseDB.mGetCourses(rNetId);
		
		for (int j=0; j<lCourseArray.size(); j++) {
			JSONObject lCourseObj = new JSONObject();
			cUserDB lUserDB=new cUserDB();
			cUser luser=lUserDB.mGetUser(lCourseArray.get(j).gProfessorNetid);
			lCourseObj.put("courseId", ""+lCourseArray.get(j).gCourseId+"");
			lCourseObj.put("professorid", ""+lCourseArray.get(j).gProfessorNetid+"");
			lCourseObj.put("professorname", ""+luser.gName+"");
			lCourseObj.put("coursename",""+lCourseArray.get(j).gCourseName+"");
			lCourseObj.put("courseno", ""+lCourseArray.get(j).gCourseNo+"");
			lCourseObj.put("sectionno", ""+lCourseArray.get(j).gSectionNo+"");
			lCourseData.add(lCourseObj);
		}
		
		lDashboardObj.put("courseData", lCourseData);
		return lDashboardObj;
	}
	
	public ArrayList<cAppointment> mGetAppointmentSlotList (String rNetId) {
		
		cSlotMgr lSlotMgr=new cSlotMgr(rNetId);
		ArrayList<cSlot> lSlotList=lSlotMgr.mGetSlotList();
		ArrayList<cAppointment> List=new ArrayList<cAppointment>();
		//System.out.println(lSlotList.size());
		for(int i=0;i<lSlotList.size();i++){
			cSlot lSlot=lSlotList.get(i);
			
			Calendar lStartDate = Calendar.getInstance();
			lStartDate.setTime(lSlot.gStartDateTime);
			Calendar lEndDate = Calendar.getInstance();
			lEndDate.setTime(lSlot.gEndDateTime);

			Calendar lNextDate = Calendar.getInstance();
			Calendar lTempDate = Calendar.getInstance();
			lTempDate.setTime(lSlot.gSlotStartTime);

			if (lSlot.gSlotType == 1) {
				if (lNextDate.get(Calendar.DAY_OF_WEEK) == lSlot.gSlotFrequency) {
					int TodayTimeinMin = lNextDate.get(Calendar.HOUR_OF_DAY) * 60 + lNextDate.get(Calendar.MINUTE);
					int SlotTimeinMin = lTempDate.get(Calendar.HOUR_OF_DAY) * 60 + lTempDate.get(Calendar.MINUTE);
					
					lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
					lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
				} else if (lNextDate.get(Calendar.DAY_OF_WEEK) > lSlot.gSlotFrequency) {
					lNextDate.set(Calendar.DAY_OF_WEEK, lSlot.gSlotFrequency);
					lNextDate.add(Calendar.WEEK_OF_YEAR, 1);
					lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
					lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
				} else {
					lNextDate.set(Calendar.DAY_OF_WEEK, lSlot.gSlotFrequency);
					lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
					lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
				}
			} else {
				lNextDate.setTime(lSlot.gStartDateTime);
				lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
				lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
			}
			
			cAppointment appointment=new cAppointment();
			appointment.gAppointmentId=lSlot.OfficeSlotId;
			appointment.gStatusId=0;
			appointment.gcommentId=0;
			appointment.gAppointmentDate=lNextDate.getTime();
			appointment.gCurrentStatusId=0;
			appointment.gAttendees.put("student", rNetId);
			appointment.gAttendees.put("professor", rNetId);
			appointment.gSlot=lSlot;
			
			List.add(appointment);
		}
		
		return List;
	
	}
	
	/*public JSONObject mGetDeptTabContent (String rNetId) {
		JSONObject lDashboardObj = new JSONObject();
		JSONArray lProfData = new JSONArray();
		cSearchDB lSearchDB = new cSearchDB();
		ArrayList<cUser> lUsers = lSearchDB.mGetCourses(rNetId);
		
		for (int j=0; j<lUsers.size(); j++) {
			JSONObject lProfObj = new JSONObject();
			lProfObj.put("netId", lUsers.get(j).gNetId);
			lProfObj.put("name", lUsers.get(j).gName);
			lProfObj.put("gEmail", lUsers.get(j).gEmail);
			lProfData.add(lProfObj);
		}
		
		lDashboardObj.put("profData", lProfData);
		return lDashboardObj;
	}*/
}
