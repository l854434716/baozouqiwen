package com.ultrapower.baozouqiwen.adapter;

import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ultrapower.baozouqiwen.BaozouApplication;
import com.ultrapower.baozouqiwen.R;
import com.ultrapower.baozouqiwen.fragment.HeadFragment;
import com.ultrapower.baozouqiwen.util.StringUtil;
import com.ultrapower.baozouqiwen.util.VolleyUtil;
import com.ultrapower.baozouqiwen.vo.MainViewPagerVo;
import com.ultrapower.baozouqiwen.vo.MainViewPagerVos;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter { 
	
	private  volatile  boolean  linkedNetwork;


	private  MainViewPagerVos datas;
	
	
	private TextView imgTitle;
	
	
	private LinearLayout dotGroup;
	
	
	private RequestQueue rq;
	
	
	private Context context;
	

	private List<TextView> dots;
	
	private  int  courrentPage=0;
	
	
	public MainViewPagerAdapter(FragmentManager supportFragmentManager,
			TextView imgTitle, final LinearLayout dotGroup, RequestQueue rq,final Context context, final List<TextView> dots) {
		
		super(supportFragmentManager);
		
		this.imgTitle = imgTitle;
		this.dotGroup = dotGroup;
		this.rq = rq;
		this.context = context;
		this.dots=dots;
		getDataFromNetwork();
	}

	@Override
	public Fragment getItem(int position) {
		
		
		return HeadFragment.createFragemt(position,rq,datas.getFl().get(position).getTp());
	}

	@Override
	public int getCount() {

          if(null!=datas&&datas.getFl()!=null)
        	  return  datas.getFl().size()>0? datas.getFl().size():0;
          else
          return  0;
        	  
	}
	
	public  void  getDataFromNetwork(){
		
		@SuppressWarnings("unchecked")
		JsonObjectRequest jsonReq= VolleyUtil.createJsonObjectReq(BaozouApplication.MainViewPagerSourceUrl, null, new Listener(){

			@Override
			public void onResponse(Object response) {

                if(null!=response){
                	
                	 Gson gson=new Gson();
                     
                     datas= gson.fromJson(response.toString(), MainViewPagerVos.class);
                     
                     List<MainViewPagerVo> fl=datas.getFl();
                     
                     
                     for(int i=0; i<fl.size();i++){
                    	 
                    	 
                    	 fl.get(i).setBt( StringUtil.deCode(fl.get(i).getBt(), "ISO-8859-1", "utf-8"));
                    	 
                     	
                   	     TextView  tv= new TextView(context);
                   	  
                   	     tv.setLayoutParams(new LayoutParams(30,30));
             			 tv.setPadding(0, 0, 2, 0);	
             			 
             			 dots.add(i, tv);
             			
             			if (i==0)
             			{	
             				imgTitle.setText(fl.get(i).getBt());
             		     	tv.setBackgroundResource(R.drawable.radio_sel);						
             			}else {
             				tv.setBackgroundResource(R.drawable.radio);
             			}
             			
             			dotGroup.addView(tv);
                   }
                     
                     linkedNetwork=true;   
                     MainViewPagerAdapter.this.notifyDataSetChanged();
                     
                }else{
                	
                	Log.e("network", "获取数据失败");
                }
				
			}}, new ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError error) {


					Log.e("network", error!=null?error.toString():"网络错误");
					
				}});
		
		
		  VolleyUtil.addReqToQueue(rq, jsonReq);
		
		  
	}
	

	public class  MainViewPagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int currentNum) {
			if(!linkedNetwork)
				return ;
			courrentPage=currentNum;
			dots.get(currentNum).setBackgroundResource(R.drawable.radio_sel);

			imgTitle.setText(datas.getFl().get(currentNum).getBt());
			for(int i=0; i<dots.size();i++){
				
				if(i!=currentNum){
					dots.get(i).setBackgroundResource(R.drawable.radio);
				}
			}
			
		}
		
		
	}


	public boolean isLinkedNetwork() {
		return linkedNetwork;
	}

	public void setLinkedNetwork(boolean linkedNetwork) {
		this.linkedNetwork = linkedNetwork;
	}

	public MainViewPagerVos getDatas() {
		return datas;
	}

	public void setDatas(MainViewPagerVos datas) {
		this.datas = datas;
	}

	public TextView getImgTitle() {
		return imgTitle;
	}

	public void setImgTitle(TextView imgTitle) {
		this.imgTitle = imgTitle;
	}

	public LinearLayout getDotGroup() {
		return dotGroup;
	}

	public void setDotGroup(LinearLayout dotGroup) {
		this.dotGroup = dotGroup;
	}

	public RequestQueue getRq() {
		return rq;
	}

	public void setRq(RequestQueue rq) {
		this.rq = rq;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<TextView> getDots() {
		return dots;
	}

	public void setDots(List<TextView> dots) {
		this.dots = dots;
	}

	public void changeViews(final ViewPager viewPager) {
		
		if(linkedNetwork){
			
			dots.get(courrentPage).setBackgroundResource(R.drawable.radio_sel);

			imgTitle.setText(datas.getFl().get(courrentPage).getBt());
			for(int i=0; i<dots.size();i++){
				
				if(i!=courrentPage){
					dots.get(i).setBackgroundResource(R.drawable.radio);
				}
			
		}
			if(viewPager!=null)
		viewPager.setCurrentItem(courrentPage, true);
			++courrentPage;
			
	      if(courrentPage>getCount()-1)
	    	  courrentPage=0;
	}
   }
    
}
