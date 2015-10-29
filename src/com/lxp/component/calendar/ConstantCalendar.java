package com.lxp.component.calendar;

import android.content.Context;

public class ConstantCalendar {
	/**
	 * 小时00:00至00:00
	 */
	public static final String[] DISPLAY_HOURS = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00"};
	/**
	 * 标题文字的颜色
	 */
	public static final String COLOR_TEXT_TITLE_BLACK = "#333333";
	/**
	 * 文字默认颜色
	 */
	public static final String COLOR_TEXT_DEFAULT = "#959595";
	/**
	 * 分割线颜色
	 */
	public static final String COLOR_LINE_DEFAULT = "#888888";
	/**
	 * 日历 今天 颜色
	 */
	public static final String COLOR_TODAY = "#50b495";
	/**
	 * 日历激活（选中）颜色
	 */
	public static final String COLOR_ACTIVE = "#333333";
	/**
	 * 矩形填充色
	 */
	public static final String COLOR_RECT = "#e8f8f2";
	/**
	 * 小点距离底边的像素
	 */
	public static final int MARGIN_BOTTOM = 14;
	/**
	 * 小点的半径
	 */
	public static final int CIRCLE_SMALL = 6;
	
	/** dip转换px */
	public static int dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}
}
