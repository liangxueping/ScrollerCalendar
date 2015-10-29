package com.lxp.demo.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.lxp.component.calendar.CalendarMonthCard.OnCellBackListener;
import com.lxp.component.calendar.CustomDate;
import com.lxp.component.calendar.CalendarHourCard.MyEvent;
import com.lxp.component.calendar.CalendarHourCard.OnHourCardListener;
import com.lxp.component.calendar.ScrollerMonth;
import com.lxp.component.calendar.ScrollerWeek;
import com.lxp.component.calendar.ScrollerWeek.OnScrollerWeekListener;
import com.lxp.demo.R;

public class LeftAndRightActivity extends Activity{

	private ViewFlipper viewFlipper;
	private ScrollerWeek scrollerWeek;
	private ScrollerMonth scrollerMonth;
	private TextView tvChange, tvToday;
	private boolean isShowMonth = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_left_and_right);
		tvChange = (TextView) findViewById(R.id.tv_change);
		tvToday = (TextView) findViewById(R.id.tv_today);
		
		viewFlipper = (ViewFlipper) findViewById(R.id.vf_calendar);
		
		scrollerWeek = (ScrollerWeek) findViewById(R.id.scroller_week);
		scrollerWeek.setOnScrollerWeekListener(new OnScrollerWeekListener() {
			@Override
			public void onItemClick(CustomDate date) {
				scrollerMonth.scrollToToday(date);
			}
		});
		scrollerWeek.setOnHourCardListener(new OnHourCardListener() {
			@Override
			public void onItemClickEvent(MyEvent event) {
				Toast.makeText(LeftAndRightActivity.this, "点击："+event.title, Toast.LENGTH_SHORT).show();
			}
		});
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
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(LeftAndRightActivity.this, R.anim.push_left_in));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(LeftAndRightActivity.this, R.anim.push_left_out));
					viewFlipper.showNext();
				}else {
					viewFlipper.setInAnimation(AnimationUtils.loadAnimation(LeftAndRightActivity.this, R.anim.push_right_in));
					viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(LeftAndRightActivity.this, R.anim.push_right_out));
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
