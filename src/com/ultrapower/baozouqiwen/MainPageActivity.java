package com.ultrapower.baozouqiwen;

import java.util.ArrayList;
import java.util.List;
import com.android.volley.RequestQueue;
import com.ultrapower.baozouqiwen.adapter.DropDownRrfreshListViewAdapter;
import com.ultrapower.baozouqiwen.adapter.MainViewPagerAdapter;
import com.ultrapower.baozouqiwen.model.MainListInfo;
import com.ultrapower.baozouqiwen.util.ViewHepler;
import com.ultrapower.baozouqiwen.util.VolleyUtil;
import com.ultrapower.baozouqiwen.view.mainpage.MainPageListListener;
import com.ultrapower.baozouqiwen.view.mainpage.MainPageListView;
import com.ultrapower.baozouqiwen.view.mainpage.MainPageListView.MainPageListViewAdapter;
import com.ultrapower.baozouqiwen.view.mainpage.MainPageListView.MainPageListViewAdapter.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainPageActivity extends FragmentActivity /*implements OnClickListener*/{
	// 上半部分
	private  View  listViewViewPagerHeader;
	private  View  listViewTextViewHeader;
	private  ViewPager  viewPager;
	public   final int  ViewPagerHideRatio=6;
	private  MainViewPagerAdapter mainViewPagerAdapter;
	private  TextView  imgTitles;
	private  LinearLayout  dotGroup;
	private  List<TextView> dots;
	
	//下半部分
	
	private static final String TAG = "MainActivity";

	
	private MainPageListViewAdapter mAdapter;
	private MainPageListView mListView;
	private RequestQueue  rq;
	private List<MainListInfo> mList = new ArrayList<MainListInfo>();
	private MainPageListListener  dropListViewListener;
	
	private ProgressBar  listViewLoadDataProgressBar;
	
	private TextView  listViewLoadDataTip;
	
	private View  DropListViewFootView;
	private DropDownRrfreshListViewAdapter mAdapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rq=VolleyUtil.createRequestQueue(getApplicationContext());
        setContentView(R.layout.mainpage);
        
        init();
        

       /*final Handler viewPagerChangeHandler= new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
                   if(msg.what==0x123){
                	   
                	   mainViewPagerAdapter.changeViews(viewPager);
                	   
                	   sendEmptyMessageDelayed(0x123, 4000);
                   }
			}
        	
        	
        	
        };
        
        
        viewPagerChangeHandler.sendEmptyMessageDelayed(0x123, 4000);*/
        
    }


    private void init() {
		// TODO Auto-generated method 
    	listViewViewPagerHeader=LayoutInflater.from(this).inflate(R.layout.mainpage_viewpager, null);
    	listViewTextViewHeader=LayoutInflater.from(this).inflate(R.layout.mainpage_textafterviewpager, null);
        viewPager= (ViewPager) listViewViewPagerHeader.findViewById(R.id.viewPager);
        
        viewPager.setOffscreenPageLimit(2);
        
        viewPager.setPageMargin(30);
        
        dotGroup = (LinearLayout) listViewTextViewHeader.findViewById(R.id.dotGroup);
        
        imgTitles= (TextView) listViewTextViewHeader.findViewById(R.id.title);
        
        dots=new ArrayList<TextView>();
        mainViewPagerAdapter= new MainViewPagerAdapter(getSupportFragmentManager(),imgTitles,dotGroup,rq, this,dots);
        viewPager.setAdapter(mainViewPagerAdapter); 
        
        viewPager.setOnPageChangeListener(mainViewPagerAdapter. new  MainViewPagerListener());
        // 隐藏部分viewpager
        ViewHepler.measureView(listViewViewPagerHeader);
        ViewHepler.measureView(viewPager);
        
        listViewViewPagerHeader.setPadding(0, -viewPager.getMeasuredHeight()/ViewPagerHideRatio, 0, -viewPager.getMeasuredHeight()/ViewPagerHideRatio);
        initListView();
        
	}


	
	private void initListView(){
		mListView = (MainPageListView) findViewById(R.id.dropListView);
		mListView.setLongClickable(true);
		mListView.addHeaderView(listViewViewPagerHeader, null, false);
		
		mListView.addHeaderView(listViewTextViewHeader, null, false);
		mAdapter=mListView.new MainPageListViewAdapter(rq);
		
		mListView.setAdapter(mAdapter);
		mListView.setDropLength(viewPager.getMeasuredHeight()/ViewPagerHideRatio);
		mListView.setRATIO(5);
		
		//添加一个footview
		LayoutInflater inflater= LayoutInflater.from(getApplicationContext());  
		
		DropListViewFootView = inflater.inflate(R.layout.listfooter_more, null);
		DropListViewFootView.setVisibility(View.VISIBLE);
		listViewLoadDataProgressBar = (ProgressBar) DropListViewFootView
				.findViewById(R.id.pull_to_refresh_progress);
		listViewLoadDataTip = (TextView) DropListViewFootView.findViewById(R.id.load_more);
		
		
		mListView.addFooterView(DropListViewFootView);

		dropListViewListener=new MainPageListListener(this);
	
		
		mListView.setOnRefreshListener(dropListViewListener);
		
		mListView.setOnLoadListener(dropListViewListener);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ViewHolder holder= (ViewHolder) view.getTag();
			   Bundle bundle= new Bundle();
			   
			   bundle.putString(BaozouApplication.DetailNewsSource, BaozouApplication.DetailNewsSourceUrl+holder.getId());
                       
			   Intent intent =new Intent(MainPageActivity.this,NewDetailActivity.class);
			   
			   intent.putExtras(bundle);
			   
			   startActivity(intent);
			}
		});
		
		
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		rq.cancelAll(TAG);
	}


	public MainPageListViewAdapter getmAdapter() {
		return mAdapter;
	}



	public MainPageListView getmListView() {
		return mListView;
	}


	public ProgressBar getListViewLoadDataProgressBar() {
		return listViewLoadDataProgressBar;
	}


	public TextView getListViewLoadDataTip() {
		return listViewLoadDataTip;
	}


	public View getDropListViewFootView() {
		return DropListViewFootView;
	}


	


	public View getListViewViewPagerHeader() {
		return listViewViewPagerHeader;
	}
	
	
	 
	

}
