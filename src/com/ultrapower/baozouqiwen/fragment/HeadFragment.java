package com.ultrapower.baozouqiwen.fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ultrapower.baozouqiwen.R;
import com.ultrapower.baozouqiwen.adapter.MainViewPagerAdapter;
import com.ultrapower.baozouqiwen.util.BitmapCache;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HeadFragment extends Fragment {
	
	private  final static  String ARG_Page="page_num";
	
	private  int  currentPageNum;
	
	private RequestQueue rq;

    private String  imgUrl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		currentPageNum = getArguments().getInt(ARG_Page);  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	   
	  /*  
	    if(mainViewPagerAdapter.isLinkedNetwork())
	    	
	    	imgUrl=mainViewPagerAdapter.getDatas().getFl().get(currentPageNum).getTp();*/
	    
	    NetworkImageView  imgView= (NetworkImageView) inflater.inflate(R.layout.main_page_head_image, container, false);
	    
	    
	    	
	    imgView.setImageUrl(imgUrl, new ImageLoader(rq, BitmapCache.getInstance()));
	   
	    imgView.setDefaultImageResId(R.drawable.imgload);
	    
	    imgView.setErrorImageResId(R.drawable.imgerror);
		
		return imgView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	

	public RequestQueue getRq() {
		return rq;
	}

	public void setRq(RequestQueue rq) {
		this.rq = rq;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public static Fragment createFragemt(int position, RequestQueue rq, String imgUrl) {
		// TODO Auto-generated method stub
        HeadFragment  hf= new HeadFragment();
		
		Bundle bundle =new Bundle();
		
	    bundle.putInt(ARG_Page, position);
	  
	    hf.setArguments(bundle);
	    
	    hf.setRq(rq);
	    
	    hf.setImgUrl(imgUrl);
	    
		return hf;
	}


	

}
