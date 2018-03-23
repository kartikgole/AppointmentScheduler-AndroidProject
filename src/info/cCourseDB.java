package info;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import base.cCourse;
import base.cSlot;
import base.cUser;
import info.cUserDB;
import uta.dbconnector.cDBConnection;
import info.cSlotDB;

public class cCourseDB {
	public ArrayList<cCourse> mGetCourses (String rNetId) {
		ArrayList<cCourse> lCourses = new ArrayList<cCourse>();
		cDBConnection lDBConnection = new cDBConnection();
		
		try {
			String lQuery=" select * "+
					" from Courses "+
					" where pNetId = '"+rNetId+"' or "+
					"  pNetId in (SELECT pNetId  FROM Enrollment  "+  
					" INNER JOIN Courses ON Enrollment.courseId=Courses.courseId  WHERE netId = '"+rNetId+"') ";	
			//System.out.println(lQuery);
			ResultSet lres = lDBConnection.aStatement.executeQuery(lQuery);
			
			while (lres.next()) {
				cCourse lCourse = new cCourse();
				lCourse.gCourseId = lres.getString("courseId");
				lCourse.gCourseName=lres.getString("name");
				lCourse.gCourseNo=lres.getString("courseno");
				lCourse.gProfessorNetid=lres.getString("pNetId");
				lCourse.gSectionNo=lres.getString("sectionno");
				
				lCourses.add(lCourse);
				
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep);
		}
		
		lDBConnection.mCloseConnection();
		return lCourses;
	}
	
	private String mSlotsToString (cSlot rSlots) {
		String lOfficeHours = "";
		
		if (rSlots.gSlotFrequency == 1) {
			if (!lOfficeHours.equals("")) {
				lOfficeHours += ", ";
			}
			
			SimpleDateFormat lDateFormat = new SimpleDateFormat("EEEE");
			lOfficeHours += lDateFormat.format(rSlots.gStartDateTime);
			lOfficeHours += " ";
			lOfficeHours += rSlots.gSlotStartTime.toString();
			lOfficeHours += "-";
			lOfficeHours += rSlots.gSlotEndTime.toString();
		}
		
		
		return lOfficeHours;
	}
}
