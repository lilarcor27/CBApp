package com.lilarcor.planner;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class FileAdministrator
{
	private Context context = null;
	static String privatePath = null;

	static File file = null;
	static FileOutputStream fos = null;
	
	static Calendar fac = Calendar.getInstance();
	
	public FileAdministrator(Context context) {
		this.context = context;
		
		privatePath = new StringBuilder(context.getExternalFilesDir(null).getAbsolutePath()).toString();
	
		try {
			file = new File(FileAdministrator.privatePath + "/ERROR_LOG");
			if(file.exists() == true)
				fos = new FileOutputStream(file, true);
			else 
				fos = new FileOutputStream(file);
		} catch(Exception e) {
		}
	}
	
	public static boolean isOnline(Activity activity) {
		try {
			ConnectivityManager conMan = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			if(wifi.isConnected()) // 와이파이가 연결되었을 경우
				return true;
				
			if(mobile == null)  // 3G, 4G 미지원 기기일 경우
				return false;
			
			if(mobile.isConnected()) // 3G, 4G 가 연결되었을 경우
				return true;
				
		} catch(Exception e) {
			FileAdministrator.writeExceptionLog(e, "isOnline");
		}
		return false;
	}
	
	public static void writeLog(String s) {
		try {
			fos.write(("[" + fac.get(Calendar.HOUR_OF_DAY) + ":" + fac.get(Calendar.MINUTE) + "] " + s + "\n").getBytes());
		} catch(Exception e) {
		}
		return;
	}
	
	public static void writeExceptionLog(Exception e) {
		writeLog("------Exception------");
		writeLog(e.toString());
		writeLog("------Exception------");
		return;
	}
	
	public static void writeExceptionLog(Exception e, String s) {
		writeLog("------Exception------");
		writeLog(s);
		writeLog(e.toString());
		writeLog("------Exception------");	
		return;
	}
}