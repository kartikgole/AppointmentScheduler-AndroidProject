package info;

import java.sql.ResultSet;
import java.util.ArrayList;

import base.cUser;
import uta.dbconnector.cDBConnection;

public class cSearchDB {
	public ArrayList<cUser> mGetProf (String netId) {
		ArrayList<cUser> professors = new ArrayList<cUser>();
		cDBConnection lDBConnection = new cDBConnection();
		
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery(
					"SELECT name, email, netId FROM UserAccounts "
					+ "WHERE departmentId = (SELECT departmentId "
					+ "FROM UserAccounts WHERE netId='" + netId + "') AND userType=2");
			
			while(lres.next()) {
				cUser professor = new cUser();
				professor.gNetId = lres.getString("netId");
				professor.gName = lres.getString("name");
				professor.gEmail = lres.getString("email");
				professors.add(professor);
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		
		lDBConnection.mCloseConnection();
		return professors;
	}
	
	public ArrayList<cUser> mSearchProf (String keyword) {
		ArrayList<cUser> sresult1 = new ArrayList<cUser>();
		cDBConnection lDBConnection = new cDBConnection();
		System.out.println(keyword);
		try {
//			ResultSet lres = lDBConnection.aStatement.executeQuery(
//					"SELECT UserAccounts.name, UserAccounts.email, "
//					+ "Departments.name, UserAccounts.netId "
//					+ "FROM UserAccounts "
//					+ "INNER JOIN Departments ON UserAccounts.departmentId=Departments.departmentId "
//					+ "WHERE (netId = '" + keyword
//					+ "' OR UserAccounts.name like '%" + keyword+ "%' OR email = '" + keyword
//					+ "' OR UserAccounts.departmentId = '" + keyword
//					+ "') AND userType=2");
			ResultSet lres = lDBConnection.aStatement.executeQuery(
					"SELECT netid,name from UserAccounts WHERE userType=2 AND name LIKE '%" + keyword + "%'");
			while(lres.next()) {
				cUser results = new cUser();
				
				results.gNetId = lres.getString("netId");
				
				results.gName = lres.getString("name");
				System.out.println(results.gName);
				//results.gEmail = lres.getString("email");
				//results.gDepartmentName = lres.getString("Departments.name");
				sresult1.add(results);
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		
		lDBConnection.mCloseConnection();
		return sresult1;
	}
}
