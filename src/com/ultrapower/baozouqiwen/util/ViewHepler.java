package com.ultrapower.baozouqiwen.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

public class ViewHepler {

	/**
	 * 测量HeadView宽高(注意：此方法仅适用于LinearLayout)
	 * @param pChild 
	 * @version 1.0
	 */
	public static  void measureView(View pChild) {
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}

}
