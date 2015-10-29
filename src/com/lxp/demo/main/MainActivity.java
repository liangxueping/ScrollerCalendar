package com.lxp.demo.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lxp.component.calendar.CalendarHourCard.MyEvent;
import com.lxp.component.calendar.CalendarHourCard.OnHourCardListener;
import com.lxp.component.calendar.CalendarMonthCard.OnCellBackListener;
import com.lxp.component.calendar.CustomDate;
import com.lxp.component.calendar.ScrollerMonth;
import com.lxp.component.calendar.ScrollerWeek;
import com.lxp.component.calendar.ScrollerWeek.OnScrollerWeekListener;
import com.lxp.demo.R;

public class MainActivity extends Activity{

	private ScrollerWeek scrollerWeek;
	private ScrollerMonth scrollerMonth;
	private TextView tvChange, tvToday;
	private boolean isShowMonth = true;
	private Animation animaHide;
	private Animation animaShow;
	private static final int ANIMA_DURATION = 500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvChange = (TextView) findViewById(R.id.tv_change);
		tvToday = (TextView) findViewById(R.id.tv_today);
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
				Toast.makeText(MainActivity.this, "点击："+event.title, Toast.LENGTH_SHORT).show();
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
				showCalendar();
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
		initAnimation();
	}

	private void initAnimation(){
		animaHide = new AlphaAnimation(1, 0); 
		animaHide.setDuration(ANIMA_DURATION);//设置动画持续时间 
		
		animaShow = new AlphaAnimation(0, 1); 
		animaShow.setDuration(ANIMA_DURATION);//设置动画持续时间 
	}
	
	private void showCalendar(){
		if(isShowMonth){
			animaHide.setStartOffset(0);
			scrollerWeek.startAnimation(animaHide);
			animaShow.setStartOffset(ANIMA_DURATION);
			animaShow.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					scrollerWeek.setVisibility(View.GONE);
					scrollerMonth.setVisibility(View.VISIBLE);
					scrollerMonth.updateScroll();
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				@Override
				public void onAnimationEnd(Animation animation) {}
			});
			scrollerMonth.startAnimation(animaShow);
		}else {
			animaHide.setStartOffset(0);
			scrollerMonth.startAnimation(animaHide);
			animaShow.setStartOffset(ANIMA_DURATION);
			animaShow.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					scrollerMonth.setVisibility(View.GONE);
					scrollerWeek.setVisibility(View.VISIBLE);
					}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				@Override
				public void onAnimationEnd(Animation animation) {}
			});
			scrollerWeek.startAnimation(animaShow);
		}
		isShowMonth = !isShowMonth;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return scrollerWeek.getTouchListener().onTouchEvent(event);
	}
}
