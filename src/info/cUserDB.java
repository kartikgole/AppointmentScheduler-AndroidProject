package info;

import java.sql.ResultSet;
import base.cUser;
import uta.dbconnector.cDBConnection;

public class cUserDB {
	public cUser mGetUser(String rNetId) {
		cUser lUser = new cUser();
		cDBConnection lDBConnection = new cDBConnection();
		
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery("SELECT "
					+ "UserAccounts.netId, UserAccounts.name, UserAccounts.email, "
					+ "UserAccounts.userType, UserAccounts.departmentId, Departments.name "
					+ "FROM UserAccounts INNER JOIN Departments ON "
					+ "UserAccounts.departmentId=Departments.departmentId WHERE netId = '" + rNetId + "'");
			if (lres.next()) {
				lUser.gNetId = lres.getString("netId");
				lUser.gName = lres.getString("UserAccounts.name");
				lUser.gEmail = lres.getString("email");
				lUser.gUserType = lres.getInt("userType");
				lUser.gDepartmentId = lres.getInt("departmentId");
				lUser.gDepartmentName = lres.getString("Departments.name");
				}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		
		lDBConnection.mCloseConnection();
		return lUser;
	}
	
	public String mGetPassword(String rNetId) {
		String password = null;
		cDBConnection lDBConnection = new cDBConnection();
		
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery("SELECT password "
					+ "FROM UserAccounts WHERE netId = '" + rNetId + "'");
			if (lres.next()) {
				password = lres.getString("password");
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		
		lDBConnection.mCloseConnection();
		return password;
	}
}
