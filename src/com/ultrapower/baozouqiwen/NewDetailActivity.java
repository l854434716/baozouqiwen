package com.ultrapower.baozouqiwen;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.ultrapower.baozouqiwen.util.BitmapCache;
import com.ultrapower.baozouqiwen.util.StringUtil;
import com.ultrapower.baozouqiwen.util.VolleyUtil;
import com.ultrapower.baozouqiwen.vo.NewsDetailVo;
import com.ultrapower.baozouqiwen.vo.NewsDetailVos;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
public class NewDetailActivity extends Activity {
	
	private RequestQueue  rq;
	
    private TextView biaoti;
    
    private TextView date;
    
    private TextView newscontent;
    
    private NetworkImageView newsImage;
    
    private  ActionBar actionBar;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_detail);
		actionBar= getActionBar();
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		rq=VolleyUtil.createRequestQueue(this);
		
		biaoti= (TextView) findViewById(R.id.biaoti);
		date= (TextView) findViewById(R.id.date);
		newscontent= (TextView) findViewById(R.id.newscontent);
		newsImage= (NetworkImageView) findViewById(R.id.newsImage);
		
		Intent intent= getIntent();
		
		String sourceUrl= intent.getStringExtra(BaozouApplication.DetailNewsSource);
		
		@SuppressWarnings("unchecked")
		JsonObjectRequest jsonReq= VolleyUtil.createJsonObjectReq(sourceUrl, null, new Listener(){

			@Override
			public void onResponse(Object response) {
				if(response!=null){
					Gson gson= new Gson();
					
					NewsDetailVos datas=gson.fromJson(response.toString(), NewsDetailVos.class);
					NewsDetailVo data=datas.getDc().get(0);
					biaoti.setText(StringUtil.deCode(data.getBt(), "ISO-8859-1", "utf-8"));
			        date.setText(data.getPt());
			        newscontent.setText(StringUtil.deCode(data.getNr(), "ISO-8859-1", "utf-8"));
					Log.v("network", "成功解析数据");
					
					loadImage(data.getTp());
					
				}else{
					
					Log.e("network", "服务器异常");
				}
			}
			
			
		}, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("network", "网络异常");
			}});
		
		VolleyUtil.addReqToQueue(rq, jsonReq);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_detail, menu);
		return true;
	}
	
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.isCheckable())
			
			item.setChecked(true);
		
		switch(item.getItemId()){
		
		case  android.R.id.home:
			
			  /*Intent intent= new Intent(this,MainActivity.class);
			  
			  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			  
			  startActivity(intent);*/
			  
			  this.finish();
			  break;
		  
		
		
		}
		return true;
	}

	private void loadImage(String imageUrl){
		Log.v("network", "开始解析新闻图片"+imageUrl);
		    newsImage.setImageUrl(imageUrl, new ImageLoader(rq, BitmapCache.getInstance()));
		    
		    newsImage.setDefaultImageResId(R.drawable.imgload);
		    
		    newsImage.setErrorImageResId(R.drawable.imgerror);
	}

}
