package com.ultrapower.baozouqiwen.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {
	
	private LruCache<String ,Bitmap> mCache;
	
   
    
    private static BitmapCache bitmapCache;
    
    
	private  BitmapCache() {
		
		int cacheSize= (int) Runtime.getRuntime().maxMemory()/8;
			
		
		mCache= new LruCache<String,Bitmap>(cacheSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes()*value.getHeight();
			}
			
			
		};
	}

	
	@Override
	public Bitmap getBitmap(String arg0) {
		// TODO Auto-generated method stub
		return mCache.get(arg0);
	}

	@Override
	public void putBitmap(String arg0, Bitmap arg1) {
		// TODO Auto-generated method stub
		mCache.put(arg0, arg1);
		
	}   
	
	public static  BitmapCache  getInstance(){
		
		if(bitmapCache==null)
			bitmapCache=new BitmapCache();
		
		return bitmapCache;
	}

}
