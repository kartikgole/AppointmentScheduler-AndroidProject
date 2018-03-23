package expert;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import base.cAppointment;
import base.cSlot;
import info.cAppointmentDB;
import info.cSlotDB;
import uta.dbconnector.cDBConnection;

public class cApptMgr {
	@SuppressWarnings("unchecked")
	public JSONObject mGetApptHistory (String rNetId) {
		JSONObject lApptsObj = new JSONObject();
		
		try {
			JSONArray lApptArr = new JSONArray();
			cAppointmentDB lApptDB = new cAppointmentDB();
			ArrayList<cAppointment> lAppts = lApptDB.mGetApptHistory(rNetId);
			
			for (int i=0; i<lAppts.size(); i++) {
				JSONObject lApptObj = new JSONObject();
				lApptObj.put("sNetId", lAppts.get(i).gAttendees.get("student"));
				lApptObj.put("pNetId", lAppts.get(i).gAttendees.get("professor"));
				lApptObj.put("statusId", lAppts.get(i).gStatusId);
				lApptObj.put("apptDate", lAppts.get(i).gAppointmentDate.toString());
				lApptArr.add(lApptObj);
			}
			
			lApptsObj.put("pastAppts", lApptArr);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return lApptsObj;
	}
	
	public ArrayList<cAppointment> mGetAppointmentsList(String rNetId){
		
		cAppointmentDB lAppointment=new cAppointmentDB();
		ArrayList<cAppointment> lAppointmentList=lAppointment.mGetExistingAppointments(rNetId);
		return lAppointmentList;
	}
	
	public boolean mCreateAppointment(String rNetIds,int rSlotID,Date rDate){
		//mCreateAppointment();
		cAppointment lAppointment=new cAppointment();
		lAppointment.gAppointmentId=0;
		lAppointment.gAttendees.put("student", rNetIds) ; //professor and student
		lAppointment.gSlot=mGetSlot(rSlotID);
		lAppointment.gAppointmentDate=rDate;
		
		cAppointmentDB lAppointmentDB=new cAppointmentDB();
		return lAppointmentDB.mCreateAppointment(lAppointment);
	}

	public cSlot mGetSlot(int rSlotID){
		
		cDBConnection lDBConnection = new cDBConnection();
		cSlot lSlot=new cSlot();
		try {
			
			String lQuery=" SELECT * "+
						" FROM uta_appointment_app.officeslots "+
						" where slotid='"+rSlotID+"'";
			
			ResultSet lres = lDBConnection.aStatement.executeQuery( lQuery);
		
			if(lres.next()) {
			
				
				lSlot.OfficeSlotId =lres.getInt("slotId");
				lSlot.gSlotType =lres.getInt("slotType");
				lSlot.gStartDateTime =lres.getDate("slotStartDateTime");
				lSlot.gEndDateTime =lres.getDate("slotEndDateTime");
				lSlot.gSlotFrequency =lres.getInt("slotFrequency");
				lSlot.gBuildingId =lres.getString("buildingId");;
				lSlot.gRoomNo =lres.getString("roomNo");
				
			
			
			}
			
			lres.close();

		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		return lSlot;
	}


	public boolean mCancelAppointment(int rAppointmentId, String rComment){
		cAppointment lAppointment=new cAppointment();
		cAppointmentDB lAppointmentDB=new cAppointmentDB();
		lAppointment=lAppointmentDB.mGetAppointmentDetails(rAppointmentId);
		boolean lReturn=lAppointmentDB.mCancelAppointment(lAppointment, rComment);
		
		return lReturn;
	}
	
	public ArrayList<cAppointment> mGetAppointmentSlotList(int Slotid,Date date ){
		cSlotDB lSlotDB=new cSlotDB();
		ArrayList<cAppointment> AppointmentList=lSlotDB.mGetAppointmentDetails(Slotid, date);
		
		return AppointmentList;
	}
	
	public boolean mUpdateAppointmentLiveStatus(String Slotid, Date ApptDate){
		cAppointmentDB lAppointmentDB=new cAppointmentDB();
		boolean lreturn=lAppointmentDB.mUpdateAppointmentLiveStatus(Slotid, ApptDate);
		
		return lreturn;
	}
	
}
