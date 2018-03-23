package info;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Statement;

import base.cAppointment;
import base.cComments;
import base.cSlot;
import base.cUser;
import uta.dbconnector.cDBConnection;

public class cAppointmentDB {
	
	
	public cAppointmentDB(){
		
	}
	
	public JSONObject mCovertToJsonFormat(cAppointment rAppointment){
		
		JSONObject lJsonAppointment=new JSONObject();
		cUserDB lUserDB=new cUserDB();
		cUser luser=lUserDB.mGetUser(rAppointment.gAttendees.get("student"));
		cUser luser2=lUserDB.mGetUser(rAppointment.gAttendees.get("professor"));
		lJsonAppointment.put("appointmentid", ""+rAppointment.gAppointmentId+"");
		lJsonAppointment.put("student", ""+rAppointment.gAttendees.get("student")+"");
		lJsonAppointment.put("studentname", ""+luser.gName+"");
		
		lJsonAppointment.put("professor", ""+rAppointment.gAttendees.get("professor")+"");
		lJsonAppointment.put("professorname", ""+luser2.gName+"");
		lJsonAppointment.put("appointmentdate", ""+rAppointment.gAppointmentDate+"");
		lJsonAppointment.put("commentid", ""+rAppointment.gcommentId+"");
		lJsonAppointment.put("statusid", ""+rAppointment.gStatusId+"");
		lJsonAppointment.put("slotid", ""+rAppointment.gSlot.OfficeSlotId+"");
		lJsonAppointment.put("slotstartTime", ""+rAppointment.gSlot.gSlotStartTime+"");
		
		return lJsonAppointment;
	}
	
	public JSONArray mCovertToJsonFormat(ArrayList<cAppointment> rAppointmentList){
		
		JSONObject lJsonAppointment=new JSONObject();
		JSONArray lJsonAppointmentList=new JSONArray();
		
		for(int i=0; i<rAppointmentList.size();i++){
			lJsonAppointment=mCovertToJsonFormat(rAppointmentList.get(i));
			lJsonAppointmentList.add(lJsonAppointment);
		}
		
		return lJsonAppointmentList;
	}
	
	public boolean mCreateAppointment(cAppointment rAppointment){
		
		boolean lReturn=false;
		cDBConnection lDBConnection = new cDBConnection();
		Date lDate=new Date();
		SimpleDateFormat lDbFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			String lQuery="INSERT INTO appointments ( creationDateTime, sNetId, slotId, statusId, commentid,appointmentdate) "
							+ " VALUES ( '"+lDbFormat.format(lDate)+"', '"+
									rAppointment.gAttendees.get("student")+"', "+
							        rAppointment.gSlot.OfficeSlotId+", "+
									"null, "+
							        "null, '"+
							        lDbFormat.format(rAppointment.gAppointmentDate)+"')";
			
			//System.out.println(lQuery);
			lDBConnection.aStatement.executeUpdate(lQuery);
			lReturn=true;
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
			lReturn=false;
		}
		lDBConnection.mCloseConnection();
		return lReturn;
	}
	
	public cAppointment mGetAppointmentDetails(int AppointmentId ){
		cAppointment lAppointment=new cAppointment();
		cDBConnection lDBConnection = new cDBConnection();
	
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery( " SELECT * "+
																	" FROM appointments apt "+
																	" left join officeslots os on apt.SlotId =os.SlotId "+
																	" left join Status stat on apt.StatusId =stat.StatusId "+
																	" left join Comment com  on apt.commentid =com.commentid "+
																	" where  apt.appointmentId='"+AppointmentId+"'");
		
			if(lres.next()) {
				
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
				lAppointment.gcommentId	=lres.getInt("commentid");
				
				
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		
		return lAppointment;
	}
	
	public ArrayList<cAppointment> mGetExistingAppointments(String rNetId){
		
		ArrayList<cAppointment> lAppointments=new ArrayList<cAppointment>();
		cDBConnection lDBConnection = new cDBConnection();
	
		try {
			/*System.out.println(" SELECT * "+
								" FROM appointments apt "+
								" left join officeslots os on apt.SlotId =os.SlotId "+
								" left join Status stat on apt.StatusId =stat.StatusId "+
								" left join Comment com  on apt.commentid =com.commentid "+
								" where apt.appointmentdate >=CURDATE() and "+
								" apt.sNetId='"+rNetId+"'");*/
			ResultSet lres = lDBConnection.aStatement.executeQuery( " SELECT * "+
																	" FROM appointments apt "+
																	" left join officeslots os on apt.SlotId =os.SlotId "+
																	" left join Status stat on apt.StatusId =stat.StatusId "+
																	" left join Comment com  on apt.commentid =com.commentid "+
																	" where apt.appointmentdate >=CURDATE() and "+
																	" (apt.statusId is null or apt.statusId<>'3') and "+
																	" apt.sNetId='"+rNetId+"'");
			
			while(lres.next()) {
				
				cAppointment lAppointmentDetails=new cAppointment();
				lAppointmentDetails.gAppointmentId=lres.getInt("appointmentId"); 
				lAppointmentDetails.gAttendees.put("student",lres.getString("sNetId"));	
				lAppointmentDetails.gAttendees.put("professor", lres.getString("pNetId"));
				
				cSlot lSlot=new cSlot();
				lSlot.OfficeSlotId =lres.getInt("slotId");
				lSlot.gSlotType =lres.getInt("slotType");
				lSlot.gStartDateTime =lres.getDate("slotStartDateTime");
				lSlot.gSlotStartTime=lres.getTime("slotstarttime");
				lSlot.gEndDateTime =lres.getDate("slotEndDateTime");
				lSlot.gSlotFrequency =lres.getInt("slotFrequency");
				lSlot.gBuildingId =lres.getString("buildingId");;
				lSlot.gRoomNo =lres.getString("roomNo");
				
				lAppointmentDetails.gSlot=(lSlot);
				lAppointmentDetails.gStatusId =lres.getInt("statusId");
				lAppointmentDetails.gcommentId	=lres.getInt("commentid");
				Calendar lStartDate = Calendar.getInstance();
				lStartDate.setTime(lSlot.gStartDateTime);
				lAppointmentDetails.gAppointmentDate=lStartDate.getTime();
				
				lAppointments.add(lAppointmentDetails);
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		
		return lAppointments;
	}
	
	public boolean mCheckAppointmentDetails(cAppointment lAppointments ){
		
		boolean lReturn=false;
		cDBConnection lDBConnection = new cDBConnection();
	
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery( " SELECT * "+
																	" FROM appointments apt "+
																	" left join officeslots os on apt.SlotId =os.SlotId "+
																	" left join Status stat on apt.StatusId =stat.StatusId "+
																	" left join Comment com  on apt.commentid =com.commentid "+
																	" where  apt.slotId ='"+lAppointments.gSlot.OfficeSlotId+"' and " +
																	//" apt.sNetId='"+rNetId+"' " +
																	" (apt.statusid<>'2' or apt.statusid is null) and "+
																	" apt.sNetId='"+lAppointments.gAttendees.get("student")+"'");
			
			if(lres.next()) {
				lReturn=true;
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		
		return lReturn;
	}
	
	public boolean mCancelAppointment(cAppointment rAppointment,String rComment){
		cDBConnection lDBConnection = new cDBConnection();
		
		int lToken=0;
		boolean lReturn=false;
		try {
			
			String lQuery="insert into comment (comment) values ('"+rComment+"')";
			lDBConnection.aStatement.executeUpdate(lQuery, Statement.RETURN_GENERATED_KEYS);
			ResultSet result = lDBConnection.aStatement.getGeneratedKeys();
			if (result.next())
			{
				lToken=result.getInt(1);
			}
			result.close(); 
			
			 lQuery="UPDATE appointments SET statusId='3', commentid='"+lToken+"' WHERE `appointmentId`='"+rAppointment.gAppointmentId+"'";
			 lDBConnection.aStatement.executeUpdate(lQuery);
			 
			 lReturn=true;

		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		return lReturn;
	}

	public boolean mUpdateAppointment(cAppointment rAppointment,String rComments ){
		
		cDBConnection lDBConnection = new cDBConnection();
		int lToken=0;
		try {
			String lQuery="insert into comment (comment) values ('"+rComments+"')";
			lDBConnection.aStatement.executeUpdate(lQuery, Statement.RETURN_GENERATED_KEYS);
			ResultSet result = lDBConnection.aStatement.getGeneratedKeys();
			if (result.next())
			{
				lToken=result.getInt(1);
			}
			result.close(); 
			
			lQuery="UPDATE appointments SET statusId='"+rAppointment.gStatusId+"', commentId='"+lToken+"' WHERE appointmentId='"+rAppointment.gAppointmentId+"';";
			lDBConnection.aStatement.executeUpdate(lQuery);

		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		return true;
		
	}
	
	public boolean mUpdateAppointmentLiveStatus(String Slotid, Date ApptDate){
		cDBConnection lDBConnection = new cDBConnection();
		boolean lreturn=false;
		SimpleDateFormat lDbFormat=new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			String lQuery=" SELECT *   FROM appointments apt "+  
					" left join officeslots os on apt.SlotId =os.SlotId "+  
					" left join Status stat on apt.StatusId =stat.StatusId   "+
					" left join Comment com  on apt.commentid =com.commentid   "+
					" where  apt.SlotId='"+Slotid+"' and   "+
					" (apt.StatusId<>'1' or apt.StatusId is null) and "+
					" apt.appointmentDate='"+lDbFormat.format(ApptDate)+"'  order by appointmentId asc "+
					" LIMIT 2";
			
			ResultSet lres = lDBConnection.aStatement.executeQuery( lQuery);
			int count=0;
			while(lres.next()){
				count++;
				int status=lres.getInt("statusId");
				
				if(status==0){
					lQuery="UPDATE appointments SET statusId='2' WHERE appointmentId='"+lres.getString("appointmentid")+"';";
					lDBConnection.aStatement2.executeUpdate(lQuery);;
					if(count==1)
						lres.next();
				}else {
					lQuery="UPDATE appointments SET statusId='1' WHERE appointmentId='"+lres.getString("appointmentid")+"';";
					lDBConnection.aStatement2.executeUpdate(lQuery);;
				}
				
			}
			lreturn=true;
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		return lreturn;
	}
	
	public ArrayList<cAppointment> mGetAppointmentForSlot(int rSlotID,Date rAppointmentDate){
		
		ArrayList<cAppointment> lAppointment=new ArrayList<cAppointment>();
			cDBConnection lDBConnection = new cDBConnection();
			
			SimpleDateFormat lDbFormat=new SimpleDateFormat("yyyy-MM-dd");
			try {
				String lQuery= " SELECT * "+
								" FROM appointments apt "+
								" left join officeslots os on apt.SlotId =os.SlotId "+
								" left join Status stat on apt.StatusId =stat.StatusId "+
								" left join Comment com  on apt.commentid =com.commentid "+
								" where  apt.SlotId='"+rSlotID+"' and "+
								" apt.appointmentDate='"+lDbFormat.format(rAppointmentDate)+"' and "+
								" (apt.statusId is null or apt.statusId!=3) "+
								" order by apt.appointmentId asc";
				
				ResultSet lres = lDBConnection.aStatement.executeQuery(lQuery);
				System.out.println(lQuery);
				
				while(lres.next()) {
					cAppointment lAppointmentdetail=new cAppointment();
					
					lAppointmentdetail.gAppointmentId=lres.getInt("appointmentId"); 
					lAppointmentdetail.gAttendees.put("student",lres.getString("sNetId"));	
					lAppointmentdetail.gAttendees.put("professor", lres.getString("pNetId"));
					lAppointmentdetail.gAppointmentDate=lres.getDate("appointmentDate");
					cSlot lSlot=new cSlot();
					lSlot.OfficeSlotId =lres.getInt("slotId");
					lSlot.gSlotType =lres.getInt("slotType");
					lSlot.gStartDateTime =lres.getDate("slotStartDateTime");
					lSlot.gEndDateTime =lres.getDate("slotEndDateTime");
					lSlot.gSlotFrequency =lres.getInt("slotFrequency");
					lSlot.gBuildingId =lres.getString("buildingId");;
					lSlot.gRoomNo =lres.getString("roomNo");
					//lSlot.gAppointmentTime=lres.getTime("");
					
					lAppointmentdetail.gSlot=(lSlot);
					lAppointmentdetail.gStatusId =lres.getInt("statusId");
					lAppointmentdetail.gcommentId	=lres.getInt("comment");
					
					lAppointment.add(lAppointmentdetail);
				}
				
				lres.close();
			} catch (Exception excep) {
				System.err.println(excep.getMessage());
			}
			lDBConnection.mCloseConnection();
			
			return lAppointment;
	}
	
	public ArrayList<cAppointment> mGetApptHistory (String rNetId) {
		ArrayList<cAppointment> lAppointments = new ArrayList<cAppointment>();
		cDBConnection lDBConnection = new cDBConnection();
		
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery(
					"SELECT Appointments.sNetId, Appointments.statusId, "
					+ "Appointments.appointmentDate, UserAccounts.name "
					+ "FROM Appointments INNER JOIN OfficeSlots ON "
					+ "Appointments.slotId=OfficeSlots.slotId INNER JOIN UserAccounts ON "
					+ "OfficeSlots.pNetId=UserAccounts.netId "
					+ "WHERE (Appointments.sNetId = '" + rNetId + "' OR OfficeSlots.pNetId = '" + rNetId + "') "
					+ "AND (Appointments.statusId = 1 OR Appointments.statusId = 3 "
					+ "OR Appointments.statusId = 4);");
			
			while(lres.next()) {
				cAppointment lAppointmentDetails=new cAppointment();
				lAppointmentDetails.gAttendees.put("student",lres.getString("sNetId"));	
				lAppointmentDetails.gAttendees.put("professor", lres.getString("name"));
				lAppointmentDetails.gStatusId =lres.getInt("statusId");
				lAppointmentDetails.gAppointmentDate =lres.getDate("appointmentDate");
				lAppointments.add(lAppointmentDetails);
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		lDBConnection.mCloseConnection();
		
		return lAppointments;
	}
	
	public static void main(String arg[]){
		//cAppointmentDB lAppointmentDB=new cAppointmentDB();
		//lAppointmentDB.mGetAppointmentDetails(2);
		
		/*;
		 
		cAppointment lAppointment=new cAppointment();
		lAppointment.gAppointmentId=0; 
		lAppointment.gAttendees.put("student","1001");	
		lAppointment.gAttendees.put("professor","1002");
		Date ldate=new Date();
		
		cSlot lSlot=new cSlot();
		lSlot.OfficeSlotId =1;
		lSlot.gSlotType =2;
		lSlot.gStartDateTime =ldate;
		lSlot.gEndDateTime =ldate;
		lSlot.gSlotDuration =90;
		lSlot.gSlotFrequency =2;
		lSlot.gBuildingId ="101";
		lSlot.gRoomNo ="201";
		lSlot.gAppointmentTime=ldate;
		
		lAppointment.gSlot.add(lSlot);
		lAppointment.gStatusId =0;
		lAppointment.gcommentId	=0;
		
		lAppointmentDB.mCreateAppointment(lAppointment);*/
		
		//ArrayList<cAppointment> lAppointments= lAppointmentDB.mGetExistingAppointments("1002");
		/*Date ldate=new Date();
		Calendar lCalendar=Calendar.getInstance();
		lCalendar.set(2017, 11, 2);
		ldate.setTime(lCalendar.getTimeInMillis());	
		
		ArrayList<cAppointment> lAppointments= lAppointmentDB.mGetAppointment(2, ldate);
		for(int i=0;i<lAppointments.size();i++){
			cSlot lSlot=lAppointments.get(i).gSlot.get(0);
			System.out.println(lAppointments.get(i).gAppointmentId+"\n"+ 
			lAppointments.get(i).gAttendees.put("student","1001")+"\n"+	
			lAppointments.get(i).gAttendees.put("professor","1002")+"\n"+
	
			lSlot.OfficeSlotId +"\n"+
			lSlot.gSlotType +"\n"+
			lSlot.gStartDateTime +"\n"+
			lSlot.gEndDateTime +"\n"+
			lSlot.gSlotDuration +"\n"+
			lSlot.gSlotFrequency +"\n"+
			lSlot.gBuildingId +"\n"+
			lSlot.gRoomNo +"\n"+
			lSlot.gAppointmentTime+"\n"+
			
			lAppointments.get(i).gStatusId +"\n"+
			lAppointments.get(i).gcommentId	+"\n"+
			"======================================================");
		}*/
		
	}

}
