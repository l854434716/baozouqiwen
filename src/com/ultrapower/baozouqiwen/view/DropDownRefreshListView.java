package com.ultrapower.baozouqiwen.view;

import com.ultrapower.baozouqiwen.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public abstract class DropDownRefreshListView extends ListView implements
		OnScrollListener {
	
	
	//下拉状态
	public final static byte RELEASE_TO_REFRESH = 0;//松开刷新状态
	public final static byte PULL_TO_REFRESH = 1;//下拉刷新状态
	public final static byte REFRESHING = 2; // 刷新中
	public final static byte REFEESHDONE = 3; // 刷新完成
//	public final static byte RETURNINGTOORIGINAL = 4;// 反弹回初始位置
	
	
	//加载更多状态
	
	public final static byte  MORELODING=0;
	public final static byte  MORELODINGDONE=1;
	
	
	
	// 刷新状态位和加载更多状态位
	public byte  refreshState=REFEESHDONE;
	public byte  loadMoreState=MORELODINGDONE;
	
	
	
	public byte getRefreshState() {
		return refreshState;
	}



	public byte getLoadMoreState() {
		return loadMoreState;
	}


	public  LayoutInflater mInflater;
	


	public DropDownRefreshListView(Context context) {
		super(context);
		init(context);
	}
	
	

	public DropDownRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}



	public DropDownRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}



	private void init(Context context) {

        setCacheColorHint(context.getResources().getColor(R.color.transparent));
        setOnScrollListener(this);
        mInflater= LayoutInflater.from(context);
  
	}



	
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if(scrollState==SCROLL_STATE_IDLE&&isLastItemShow){
			// need to load more data
			if(loadMoreState!=MORELODING&&refreshState!=REFRESHING){// 此时没有加载更多数据并且没有下拉刷新
				loadMoreData();
			}
		}

      
	}

	/*报告下拉刷新列表加载更多数据的状态给其他组件*/
	protected abstract void noticeListLoadMoreDataState();


	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 */
	protected abstract void loadMoreData() ;


	/**
	 * 加载更多完成 
	 */
	protected abstract void onLoadMoreComplete();


	/*表示列表目前显示在屏幕中的列情况以及listview的列总数
	 * 
	 * 
	 * */
	private int  firstVisibleItem;
	private int  lastVisibleItem;
	private int  visibleItemCount;
	private int  itemCount;
    private boolean isLastItemShow;// 是否到达listview的末端
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

         this.firstVisibleItem=firstVisibleItem;
         this.visibleItemCount=visibleItemCount;
         itemCount=totalItemCount;
         lastVisibleItem=firstVisibleItem+visibleItemCount;
         if(itemCount<=lastVisibleItem+1)
        	 isLastItemShow=true;
         else
        	 isLastItemShow=false;
         
        /* Log.d("MainPageDropRefreshListView", "firstVisibleItem=" + firstVisibleItem
 				+ ", lastVisibleItem=" + lastVisibleItem
 				+ ", visibleItemCount=" + visibleItemCount + ", itemCount="
 				+ itemCount + ", isLastItemShow=" + isLastItemShow );*/

	}



//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int expend= MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
//        
//		super.onMeasure(widthMeasureSpec, expend);
//	}



	private int  startY; //滑动list时Y的起始坐标
	private boolean  isRecoredY; //startY的值是否被记录
	private boolean  isReleaseBackToPull; //记录是否是有松开刷新状态转变为下拉刷新状态  当列表达到释放刷新状态后 用户又将列表手动推了上去相当于手动取消了刷新
	private  int RATIO=0 ;//下拉列表手指下滑距离与列表下拉位移比例
	private  int dropMaxLength=-1;//下拉列表最大的下拉距离
	private  int listViewTranslation ;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

        
		 if(loadMoreState==MORELODING||dropMaxLength==-1||RATIO==0)
		    return super.onTouchEvent(ev);
		//如果listview没有处于正在加载更多的状态那么开始捕获触摸事件
		 switch (ev.getAction()){
		 
		 case MotionEvent.ACTION_DOWN:
			 if(firstVisibleItem==0&&!isRecoredY){
				//如果在显示listview第一条记录的时候触摸listview 记下此点击的Y坐标 
				 isRecoredY=true;
				 startY=(int) ev.getY();
//			    停止回弹
			    stopRebound();
			 }
			 
			 break;
		 case MotionEvent.ACTION_MOVE:

			 int tempY = (int) ev.getY();
			 
//			 如果没有显示出header 则不执行任何下拉刷新类型的运算
			 if(firstVisibleItem!=0)
				 break;
			 
			 if(!isRecoredY) {
					isRecoredY = true;
					startY = tempY;
			 }
			 
			 if(tempY-startY<0)//直接向上推执行默认的listview 操作
				 break;
			 
			 if (refreshState == REFEESHDONE&& tempY - startY > 0) {
					refreshState = PULL_TO_REFRESH;
							
				}
					// 保证在设置padding的过程中，当前的位置一直是在head，
					// 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
			 if (refreshState == RELEASE_TO_REFRESH) {

						setSelection(0);

						if((tempY - startY) / RATIO < dropMaxLength){
							
							
							// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
							if ((tempY - startY) > 0) {
								refreshState = PULL_TO_REFRESH;
							}
							// 一下子推到顶了
							else if (tempY - startY == 0) {
								refreshState = REFEESHDONE;
							}
							// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						}else if((tempY - startY) / RATIO == dropMaxLength){
							
							
							
						}else{
							// 用户还在一直下拉 将 开始记录的位置下移，保证起始位置 和 当前用户手指位置 距离不要大于 dropmaxlength。 否则 当复原listview的时候 上滑一段距离listview才开始上移
							
							startY=tempY-dropMaxLength*RATIO;
							
						}
						
			 }
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
			if (refreshState == PULL_TO_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= dropMaxLength) {
							refreshState = RELEASE_TO_REFRESH;
							isReleaseBackToPull = true;
						} else if (tempY - startY == 0) {
							refreshState = REFEESHDONE;
						}
			}

					

					noticeListDropRefreshStateChange();
					listViewTranslation=(tempY - startY) / RATIO;
					noticeListDropTranslation(); 
					
					Log.d("refreshState", refreshState+"----"+listViewTranslation+"-----"+dropMaxLength);
				
			
			 break;
			 
		 case MotionEvent.ACTION_UP:
			 
			//当手指移开屏幕
				
			 isRecoredY = false;
			 isReleaseBackToPull = false;
			 
			 if (refreshState == PULL_TO_REFRESH) {
				refreshState = REFEESHDONE;
				noticeListDropRefreshStateChange();
						
						
			} else if (refreshState == RELEASE_TO_REFRESH) {
						refreshData();
			} else if (refreshState == REFEESHDONE){
						
						break;
			}
				

			    //开始回弹
			if(firstVisibleItem==0)
			        rebound();
				
			 
			 break;
			 
		 
		 
		 }
			 
		 
		 return super.onTouchEvent(ev);
	}


	protected abstract void stopRebound() ;



    protected abstract void rebound();


	/*报告当前下拉列表的位移量包括下拉和上推的位移量给其他组件
     *param 位移量
     *
     * */
	protected abstract void noticeListDropTranslation() ;



	/*报告当前下拉列表下拉刷新状态信息给其他组件
	 * 
	 * */
	protected abstract void noticeListDropRefreshStateChange() ;

	
	
	/**
	 * 正在下拉刷新
	 * @date 2013-11-20 下午4:45:47
	 * @change JohnWatson
	 * @version 1.0
	 */
	protected abstract void refreshData();



	
	/**
	 * 下拉刷新完成
	 * @date 2013-11-20 下午4:44:12
	 * @change JohnWatson
	 * @version 1.0
	 */
	protected abstract void onRefreshComplete();
	

	
	public void setRATIO(int rATIO) {
		RATIO = rATIO;
	}


	public void setDropLength(int dropLength) {
		this.dropMaxLength = dropLength;
	}



	public int getListViewTranslation() {
		return listViewTranslation;
	}
	
	
	

}
