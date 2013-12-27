package com.sqr.dabbawalaapplication;

import android.app.Application;
import android.content.Context;

public class App extends Application{
	 private static Context mContext;
	 private static boolean isAppInForeground;

	    @Override
	    public void onCreate() 
	    {
	        super.onCreate();
	        mContext = this;
	    }

	    public static Context getContext() //For accessing application resources from any class(static or non activity)
	    {
	        return mContext;
	    }

		public static boolean getIsAppInForeground() {
			return isAppInForeground;
		}

		public static void setIsAppInForeground(boolean isAppInForeground) {
			App.isAppInForeground = isAppInForeground;
		}
	
	    
	    
	  /* public String username="",password="";
	   
	   public void setUsernamePassword(String uname, String pass){
		   this.username  = uname;
		   this.password = pass;
	   }
	   
	   public String getUsername(){
		   return this.username;
	   }
	   
	   public String getPassword(){
		   return this.password;
	   }*/
}
