package uta.dbconnector;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.io.*;

/**
 * @Created Amith - 
 * Getting a new DB Connection 
 *
 */

public class cDBConnection
{
	public	Connection aConnection ;
	public  Statement aStatement, aStatement1, aStatement2, aStatement3, aStatement4, aStatement5, aStatement6, aStatement7, aStatement8,
						aPassedStatement, aPassedStatement2 ;
	public	PreparedStatement aPreStatement, aPassedPreStatement ;
	private String 	aDBName,aDBUser,aDBPassword,aDBIPAddress,aDBPortNumber;
	
	  
	/* Method to Create new Connection for Database*/
	public cDBConnection() 
	{
		try
		{
			PropertyResourceBundle lResourceFile = (PropertyResourceBundle)ResourceBundle.getBundle("Application"); // the config file name
			
			aDBName =  lResourceFile.getString("DatabaseName");
			aDBUser =  lResourceFile.getString("UserName");
			aDBPassword =  lResourceFile.getString("Password");
			aDBIPAddress =  lResourceFile.getString("IPAddress");
			aDBPortNumber =  lResourceFile.getString("PortNumber");

		}
		catch(Exception ex)
		{
			System.out.println("Resources Bundle ex: "+ex);
			ex.printStackTrace();
		}
		
		try 
		{
			String lConnectionURL = "jdbc:mysql://"+this.aDBIPAddress+":"+this.aDBPortNumber+"/"+this.aDBName;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//System.out.println(this.aDBIPAddress+"--"+this.aDBPortNumber+"--"+this.aDBName+"--"+this.aDBUser+"--"+this.aDBPassword+"--"+lConnectionURL);
			aConnection = DriverManager.getConnection(lConnectionURL, this.aDBUser, this.aDBPassword);
			//lConnection.setAutoCommit(false);
			aStatement = aConnection.createStatement();
			aStatement1 = aConnection.createStatement();
			aStatement2 = aConnection.createStatement();
			aStatement3 = aConnection.createStatement();
			aStatement4 = aConnection.createStatement();
			aStatement5 = aConnection.createStatement();
			aStatement6 = aConnection.createStatement();
			aStatement7 = aConnection.createStatement();
			aStatement8 = aConnection.createStatement();
			aPassedStatement = aConnection.createStatement();
			aPassedStatement2 = aConnection.createStatement();
			aPreStatement = null;
			aPassedPreStatement = null;
		} 
		catch (SQLException sqle) 
		{
			/*if(sqle.getMessage().contains("SQL String can not be NULL"));
			else*/
			System.out.println("cDBConnection SQL Error1: "+sqle);
			sqle.printStackTrace();
		}
		catch (InstantiationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Instantiation Error: "+e);
		}
		catch (IllegalAccessException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IllegalAccess Error: "+e);
		}
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ClassNotFound Error: "+e);
		}
	}
  
	
	public String mGetSchemaName()
	{
		return aDBName;
	}
	
	/* Method to Create new Statement Connection*/
	public Statement mCreateStatement()
	{
		Statement lStatement = null;
		try
		{
			lStatement = aConnection.createStatement();
		}
		catch (SQLException sqle) 
		{
			System.out.println("Get mPreStatement SQL Error: "+sqle);
		}
		
		return lStatement;
	}
	
	/* Method to Create new Prepared Statement Connection*/
	public void mPreStatement(String Q)
	{
		try
		{
			aPreStatement = aConnection.prepareStatement(Q);
		}
		catch (SQLException sqle) 
		{
			System.out.println("Get mPreStatement SQL Error: "+sqle);
		}
	  
	}
	
	public void mPrePassedStatement(String Q)
	{
		try
		{
			aPassedPreStatement = aConnection.prepareStatement(Q);
		}
		catch (SQLException sqle) 
		{
			System.out.println("Get mPrePassedStatement SQL Error: "+sqle);
		}
	}
  
	
	
  /* Method to Close new Prepared Statement Connection*/
	public void mClosePreStatement()
	{
		try
		{
			if(aPreStatement!=null)
				aPreStatement.close();
			if(aPassedPreStatement!=null)
				aPassedPreStatement.close();
		}
		catch (SQLException sqle) 
		{
			if(aPreStatement!=null)
			{	
				try 
				{
					aPreStatement.close();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("inner mClosePreStatement SQL Error: "+sqle);
				}
			}
			if(aPassedPreStatement!=null)
			{	
				try 
				{
					aPassedPreStatement.close();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("inner mClosePassedPreStatement SQL Error: "+sqle);
				}
			}
			System.out.println("mClosePreStatement SQL Error: "+sqle);
		}
	  
	}

	
	
  /* Method to Close DB Connection*/
	public void mCloseConnection()
	{
		try
		{
			if(aStatement!=null)
				aStatement.close();
			if(aStatement1!=null)
				aStatement1.close();
			if(aStatement2!=null)
				aStatement2.close();
			if(aStatement3!=null)
				aStatement3.close();
			if(aStatement4!=null)
				aStatement4.close();
			if(aStatement5!=null)
				aStatement5.close();
			if(aStatement6!=null)
				aStatement6.close();
			if(aStatement7!=null)
				aStatement7.close();
			if(aStatement8!=null)
				aStatement8.close();
			if(aPassedStatement!=null)
				aPassedStatement.close();
			if(aPassedStatement2!=null)
				aPassedStatement2.close();
			
			mClosePreStatement();
			if(aConnection!=null)
				aConnection.close();
		   // mConnectionCheck5Min();
		}
		catch (SQLException sqle) 
		{
			if(aConnection!=null)
			{	
				try 
				{
					aConnection.close();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("inner mCloseConnection SQL Error: "+sqle);
				}
			}
			System.out.println("mCloseConnection SQL Error: "+sqle);
		}
	  
	}
	
	
	/* Method to Close Statement Connection*/
	public void mCloseStatement(Statement pStatement)
	{
		try
		{
			if(pStatement!=null)
				pStatement.close();
		}
		catch (SQLException sqle) 
		{
			sqle.printStackTrace();
			System.out.println("mCloseConnection SQL Error: "+sqle);
		}
	  
	}
	
	public void mConnectionCheck5Min()
	{
		Connection lConnection=null;
		try
		{
			Socket s = new Socket(this.aDBIPAddress, Integer.parseInt(this.aDBPortNumber));
			String lHostIP=s.getLocalAddress().getHostAddress();
			s.close();
		
			String lConnectionURL = "jdbc:mysql://"+this.aDBIPAddress+":"+this.aDBPortNumber+"/information_schema";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			lConnection = DriverManager.getConnection(lConnectionURL, this.aDBUser, this.aDBPassword);
			ResultSet lResultSet = lConnection.createStatement().executeQuery("SELECT * FROM information_schema.PROCESSLIST WHERE db =\""+this.aDBName+"\" AND HOST LIKE \""+lHostIP+"%\"");
			while(lResultSet.next())
			{
				if(lResultSet.getString(5).equals("Sleep") && lResultSet.getInt(6)>300)
				{
					lConnection.createStatement().execute("Kill "+lResultSet.getString(1));
				}
			}
			lResultSet.close();
			lConnection.close();
		
		}
		catch(Exception Ex)
		{
			Ex.printStackTrace();
			System.out.println("Kill Connection 5 Min:= "+Ex);
		}
		finally
		{
			try
			{
				lConnection.close();
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void mConnectionCheck()
	{
		Connection lConnection=null;
		try
		{
			Socket s = new Socket(this.aDBIPAddress, Integer.parseInt(this.aDBPortNumber));
			String lHostIP=s.getLocalAddress().getHostAddress();
			//System.out.println("socket==="+lHostIP);
			s.close();
		
			String lConnectionURL = "jdbc:mysql://"+this.aDBIPAddress+":"+this.aDBPortNumber+"/information_schema";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//System.out.println(this.aDBIPAddress+"--"+this.aDBPortNumber+"--"+this.aDBName+"--"+this.aDBUser+"--"+this.aDBPassword+"--"+lConnectionURL);
			lConnection = DriverManager.getConnection(lConnectionURL, this.aDBUser, this.aDBPassword);
			ResultSet lResultSet = lConnection.createStatement().executeQuery("SELECT * FROM information_schema.PROCESSLIST WHERE db =\""+this.aDBName+"\" AND HOST LIKE \""+lHostIP+"%\"");
			while(lResultSet.next())
			{
				//System.out.println("ID="+lResultSet.getString(1)+" Time="+lResultSet.getString(6));
				if(lResultSet.getInt(6)>180)
				{
					//System.out.println("Kill "+lResultSet.getString(1));
					lConnection.createStatement().execute("Kill "+lResultSet.getString(1));
				}
			}
			lResultSet.close();
			lConnection.close();
		
		}
		catch(Exception Ex)
		{
			Ex.printStackTrace();
			System.out.println("Kill Connection:= "+Ex);
		}
		finally
		{
			try
			{
				lConnection.close();
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}