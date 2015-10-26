package com.lxp.demo.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lxp.component.calendar.CalendarCard.OnCellBackListener;
import com.lxp.component.calendar.CustomDate;
import com.lxp.component.calendar.ScrollerMonth;
import com.lxp.component.calendar.ScrollerWeek;
import com.lxp.demo.R;

public class MainActivity extends Activity{

	private ViewFlipper viewFlipper;
	private ScrollerWeek scrollerWeek;
	private ScrollerMonth scrollerMonth;
	private TextView tvChange, tvToday;
	private boolean isShowMonth = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvChange = (TextView) findViewById(R.id.tv_change);
		tvToday = (TextView) findViewById(R.id.tv_today);
		
		viewFlipper = (ViewFlipper) findViewById(R.id.vf_calendar);
		
		scrollerWeek = (ScrollerWeek) findViewById(R.id.scroller_week);
		scrollerMonth = (ScrollerMonth) findViewById(R.id.scroller_month);
		scrollerMonth.setCallBackListener(new OnCellBackListener() {
			@Override
			public void clickDate(CustomDate date) {
				scrollerWeek.setDate(date.getYear(), date.getMonth(), date.getDay());
				if(!isShowMonth){
					tvChange.performClick();
				}
			}
			@Override
			public void changeDate(CustomDate date) {
				//tvCurrentMonth.setText("选择日期："+date.toString());
			}
		});
		tvChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isShowMonth){
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_left_in));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_left_out));
					viewFlipper.showNext();
				}else {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_right_in));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_right_out));
					viewFlipper.showPrevious();
				}
				isShowMonth = !isShowMonth;
			}
		});
		
		tvToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scrollerMonth.scrollToToday();
				if(isShowMonth){
					tvChange.performClick();
				}
			}
		});
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return scrollerWeek.getTouchListener().onTouchEvent(event);
	}
}
