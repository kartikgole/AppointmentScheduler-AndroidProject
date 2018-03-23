package base;

import java.util.ArrayList;
import java.util.Hashtable;

public class cComments {
	public int RequestId;
	public ArrayList<Hashtable<String,String>>Comments;

	public cComments(){
		RequestId=0;
		Comments=new ArrayList<Hashtable<String,String>>(); //netid,comment
	}
	
}
