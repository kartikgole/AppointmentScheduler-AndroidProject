package base;

import java.sql.Time;
import java.util.Date;

public class cSlot {
	
	public int OfficeSlotId;
	public Date gStartDateTime;
	public Date gEndDateTime;
	public Time gSlotStartTime;
	public Time gSlotEndTime;
	//public Date gAppointmentTime;
	public int gSlotType;
	public int gSlotFrequency;
	public String gBuildingId;
	public String gRoomNo;

	public cSlot() {
		OfficeSlotId=0;
		gStartDateTime=new Date();
		gSlotStartTime=null;
		gSlotEndTime=null;
		gEndDateTime=new Date();
		//gAppointmentTime=new Date();
		gSlotType=0;
		gSlotFrequency=0;
		gBuildingId=null;
		gRoomNo=null;
	}
}
