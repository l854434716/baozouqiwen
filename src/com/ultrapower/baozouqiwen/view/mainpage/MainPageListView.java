package com.ultrapower.baozouqiwen.view.mainpage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
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
import com.ultrapower.baozouqiwen.view.DropDownRefreshListView;
import com.ultrapower.baozouqiwen.vo.MainListViewVo;
import com.ultrapower.baozouqiwen.vo.MainListViewVos;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class MainPageListView extends DropDownRefreshListView{
	

	public MainPageListView(Context context) {
		super(context);
	}
	
	

	public MainPageListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}



	public MainPageListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}





	 /*报告下拉刷新列表加载更多数据的状态给其他组件*/
	@Override
	protected    void noticeListLoadMoreDataState() {

		if (loadMoreDataListener != null) {
			
			loadMoreDataListener.doLoadMoreStateChange(this);
		}else{
			
			throw new RuntimeException("loadMoreDataListener is not set ");
		}
		
	}


	private  OnLoadMoreListener loadMoreDataListener;
	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 */
	protected void loadMoreData() {
		
		Log.d("mainpagerefreashlistview", "开始加载更多数据");
		loadMoreState=MORELODING;
		
		if (loadMoreDataListener != null) {
			loadMoreDataListener.doLoadData();
		}else{
			
			throw new RuntimeException("loadMoreDataListener is not set ");
		}
		
		noticeListLoadMoreDataState();
 	}


	/**
	 * 加载更多完成 
	 * @date 2013-11-11 下午10:21:38
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void onLoadMoreComplete() {
	    loadMoreState=MORELODINGDONE;
 
	    noticeListLoadMoreDataState();
	}


    protected void stopRebound() {
    	if(null!=refreshDataListener)
			refreshDataListener.doStopRebound();
		else
			throw new RuntimeException("refreshDataListener  is not set ");
		
	}



    protected void rebound() {
		if(null!=refreshDataListener)
			refreshDataListener.doRebound();
		else
			throw new RuntimeException("refreshDataListener  is not set ");
	}



	/*报告当前下拉列表的位移量包括下拉和上推的位移量给其他组件
     *param 位移量
     *
     * */
    protected void noticeListDropTranslation() {
 
		if(null !=refreshDataListener)
			refreshDataListener.doRefreshTranslationChange(this);
		else
			throw new RuntimeException("refreshDataListener  is not set ");
	}



	/*报告当前下拉列表下拉刷新状态信息给其他组件
	 * 
	 * */
    protected void noticeListDropRefreshStateChange() {
		
		if(null !=refreshDataListener)
			
		     refreshDataListener.doRefreshStateChange(this);
		else
			throw new RuntimeException("refreshDataListener  is not set ");
		
	}

	
	

	private  OnRefreshListener refreshDataListener;
	/**
	 * 正在下拉刷新
	 * @date 2013-11-20 下午4:45:47
	 * @change JohnWatson
	 * @version 1.0
	 */
	protected void refreshData() {
		refreshState = REFRESHING;
		if (refreshDataListener != null) {
			refreshDataListener.doRefreshData();
		}else{
			
			throw new RuntimeException ("refreshDataListener  is not set");
		}
		noticeListDropRefreshStateChange();
	}



	
	/**
	 * 下拉刷新完成
	 * @date 2013-11-20 下午4:44:12
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void onRefreshComplete() {
		// 下拉刷新后是否显示第一条Item 
		setSelection(0);
		
		refreshState =REFEESHDONE;

		//回弹机制
		noticeListDropRefreshStateChange();
	}
	
	/**
	 * 下拉刷新监听接口
	 * @date 2013-11-20 下午4:50:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public interface OnRefreshListener {
		public void doRefreshData();
		
		
		public void doRefreshStateChange(final MainPageListView dropDownRefreshListView);


		public void doRefreshTranslationChange(final MainPageListView dropDownRefreshListView);
		
		
		public void doRebound();
		
		
		public void doStopRebound();
	}
	
	/**
	 * 加载更多监听接口
	 * @date 2013-11-20 下午4:50:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public interface OnLoadMoreListener {
		public void doLoadData();
		
		public void doLoadMoreStateChange(final MainPageListView listview);
		
		
	}
	
	public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
		if(pRefreshListener != null){
			refreshDataListener = pRefreshListener;
			
		}
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if(pLoadMoreListener != null){
			loadMoreDataListener = pLoadMoreListener;
		}
	}
	

	
	
	
	
	
	
	public class MainPageListViewAdapter extends BaseAdapter {

		
		private List<MainListInfo> mList = new ArrayList<MainListInfo>();
		
		private RequestQueue rq;
		private  int   currentNews;// 已经加载的条数
		private  int  newsAmount;// 新闻总条数
		private  volatile int  dataNumForOneRequest;//一条请求获取到的新闻条数
		private  List<Integer> requestCache=new ArrayList<Integer>();
		
		public MainPageListViewAdapter(RequestQueue rq) {
			super();
			this.rq=rq;
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
						
						addVosToModels(datas,mList);
						onLoadMoreComplete();
						
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
		
		private   void getDataFromNetwork4Refluse(){
			Log.v("network", "开始获取list数据");
			 @SuppressWarnings("unchecked")
			JsonObjectRequest jsonReq=VolleyUtil.createJsonObjectReq(BaozouApplication.MainListViewSourecUrl, null, new Listener(){
				
				@Override
				public void onResponse(Object response) {
					if(null!=response){
						Log.v("network", "开始解析list数据");
						Gson gson=new Gson();
						
						MainListViewVos datas= gson.fromJson(response.toString(), MainListViewVos.class);
						List<MainListInfo> cacheList=new ArrayList<MainListInfo>();
						addVosToModels(datas,cacheList);
						mList=cacheList;
					
						// 完成刷新数据
						
						onRefreshComplete();
						
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
		
		private  void addVosToModels(MainListViewVos datas,List<MainListInfo> list) {
			
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
	    			if(list==null)
	    				return ;
	    			list.add(mInfo);
	    			
	    		}
	        	
	        	this.notifyDataSetChanged();
	        }
		}



		public class ViewHolder {
			private NetworkImageView mImage;
			private TextView mTitle;
			private String id;
			public String getId() {
				return id;
			}
			
			
		}



		public void refeshData() {

	         //  this.mList.clear();
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
						getDataFromNetwork4Refluse();
						
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

}
