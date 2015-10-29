package com.lxp.component.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxp.component.calendar.CalendarMonthCard.OnCellBackListener;

@SuppressLint("ClickableViewAccessibility") 
public class ScrollerMonth extends LinearLayout{

	private Context mContext;
	private LinearLayout layoutWeek;
	private RecyclerViewMonth scrollerCalendar;
	
	public ScrollerMonth(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setOrientation(VERTICAL);
		createView();
	}
	
	private void createView(){
		//创建周表头容器
		layoutWeek = new LinearLayout(mContext);
		layoutWeek.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		layoutWeek.setOrientation(HORIZONTAL);
		addView(layoutWeek);
		//创建周表头元素
		for (int i = 0; i < 7; i++){
			TextView tvWeek = new TextView(mContext);
			tvWeek.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
			tvWeek.setGravity(Gravity.CENTER);
			tvWeek.setText(DateUtil.WEEKS_NAME[i]);
			tvWeek.setTextColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK));
			layoutWeek.addView(tvWeek);
		}
		scrollerCalendar = new RecyclerViewMonth(mContext);
		addView(scrollerCalendar);
	}

	public void setCallBackListener(OnCellBackListener callBackListener){
		scrollerCalendar.setOnCellClickListener(callBackListener);
	}
	
	public void scrollToToday(){
		scrollerCalendar.scrollToToday();
	}
	
	public void scrollToToday(CustomDate day){
		scrollerCalendar.scrollToDay(day);
	}
	
	public void updateScroll(){
		scrollerCalendar.updateScroll();
	}
}
