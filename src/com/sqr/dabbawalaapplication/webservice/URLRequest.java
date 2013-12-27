package com.sqr.dabbawalaapplication.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import android.util.Log;

public class URLRequest {
		
	HttpURLConnection conn=null;
	//HttpURLConnection conn1=null;
	StringBuffer response;	
	
	public String sendRequest(String serviceURL,List<NameValuePair> params)
	{
		
		
		try{
			
			URL url=new URL(serviceURL);
			Log.d("Service URL","in sendRequest"+" "+serviceURL);
			
			//Encoding parameters
			StringBuffer postParameters=new StringBuffer();
			for(int i=0;i<params.size();i++)
			{
				if(i != 0)
				{
					postParameters.append("&");
				}
				postParameters.append(params.get(i).getName());
				postParameters.append("=");
				postParameters.append(URLEncoder.encode(params.get(i).getValue(),"UTF-8"));
			}
			
			//postParameters.append("none=none");
			Log.d("Post Parameters : ","parameter"+" "+postParameters.toString());
			
			conn=(HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(120000);
			conn.setRequestMethod("POST");
			//conn.setFixedLengthStreamingMode(param.getBytes().length);
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
					
			PrintWriter out=new PrintWriter(conn.getOutputStream());
			out.write(postParameters.toString());
			out.flush();
				
			//start listening to the stream
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line;
			response=new StringBuffer();
			while((line=in.readLine())!=null)
			{
				response.append(line);	
			}			
				
			in.close();
			out.close();
		}
		catch(Exception ex){  
			ex.printStackTrace();
			response = new StringBuffer("Error " + ex.getLocalizedMessage());
		}
		finally
		{
			conn.disconnect();
		}
		
		return response.toString();
	}
	
	public String sendRequest(String serviceURL)
	{
		try
		{
			
			URL url=new URL(serviceURL);
			Log.d("Service URL","in sendRequest"+" "+serviceURL);
			
				
			conn=(HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(120000);
			//conn.setFixedLengthStreamingMode(param.getBytes().length);
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
			Log.e("Response Code : ",new Integer(conn.getResponseCode()).toString());
			
					
			//start listening to the stream
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line;
			response=new StringBuffer();
			while((line=in.readLine())!=null)
			{
				response.append(line);	
			}			
				
			in.close();
		}
		catch(Exception ex)
		{  
			ex.printStackTrace();
			response = new StringBuffer("Error " + ex.getLocalizedMessage());
		}
		finally
		{
			conn.disconnect();
		}
		
		return response.toString();
	}
	
	
	
	
}
