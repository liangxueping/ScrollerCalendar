package com.lxp.component.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CalendarWeekAdapter extends BaseAdapter {
	private int dayOfWeek = 0; // 具体某一天是星期几
	private Context context;
	private CustomDate[] dayNumber = new CustomDate[7];
	// 系统当前时间
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	private int selectPosition = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		changeDate(position - selectPosition);
		selectPosition = position;
	}
	public void setDate(int year, int month, int day){
		currentYear = year;
		currentMonth = month;
		currentDay = day;
		initWeek();
	}
	private void changeDate(int dayNum){
		int[] times = DateUtil.getDay(currentYear, currentMonth, currentDay, dayNum);
		currentYear = times[0];
		currentMonth = times[1];
		currentDay = times[2];
	}

	public CalendarWeekAdapter(Context context, int year, int month, int day) {
		this.context = context;
		setDate(year, month, day);
	}
	
	private void initWeek(){
		dayOfWeek = DateUtil.getWeekDayFromDate(currentYear, currentMonth, currentDay);
		//初始化一周内的日期（从星期一开始）
		for (int i = 0; i < 7; i++){
			int tempDay = currentDay - dayOfWeek + 1 + i;
			int day = 0;
			if(tempDay < 1){
				day = DateUtil.getMonthDays(currentYear, currentMonth-1) + tempDay;
				dayNumber[i] = new CustomDate(currentYear, currentMonth-1, day);
			}else if(tempDay > DateUtil.getMonthDays(currentYear, currentMonth)){
				day = tempDay - DateUtil.getMonthDays(currentYear, currentMonth);
				dayNumber[i] = new CustomDate(currentYear, currentMonth+1, day);
			}else {
				day = tempDay;
				dayNumber[i] = new CustomDate(currentYear, currentMonth, day);
			}
		}
		//初始化选中位置
		selectPosition = dayOfWeek - 1;
	}

	public int getSelectPosition() {
		return selectPosition;
	}

	public int getCurrentMonth(int position) {
		return currentMonth;

	}

	public int getCurrentYear(int position) {
		return currentYear;
	}

	public int getCurrentDay(int position) {
		return currentDay;
	}

	/**
	 * 某一天在第几周
	 */
	public void getDayInWeek(int year, int month) {

	}

	@Override
	public int getCount() {
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		return dayNumber[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CalendarDayCard card = new CalendarDayCard(context);
		card.setData(dayNumber[position], selectPosition == position);
		return card;
	}

}
