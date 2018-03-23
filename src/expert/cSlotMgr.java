package expert;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import base.cSlot;
import info.cSlotDB;

public class cSlotMgr {
	String lNetId;
	cSlotDB lSlotDBObj = new cSlotDB();

	public cSlotMgr(String netId) {
		lNetId = netId;
	}

	public   cSlot mSlotDbFormat(String slotDetails) {
		cSlot oSlot = new cSlot();
		String[] slot = slotDetails.split(",");
		
		System.out.println(slotDetails);
		
		if (slot[0].equals("1")) {
			oSlot.gSlotType = Integer.parseInt(slot[0]);
			oSlot.gStartDateTime = mDateFormat(slot[1]);
			oSlot.gEndDateTime = mDateFormat(slot[2]);
			Time startTime =new Time(mTimeFormat(slot[3]).getTime());
			oSlot.gSlotStartTime = startTime;
			Time endTime =new Time(mTimeFormat(slot[4]).getTime());
			oSlot.gSlotEndTime = endTime;
			oSlot.gSlotFrequency=Integer.parseInt(slot[5]);
			oSlot.gBuildingId=slot[6];
			oSlot.gRoomNo=slot[7];
		}
		if (slot[0].equals("2")){
			oSlot.gSlotType = Integer.parseInt(slot[0]);
			oSlot.gStartDateTime = mDateFormat(slot[1]);
			oSlot.gEndDateTime = mDateFormat(slot[1]);
			Time startTime =new Time(mTimeFormat(slot[2]).getTime());
			oSlot.gSlotStartTime = startTime;
			Time endTime =new Time(mTimeFormat(slot[3]).getTime());
			oSlot.gSlotEndTime = endTime;
			oSlot.gSlotFrequency=Integer.parseInt(slot[4]);
			oSlot.gBuildingId=slot[5];
			oSlot.gRoomNo=slot[6];
		}
		return oSlot;
	}

	public Date mDateFormat(String date) {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date lDate = null;
		try {
			lDate = (Date) formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lDate;
	}

	public Date mTimeFormat(String time) {
		DateFormat formatter = new SimpleDateFormat("hh:mm");
		Date lTime = null;
		try {
			lTime=formatter.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lTime;
	}

	public boolean mCreateSlot(cSlot oSlot) {
		boolean check = false;
		try {
			 check = lSlotDBObj.mCheckSlotDetails(oSlot);
			 System.out.println(check+"s");
			if (check == false) {
				check = lSlotDBObj.mCreateSlot(oSlot, lNetId);
			} else {
				check = false;
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return check;
	}

	public boolean mUpdateSlot(cSlot oSlot) {
		cSlot lSlotObj = new cSlot();
		boolean check = false;
		try {
			lSlotObj = lSlotDBObj.mGetSlotDetails(oSlot.gSlotStartTime);
			if (lSlotObj != null) {
				check = lSlotDBObj.mUpdateSlot(lSlotObj);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return check;
	}

	public boolean mCancelSlot(int slotId) {
		//cSlot lSlotObj = new cSlot();
		boolean check = false;
		try {
			//lSlotObj = lSlotDBObj.mGetSlotDetails(oSlot.gSlotStartTime);
			//if (lSlotObj != null) {
				//check = lSlotDBObj.mCancelSlot(slotId);
			//}
		} catch (Exception e) {
			System.err.println(e);
		}
		return check;
	}

	public ArrayList<cSlot> mGetSlotList() {

		cSlotDB lSlotDB = new cSlotDB();
		ArrayList<cSlot> lSlotLists = lSlotDB.mGetSlotList(lNetId);
		return lSlotLists;
	}

	public JSONObject mCovertToJsonFormat(cSlot rSlot, boolean CurrDate) {

		Calendar lStartDate = Calendar.getInstance();
		lStartDate.setTime(rSlot.gStartDateTime);
		Calendar lEndDate = Calendar.getInstance();
		lEndDate.setTime(rSlot.gEndDateTime);

		JSONObject lJsonAppointment = new JSONObject();
		/*
		 * if(rSlot.gStartDateTime.compareTo(rSlot.gEndDateTime)==0){
		 * lJsonAppointment.put("gStartDateTime", "\""+rSlot.gStartDateTime+"\"");
		 * }else{
		 * 
		 * }
		 */

		Calendar lNextDate = Calendar.getInstance();
		Calendar lTempDate = Calendar.getInstance();
		lTempDate.setTime(rSlot.gSlotStartTime);

		if (rSlot.gSlotType == 1) {
			if (lNextDate.get(Calendar.DAY_OF_WEEK) == rSlot.gSlotFrequency) {
				int TodayTimeinMin = lNextDate.get(Calendar.HOUR_OF_DAY) * 60 + lNextDate.get(Calendar.MINUTE);
				int SlotTimeinMin = lTempDate.get(Calendar.HOUR_OF_DAY) * 60 + lTempDate.get(Calendar.MINUTE);
				if (TodayTimeinMin >= SlotTimeinMin && CurrDate==false) {
					lNextDate.add(Calendar.WEEK_OF_YEAR, 1);
				}
				lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
				lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
			} else if (lNextDate.get(Calendar.DAY_OF_WEEK) > rSlot.gSlotFrequency) {
				lNextDate.set(Calendar.DAY_OF_WEEK, rSlot.gSlotFrequency);
				lNextDate.add(Calendar.WEEK_OF_YEAR, 1);
				lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
				lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
			} else {
				lNextDate.set(Calendar.DAY_OF_WEEK, rSlot.gSlotFrequency);
				lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
				lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
			}
		} else {
			lNextDate.setTime(rSlot.gStartDateTime);
			lNextDate.set(Calendar.HOUR_OF_DAY, lTempDate.get(Calendar.HOUR_OF_DAY));
			lNextDate.set(Calendar.MINUTE, lTempDate.get(Calendar.MINUTE));
		}

		lJsonAppointment.put("OfficeSlotId", "" + rSlot.OfficeSlotId + "");
		lJsonAppointment.put("gStartDate", "" + lNextDate.getTime() + "");
		lJsonAppointment.put("gSlotStartTime", "" + rSlot.gSlotStartTime + "");
		lJsonAppointment.put("gSlotEndTime", "" + rSlot.gSlotEndTime + "");
		lJsonAppointment.put("gSlotType", "" + rSlot.gSlotType + "");
		lJsonAppointment.put("gBuildingId", "" + rSlot.gBuildingId + "");
		lJsonAppointment.put("gRoomNo", "" + rSlot.gRoomNo + "");

		return lJsonAppointment;
	}

	public JSONArray mCovertToJsonFormat(ArrayList<cSlot> rSlotList, boolean CurrDate) {

		JSONObject lJsonSlot = new JSONObject();
		JSONArray lJsonSlotList = new JSONArray();

		for (int i = 0; i < rSlotList.size(); i++) {
			
			lJsonSlot = mCovertToJsonFormat(rSlotList.get(i),CurrDate);
			lJsonSlotList.add(lJsonSlot);
		}

		return lJsonSlotList;
	}
}
