package base;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class cAppointment {
	public int gAppointmentId;
	public Hashtable<String,String> gAttendees ; //professor and student
	public cSlot gSlot;
	public int gStatusId;
	public int gcommentId;
	public Date gAppointmentDate;
	public int gCurrentStatusId;
	
	public cAppointment(){
		gAppointmentId=0;
		gAttendees=new Hashtable<String,String>();
		gAttendees.put("student", "");
		gAttendees.put("professor", "");
		gSlot=new cSlot() ;
		gAppointmentDate=new Date();
		gStatusId=0;
		gcommentId=0;
		gCurrentStatusId=0;
	}
	
}
