package com.ultrapower.baozouqiwen;

import android.app.Application;

public class BaozouApplication extends Application {
	
	private static final String TAG = "Baozouapplication";  
	
    public  final static  int LINKNETWORKRETRYTIME=10; // 联网失败 尝试次数
	
	public  final static  String  MainViewPagerSourceUrl="http://wusong.sinaapp.com/focus_json.php";
	
	public final static  String MainListViewSourecUrl="http://wusong.sinaapp.com/dl_json.php?";
	
	public final static  String DetailNewsSourceUrl="http://wusong.sinaapp.com/diary_json.php?aid=";
	
	public final static  String DetailNewsSource="DetailNewsSourceURL";
	
	public final  static String  MainListViewNewsAountUrl="http://wusong.sinaapp.com/dl_count.php";
	
	public final  static String  MianListViewIconUrl="http://wusong.sinaapp.com/dategif.php?date=";

	
}
