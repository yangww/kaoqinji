package com.android.melo.kaoqinfuxiao.utils;

import android.os.Environment;

import java.io.File;

/**
 * SdCardUtils
 */
public class SDcardUtils {
    public static String getSDPath() {
		File sdDir = null;
		sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		return sdDir.getPath();
	}

	public static String isExist(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}
		return file.getPath(); 
	}
	public static void delFile(String path){
		File f=new File(path);
		f.delete();
	}
}
