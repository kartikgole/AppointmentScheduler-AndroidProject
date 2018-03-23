package base;

import java.sql.Time;
import java.util.Date;

public class cMessage {
	public int gRequestId;
	public String gStudentId;
	public String gProfId;
	public String gComment;
	public String gSenderId;
	public Date gSentDate;
	public Time gSentTime;
	
	public cMessage () {
		gRequestId = 0;
		gStudentId = null;
		gProfId = null;
		gComment = null;
		gSenderId = null;
		gSentDate = null;
		gSentTime = null;
	}
}
