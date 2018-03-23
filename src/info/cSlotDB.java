package info;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;

import base.cSlot;
import base.cAppointment;
import uta.dbconnector.cDBConnection;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class cSlotDB {

	public cSlotDB() {
		
	}

	public cSlot mGetSlotDetails(Time slotStartTime) {
		cSlot oSlotDB = new cSlot();
		cDBConnection lDBConnection = new cDBConnection();
		try {
			ResultSet lres = lDBConnection.aStatement
					.executeQuery("Select * from officeslots where slotStartTime='" + slotStartTime + "'");
			if (lres.next()) {
				oSlotDB.OfficeSlotId = (lres.getInt("slotId"));
				oSlotDB.gStartDateTime = (lres.getDate("slotDateTime"));
				oSlotDB.gEndDateTime = (lres.getDate("slotDateTime"));
				oSlotDB.gSlotStartTime = (lres.getTime("slotStartTime"));
				oSlotDB.gSlotEndTime = (lres.getTime("slotEndTime"));
				oSlotDB.gSlotType = (lres.getInt("slotType"));
				oSlotDB.gSlotFrequency = (lres.getInt("slotFrequency"));
				oSlotDB.gBuildingId = (lres.getString("buildingId"));
				oSlotDB.gRoomNo = (lres.getString("roomNo"));
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		lDBConnection.mCloseConnection();
		return oSlotDB;
	}

	public cSlot mGetSlots(String netId) {
		cAppointment oAppointment = new cAppointment();
		cDBConnection lDBConnection = new cDBConnection();
		cSlot Appointment = oAppointment.gSlot;
		Time slotStartTime;
		try {
			ResultSet lres = lDBConnection.aStatement
					.executeQuery("Select slotId,slotEndTime from OfficeSlots where pNetId='" + netId + "'");
			if (lres.next()) {
				slotStartTime = (lres.getTime("slotEndTime"));
				Appointment=(mGetSlotDetails(slotStartTime));
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		lDBConnection.mCloseConnection();
		return Appointment;
	}
SimpleDateFormat lDbFormat = new SimpleDateFormat("yyyy-MM-dd");
	public boolean mCreateSlot(cSlot oSlot, String pNetId) {
		String netId = pNetId;
		cDBConnection lDBConnection = new cDBConnection();
		try {
			lDBConnection.aStatement.executeUpdate("INSERT INTO OfficeSlots "
                    + "(pNetId, slotType, slotStartDateTime,slotEndDateTime, slotStartTime, slotEndTime, slotFrequency, buildingId, roomNo) "
                    + "VALUES ('" + netId + "'," + oSlot.gSlotType + ", '" + lDbFormat.format(oSlot.gStartDateTime) + "', "
                    	+ "'" + lDbFormat.format(oSlot.gEndDateTime) + "', '"
                    + oSlot.gSlotStartTime + "','" + oSlot.gSlotEndTime + "',"
                    + oSlot.gSlotFrequency + ", '" + oSlot.gBuildingId + "','" + oSlot.gRoomNo + "')");
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
			return false;
		}
		lDBConnection.mCloseConnection();
		return true;
	}

	public boolean mCheckSlotDetails(cSlot oSlot) {
		cDBConnection lDBConnection = new cDBConnection();
		boolean check = false;
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery("SELECT * from OfficeSlots "
					+ "WHERE(('"+oSlot.gSlotStartTime+"' BETWEEN slotStartTime and slotEndTime) "
					+ "OR ('"+oSlot.gSlotEndTime+"' BETWEEN slotStartTime and slotEndTime))"
					+ "AND '"+oSlot.gSlotFrequency+"'=slotFrequency");
			if (lres.next()) {
				//while(lres.next()) {
				System.out.println(lres.getString(1));
				//i++;
				//}
				System.out.println("true slot");
			check = true;
			}
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		return check;
	}

	public boolean mUpdateSlot(cSlot oSlot) {
		cDBConnection lDBConnection = new cDBConnection();
		boolean check = false;
		try {
			lDBConnection.aStatement.executeUpdate("UPDATE OfficeSlots " + "SET  slotType = '" + oSlot.gSlotType
					+ "', slotStartTime='" + oSlot.gSlotStartTime + ", slotStartDateTime = '" + oSlot.gStartDateTime
					+ "', slotEndDateTime='" + oSlot.gEndDateTime + ",slotStartTime = '" + oSlot.gSlotStartTime
					+ ",slotEndTime = '" + oSlot.gSlotEndTime + "', slotFrequency = " + oSlot.gSlotFrequency
					+ ", buildingId = '" + oSlot.gBuildingId + "',roomNo='" + oSlot.gRoomNo + "' WHERE officeSlotId = "
					+ oSlot.OfficeSlotId + "'");
			check = true;
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		return check;
	}

	public boolean mCancelSlot(cSlot oSlot) {
		int lSlotId = oSlot.OfficeSlotId;
		cDBConnection lDBConnection = new cDBConnection();
		boolean check = false;
		try {
			lDBConnection.aStatement.executeUpdate("DELETE from OfficeSlots WHERE slotId='" + lSlotId + "'");
			check = true;
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		return check;
	}
	
	public ArrayList<cSlot> mGetSlotList(String lNetID) {
		ArrayList<cSlot> lSlotList = new ArrayList<cSlot>();
		cDBConnection lDBConnection = new cDBConnection();
		try {
			String lQuery="SELECT * "+
							"FROM uta_appointment_app.officeslots "+
							"where pNetId='"+lNetID+"' and "+
							"slotEndDateTime>=CURDATE() "+
							"order by slotId asc";
			
			ResultSet lres = lDBConnection.aStatement.executeQuery(lQuery);
			while (lres.next()) {
				cSlot lSlotDB = new cSlot();
				lSlotDB.OfficeSlotId = (lres.getInt("slotId"));
				lSlotDB.gStartDateTime = (lres.getDate("slotStartDateTime"));
				lSlotDB.gEndDateTime = (lres.getDate("slotEndDateTime"));
				lSlotDB.gSlotStartTime = (lres.getTime("slotStartTime"));
				lSlotDB.gSlotEndTime = (lres.getTime("slotEndTime"));
				lSlotDB.gSlotType = (lres.getInt("slotType"));
				lSlotDB.gSlotFrequency = (lres.getInt("slotFrequency"));
				lSlotDB.gBuildingId = (lres.getString("buildingId"));
				lSlotDB.gRoomNo = (lres.getString("roomNo"));
				lSlotList.add(lSlotDB);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		lDBConnection.mCloseConnection();
		return lSlotList;
	}
	
	public ArrayList<cAppointment> mGetAppointmentDetails(int Slotid,java.util.Date date ){
		ArrayList<cAppointment> lAppointmentList=new ArrayList<cAppointment>();
		cDBConnection lDBConnection = new cDBConnection();
		SimpleDateFormat lDbFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
			/*System.out.println(" SELECT * "+
					"  FROM appointments apt "+
					" left join officeslots os on apt.SlotId =os.SlotId "+
					" left join Status stat on apt.StatusId =stat.StatusId "+
					" left join Comment com  on apt.commentid =com.commentid "+
					" where  apt.SlotId='"+Slotid+"' and "+
					" apt.appointmentDate='"+lDbFormat.format(date)+"' "+
					" order by appointmentId asc");*/
			ResultSet lres = lDBConnection.aStatement.executeQuery(" SELECT * "+
					"  FROM appointments apt "+
					" left join officeslots os on apt.SlotId =os.SlotId "+
					" left join Status stat on apt.StatusId =stat.StatusId "+
					" left join Comment com  on apt.commentid =com.commentid "+
					" where  apt.SlotId='"+Slotid+"' and "+
					" apt.appointmentDate='"+lDbFormat.format(date)+"' "+
					" and (apt.statusId is null or apt.statusId<>'3') "+
					" order by appointmentId asc");
		
			while(lres.next()) {
				
				cAppointment lAppointment=new cAppointment();
				lAppointment.gAppointmentId=lres.getInt("appointmentId"); 
				lAppointment.gAttendees.put("student",lres.getString("sNetId"));	
				lAppointment.gAttendees.put("professor", lres.getString("pNetId"));
				
				cSlot lSlot=new cSlot();
				lSlot.OfficeSlotId =lres.getInt("slotId");
				lSlot.gSlotType =lres.getInt("slotType");
				lSlot.gStartDateTime =lres.getDate("slotStartDateTime");
				lSlot.gEndDateTime =lres.getDate("slotEndDateTime");
				lSlot.gSlotFrequency =lres.getInt("slotFrequency");
				lSlot.gBuildingId =lres.getString("buildingId");;
				lSlot.gRoomNo =lres.getString("roomNo");
				//lSlot.gAppointmentTime=lres.getTime("");
				
				lAppointment.gSlot=(lSlot);
				lAppointment.gStatusId =lres.getInt("statusId");
				//System.out.println(lAppointment.gStatusId);
				lAppointment.gcommentId	=lres.getInt("commentid");
				
				lAppointmentList.add(lAppointment);
				
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		
		return lAppointmentList;
	}
	
}