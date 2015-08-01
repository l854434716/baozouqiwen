package com.ultrapower.baozouqiwen.util;

import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class VolleyUtil {
	
	
	public static RequestQueue createRequestQueue(Context context){
	
		return Volley.newRequestQueue(context);

	}
	
	
	public static RequestQueue createRequestQueue(Context context,HttpStack httpStack){
		
		return Volley.newRequestQueue(context, httpStack);

	}
	
	
	public static JsonObjectRequest  createJsonObjectReq(String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener){
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonRequest, listener, errorListener);  
		
		return jsonObjectRequest;
		
		
		
	}
	

    
    
    public static JsonObjectRequest  createJsonObjectReq(URL url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener){
		
	  
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url.toString(), jsonRequest, listener, errorListener);  
		
		return jsonObjectRequest;
		
		
		
	}
	
	

	  

    public static JsonObjectRequest  createJsonObjectReq(int method,String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener){
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,url, jsonRequest, listener, errorListener);  
		
		return jsonObjectRequest;
		
		
		
	}
	
	

    
    
    public static JsonObjectRequest  createJsonObjectReq(int method,URL url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener){
		
	  
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,url.toString(), jsonRequest, listener, errorListener);  
		
		return jsonObjectRequest;
		
		
		
	}
	

	  
    public  static  void  addReqToQueue(final RequestQueue queue,final Request request){
    	
    	  queue.add(request);
    }
}
