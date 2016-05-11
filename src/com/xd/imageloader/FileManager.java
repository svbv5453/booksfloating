package com.xd.imageloader;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.booksfloating.activity/Imagefiles/";
		} else {
			return CommonUtil.getRootFilePath() + "com.booksfloating.activity/Imagefiles/";
		}
	}
}
