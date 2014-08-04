package com.database;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class Db4oHelper {

    private static ObjectContainer oc = null;

    //Create and open DB
    
    public static ObjectContainer db() {
	    try {
			if (oc == null || oc.ext().isClosed()) {
				oc = Db4o.openFile("db.bd4o"); 
			};
		
			return oc;

	    } 
		catch (Exception ie) {
			 //TODO error
			return null;
		}
    }

    public void close() {
    	if (oc != null){
    		oc.close();
    	 };
	}	


}

   