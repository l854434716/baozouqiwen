package com.ultrapower.baozouqiwen.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.ultrapower.baozouqiwen.BaozouApplication;
import com.ultrapower.baozouqiwen.R;
import com.ultrapower.baozouqiwen.model.MainListInfo;
import com.ultrapower.baozouqiwen.util.BitmapCache;
import com.ultrapower.baozouqiwen.util.StringUtil;
import com.ultrapower.baozouqiwen.util.VolleyUtil;
import com.ultrapower.baozouqiwen.vo.MainListViewVo;
import com.ultrapower.baozouqiwen.vo.MainListViewVos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DropDownRrfreshListViewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MainListInfo> mList = new ArrayList<MainListInfo>();
	private RequestQueue rq;
	private  int   currentNews;// 已经加载的条数
	private  int  newsAmount;// 新闻总条数
	private  volatile int  dataNumForOneRequest;//一条请求获取到的新闻条数
	private  List<Integer> requestCache=new ArrayList<Integer>();
	
	
	
	public DropDownRrfreshListViewAdapter(Context pContext,List<MainListInfo> mList, RequestQueue rq) {
		mInflater = LayoutInflater.from(pContext);
		this.rq=rq;
		this.mList=mList;
		
		refeshData();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getCount() == 0) {
			return null;
		}
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);

			holder = new ViewHolder();
			holder.mImage = (NetworkImageView) convertView
					.findViewById(R.id.ivIcon);
			holder.mTitle = (TextView) convertView
					.findViewById(R.id.tvTitle);
			
			//convertView.setTag(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MainListInfo ai = mList.get(position);
		//holder.mImage.setImageBitmap(ai.getIcon());
		holder.mImage.setImageUrl(ai.getIconUrl(), new ImageLoader(rq, BitmapCache.getInstance()));
		holder.mImage.setDefaultImageResId(R.drawable.imgload);
		holder.mImage.setErrorImageResId(R.drawable.imgerror);
		holder.mTitle.setText(ai.getTitle());
		holder.id=ai.getId();
		
		

		return convertView;
	}
	
	private   void getDataFromNetwork(final String params){
		Log.v("network", "开始获取list数据");
		
	
		 @SuppressWarnings("unchecked")
		
		JsonObjectRequest jsonReq=VolleyUtil.createJsonObjectReq(BaozouApplication.MainListViewSourecUrl+params, null, new Listener(){
			
			@Override
			public void onResponse(Object response) {
				if(null!=response){
					Log.v("network", "开始解析list数据");
					
					Log.v("分页调试","url="+BaozouApplication.MainListViewSourecUrl+params+"response 为"+response.toString());
					Gson gson=new Gson();
					
					MainListViewVos datas= gson.fromJson(response.toString(), MainListViewVos.class);
					
					addVosToModels(datas);
					
				}else{
					
					Log.e("network", "获取数据失败");
				}
				
			}

			},new  ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError error) {
					
					Log.v("newtwork", "网络错误");
				}});
		 
		 VolleyUtil.addReqToQueue(rq, jsonReq);
		
	}
	
	private   void getDataFromNetwork(){
		Log.v("network", "开始获取list数据");
		 @SuppressWarnings("unchecked")
		JsonObjectRequest jsonReq=VolleyUtil.createJsonObjectReq(BaozouApplication.MainListViewSourecUrl, null, new Listener(){
			
			@Override
			public void onResponse(Object response) {
				if(null!=response){
					Log.v("network", "开始解析list数据");
					Gson gson=new Gson();
					
					MainListViewVos datas= gson.fromJson(response.toString(), MainListViewVos.class);
					
					addVosToModels(datas);
					
				}else{
					
					Log.e("network", "获取数据失败");
				}
				
			}

			},new  ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError error) {
					
					Log.v("newtwork", "网络错误");
				}});
		 
		 VolleyUtil.addReqToQueue(rq, jsonReq);
		
	}
	
	private  void addVosToModels(MainListViewVos datas) {
		
         int num; 
        if(datas!=null&&datas.getDl()!=null&&(num=datas.getDl().size())>0)
        {
        	dataNumForOneRequest=num;
        	currentNews+=dataNumForOneRequest;
        	
        	for(MainListViewVo vo:datas.getDl()){
    			
    			MainListInfo mInfo=new MainListInfo();
    			
    			//mInfo.setIcon(null);
    			mInfo.setIconUrl(BaozouApplication.MianListViewIconUrl+StringUtil.changeDataFormat(vo.getRq()));
    			mInfo.setId(vo.getId());
    			mInfo.setTitle((StringUtil.deCode(vo.getBt(), "ISO-8859-1", "utf-8")).replaceAll("&quot;", "\""));
    			
    			mList.add(mInfo);
    			
    		}
        	
        	this.notifyDataSetChanged();
        }
		
		
		
	}



	public static class ViewHolder {
		private NetworkImageView mImage;
		private TextView mTitle;
		private String id;
		public String getId() {
			return id;
		}
		
		
	}



	public void refeshData() {

           this.mList.clear();
           this.currentNews=0;
           this.requestCache.clear();
           getAmoutpages();
           
		
	}

	private void  getAmoutpages() {

		Log.v("network", "开始获取新闻总数");
		 @SuppressWarnings("unchecked")
		StringRequest stringReq= new StringRequest(BaozouApplication.MainListViewNewsAountUrl, new Listener(){
			
			@Override
			public void onResponse(Object response) {
				if(null!=response){
					Log.v("network", "开始解析数据");

					newsAmount=Integer.parseInt(response.toString());
					Log.v("总新闻个数", response.toString());
					getDataFromNetwork();
					
				}else{
					
					Log.e("network", "获取新闻总数数据失败");
				}
				
			}

			},new  ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError error) {
					
					Log.v("newtwork", "网络错误");
				}});
		
		 VolleyUtil.addReqToQueue(rq, stringReq);
           
		
	}

	public void getMoreData() {
		   int  remainNews= newsAmount- currentNews;
		   
		   
		   if(remainNews==0)// 数据全部获取完成 到达列表末端
			   return ;
		   
		   
          if(remainNews<=dataNumForOneRequest){
        	   int _i=currentNews+dataNumForOneRequest;
        	   if(requestCache.contains(_i))
        		    return;
        	   requestCache.add(_i);
        	  this.getDataFromNetwork("f="+_i);
        	  
          }
          else{
        	  int _i=currentNews+dataNumForOneRequest;
        	  if(requestCache.contains(_i))
      		    return;
      	      requestCache.add(_i);
        	  this.getDataFromNetwork("f="+_i);
          }
		
	}
}
