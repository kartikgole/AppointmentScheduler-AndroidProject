package info;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import base.cMessage;
import uta.dbconnector.cDBConnection;

public class cCommentDB {
	public cCommentDB(){
		
	}
	public boolean mSendMessage (String rRequestId, String rSenderId, String rContent) {
		LocalDateTime lNow = LocalDateTime.now();
		DateTimeFormatter lDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		DateTimeFormatter lTime = DateTimeFormatter.ofPattern("HH:mm:ss");
		cDBConnection lDBConnection = new cDBConnection();
		boolean updateStatus = false;
		
		try {
			lDBConnection.aStatement.executeUpdate(
					"INSERT INTO Comment (comment) VALUES ('" + rContent + "')", Statement.RETURN_GENERATED_KEYS);
			ResultSet lres = lDBConnection.aStatement.getGeneratedKeys();
			
			if (lres.next()) {
				int lCommentId = lres.getInt(1);
				lres.close();
				lDBConnection.aStatement.executeUpdate(
						"INSERT INTO Messages (requestId, commentId, senderNetId, sentDate, sentTime) "
						+ "VALUES (" + rRequestId + ", " + lCommentId + ", '" + rSenderId
						+ "', '" + lDate.format(lNow) + "', '" + lTime.format(lNow) + "')");
				updateStatus = true;
			}
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		
		lDBConnection.mCloseConnection();
		return updateStatus;
	}
	
	
	public boolean mCreateMessage (String rRequestorId, String rSenderId, String rContent) {
		LocalDateTime lNow = LocalDateTime.now();
		DateTimeFormatter lDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		DateTimeFormatter lTime = DateTimeFormatter.ofPattern("HH:mm:ss");
		cDBConnection lDBConnection = new cDBConnection();
		boolean updateStatus = false;
		
		try {
			lDBConnection.aStatement.executeUpdate(
					"INSERT INTO Comment (comment) VALUES ('" + rContent + "')", Statement.RETURN_GENERATED_KEYS);
			ResultSet lres = lDBConnection.aStatement.getGeneratedKeys();
			
			if (lres.next()) {
				int lCommentId = lres.getInt(1);
				
					lDBConnection.aStatement2.executeUpdate(
							"INSERT INTO request (sNetId, pNetId) VALUES ('"+rRequestorId+"', '"+rSenderId+"')", Statement.RETURN_GENERATED_KEYS);
					
					ResultSet lres2 = lDBConnection.aStatement2.getGeneratedKeys();
					lres2.next();
					int lRequesterId = lres2.getInt(1);
					
					lDBConnection.aStatement2.executeUpdate(
							"INSERT INTO Messages (requestId, commentId, senderNetId, sentDate, sentTime) "
							+ "VALUES (" + lRequesterId + ", " + lCommentId + ", '" + rRequestorId
							+ "', '" + lDate.format(lNow) + "', '" + lTime.format(lNow) + "')");
					updateStatus = true;
					
				
			}
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
			updateStatus = false;
		}
		
		lDBConnection.mCloseConnection();
		return updateStatus;
	}
	
	public ArrayList<cMessage> mGetChatData (String rNetId) {
		ArrayList<cMessage> lMessages = new ArrayList<cMessage>();
		cDBConnection lDBConnection = new cDBConnection();
		
		try {
			ResultSet lres = lDBConnection.aStatement.executeQuery("SELECT Messages.requestId, "
					+ "Messages.senderNetId, Messages.sentDate, Messages.sentTime, Request.sNetId, "
					+ "Request.pNetId, Comment.comment FROM Messages INNER JOIN Request ON "
					+ "Messages.requestId=Request.requestId INNER JOIN Comment ON "
					+ "Messages.commentId=Comment.commentId WHERE Request.sNetId='"
					+ rNetId + "' OR Request.pNetId='" + rNetId + "'");
			while (lres.next()) {
				cMessage lMessage = new cMessage();
				lMessage.gRequestId = lres.getInt("requestId");
				lMessage.gStudentId = lres.getString("sNetId");
				lMessage.gProfId = lres.getString("pNetId");
				lMessage.gComment = lres.getString("comment");
				lMessage.gSenderId = lres.getString("senderNetId");
				lMessage.gSentDate = lres.getDate("sentDate");
				lMessage.gSentTime = lres.getTime("sentTime");
				
				lMessages.add(lMessage);
			}
			
			lres.close();
		} catch (Exception excep) {
			System.err.println(excep.getMessage());
		}
		
		lDBConnection.mCloseConnection();
		return lMessages;
	}
	
	public static void main(String arg[]){
		cCommentDB cCommentDB=new cCommentDB();
		cCommentDB.mCreateMessage("1002", "1002", "zyx");
	}
}
