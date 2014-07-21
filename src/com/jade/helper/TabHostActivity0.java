package com.jade.helper;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.TabHost;

public class TabHostActivity0 extends Activity{
	private int flaggingWidth;    // 互动翻页所需滚动的长度是当前屏幕宽度的1/tabCount
	private int tabCount = 3;
	private GestureDetector gestureDetector; // 用户滑动
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		gestureDetector = new GestureDetector(new TabHostTouch());
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (gestureDetector.onTouchEvent(event)) {  
	        event.setAction(MotionEvent.ACTION_CANCEL);  
	    }
		return super.dispatchTouchEvent(event);
	}
	
	private class TabHostTouch extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			float flaggingWidth = dm.widthPixels / tabCount;
			int currentTabID = -1;
			TabHost mTabHost = null;
			
			if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())
					&& (e1.getX() - e2.getX() <= (-flaggingWidth) || e1.getX()
							- e2.getX() >= flaggingWidth)) {
				if (e1.getX() - e2.getX() <= (-flaggingWidth)) {
					currentTabID = mTabHost.getCurrentTab() - 1;
					if (currentTabID < 0) {
						currentTabID = tabCount - 1;
					}
					mTabHost.setCurrentTab(currentTabID);
					return true;
				} else if (e1.getX() - e2.getX() >= flaggingWidth) {
					currentTabID = mTabHost.getCurrentTab() + 1;
					if (currentTabID >= tabCount) {
						currentTabID = 0;
					}
					mTabHost.setCurrentTab(currentTabID);
					return true;
				}
			}
			return false;
		}
	}
}