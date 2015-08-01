package com.ultrapower.baozouqiwen.view.mainpage;


import java.lang.ref.WeakReference;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.ultrapower.baozouqiwen.MainPageActivity;
import com.ultrapower.baozouqiwen.R;
import com.ultrapower.baozouqiwen.view.mainpage.MainPageListView.OnLoadMoreListener;
import com.ultrapower.baozouqiwen.view.mainpage.MainPageListView.OnRefreshListener;



public class MainPageListListener implements OnLoadMoreListener,
		OnRefreshListener {
	private static final String TAG = "DropDownRefreshListListener";
	
	WeakReference<MainPageActivity> mainActivity;
	public MainPageListListener(MainPageActivity mainActivity) {
		
		
		this.mainActivity=new WeakReference<MainPageActivity>(mainActivity);
		View vp= mainActivity.getListViewViewPagerHeader();
		int viewPageHeight= vp.getMeasuredHeight();
		int viewPagerHideRatio= mainActivity.ViewPagerHideRatio;
		orginalParading=-viewPageHeight/viewPagerHideRatio;
		
		initAnimation(vp);
		
	}

	
	private void  initAnimation(final View v){
		
		animation=ValueAnimator.ofInt(parading,orginalParading);
        animation.setDuration(1000);
        animation.addUpdateListener(new AnimatorUpdateListener(){

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {

				int p=Integer.valueOf(animation.getAnimatedValue().toString());
               v.setPadding(0, p, 0, p);
//				animation.
				
			}} );
		animation.setInterpolator(new OvershootInterpolator());
	}
	
	@Override
	public void doRefreshData() {


		MainPageActivity mactivity=mainActivity.get();
		// TODO 下拉刷新
		Log.e(TAG, "onRefresh");
		//loadData(0);
//		mactivity.getmAdapter().refeshData();
//		mactivity.getmListView().onRefreshComplete();	//下拉刷新完成
		mactivity.getmAdapter().refeshData();
//		Log.e("afterrefreshdata",mactivity.getmAdapter().getCount()+"");
//		mactivity.getmListView().onRefreshComplete();

	}

	@Override
	public void doRefreshStateChange(
			MainPageListView dlistView) {
		MainPageActivity mactivity=mainActivity.get();
		switch (dlistView.getRefreshState()) {
		case MainPageListView.RELEASE_TO_REFRESH:
			
			/*mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);

			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mArrowAnim);
			// 松开刷新
			mTipsTextView.setText(R.string.p2refresh_release_refresh);*/

			break;
		case MainPageListView.PULL_TO_REFRESH:
			/*mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (mIsBack) {
				mIsBack = false;
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mArrowReverseAnim);
				// 下拉刷新
				mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			} else {
				// 下拉刷新
				mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			}*/
			break;

		case MainPageListView.REFRESHING:
			/*mHeadView.setPadding(0, 0, 0, 0);
			
			// 华生的建议： 实际上这个的setPadding可以用动画来代替。我没有试，但是我见过。其实有的人也用Scroller可以实现这个效果，
			// 我没时间研究了，后期再扩展，这个工作交给小伙伴你们啦~ 如果改进了记得发到我邮箱噢~
			
			
			mProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			// 正在刷新...
			mTipsTextView.setText(R.string.p2refresh_doing_head_refresh);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);*/

			break;
		case MainPageListView.REFEESHDONE:
			//回弹
			/*mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			
			// 此处可以改进，同上所述。
			
			mProgressBar.setVisibility(View.GONE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageResource(R.drawable.arrow);
			// 下拉刷新
			mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);*/

			break;
		}

	}

	private int   parading=0;
	private int   orginalParading;
	@Override
	public void doRefreshTranslationChange(
			MainPageListView dropDownRefreshListView) {


		MainPageActivity mactivity=mainActivity.get();
		View vp= mactivity.getListViewViewPagerHeader();
		int listViewTranslation=dropDownRefreshListView.getListViewTranslation();
		parading=orginalParading+listViewTranslation;
		
		if(parading>0){
			
			parading=0;
		} else if(parading<orginalParading){
			
			parading=orginalParading;
		}
		
		vp.setPadding(0, parading, 0, parading); 

	}

	@Override
	public void doLoadData() {
		// TODO Auto-generated method stub
		MainPageActivity mactivity=mainActivity.get();
		// TODO 加载更多
		Log.e(TAG, "onLoad");
		//loadData(1);
		
		mactivity.getmAdapter().getMoreData();
//		mactivity.getmListView().onLoadMoreComplete();	//加载更多完成

	}

	@Override
	public void doLoadMoreStateChange(MainPageListView listview) {
		MainPageActivity mactivity=mainActivity.get();
		switch (listview.getLoadMoreState()) {
		case MainPageListView.MORELODING://刷新中
			
			// 加载中...
			if(mactivity.getListViewLoadDataTip().getText().equals(
					R.string.p2refresh_doing_end_refresh)){
				break;
			}
			mactivity.getListViewLoadDataTip().setText(R.string.p2refresh_doing_end_refresh);
			mactivity.getListViewLoadDataTip().setVisibility(View.VISIBLE);
			mactivity.getListViewLoadDataProgressBar().setVisibility(View.VISIBLE);
			break;
		case MainPageListView.MORELODINGDONE:// 自动刷新完成
			
			// 更    多
			mactivity.getListViewLoadDataTip().setText(R.string.p2refresh_end_load_more);
			mactivity.getListViewLoadDataTip().setVisibility(View.VISIBLE);
			mactivity.getListViewLoadDataProgressBar().setVisibility(View.GONE);
			
			mactivity.getDropListViewFootView().setVisibility(View.VISIBLE);
			break;
		default:
			// 原来的代码是为了： 当所有item的高度小于ListView本身的高度时，
			// 要隐藏掉FootView，大家自己去原作者的代码参考。
			
//			if (enoughCount) {					
//				endRootView.setVisibility(View.VISIBLE);
//			} else {
//				endRootView.setVisibility(View.GONE);
//			}
			break;
		}

	}
	
	private ValueAnimator animation;

	@Override
	public void doRebound() {

		   if(parading==orginalParading)
			   return ;
           animation.setIntValues(parading,orginalParading);
           animation.start();
		
	}

	@Override
	public void doStopRebound() {


		  animation.cancel();
		
	}

}
