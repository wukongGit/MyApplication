package com.suncheng.myapplication.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.suncheng.myapplication.framework.MyApplication;

public class Version {

	/**
	 * 获取当前版本号
	 *
	 * @return
	 */
	public static String getVersionName()
	{
		Context context = MyApplication.getApp();
	    PackageManager packageManager = context.getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息   
	    PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionName;   
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获取当前App的versionCode（从1开始），如果没找到（NameNotFoundException），返回0
	 * @Title: getVersionCode
	 * @Description: TODO
	 * @return
	 */
	public static int getVersionCode()
	{
		Context context = MyApplication.getApp();
	    PackageManager packageManager = context.getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息   
	    PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionCode;   
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
