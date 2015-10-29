package com.lxp.component.calendar; 
  
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

  
/** 
 * 自定义小时卡
 */
public class CalendarHourCard extends View { 
	
    private Paint mRectPaint; // 绘制圆形的画笔 
    private Paint mRectBorderPaint; // 绘制文本的画笔 
	private float mRectBorderWidth = 5;//边框宽度
    private Paint mTextPaint; // 绘制文本的画笔 
    public static final int CELL_SPACE = 180; // 单元格间距 
    private int SPACE_BORDER = 30; //空白间距
    private int touchSlop; // 
    private float mDownY;
	private ArrayList<MyEvent> dataList;
	//文字起始坐标
	private int textStartX;
	//横线起始坐标
	private float lineStartX;
	private OnHourCardListener onHourCardListener;
    public void setOnHourCardListener(OnHourCardListener onHourCardListener) {
		this.onHourCardListener = onHourCardListener;
	}
	public interface OnHourCardListener { 
        void onItemClickEvent(MyEvent event);
    } 
    public CalendarHourCard(Context context) { 
        super(context); 
        init(context); 
    }
    
    public CalendarHourCard(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        init(context); 
    } 
    public CalendarHourCard(Context context, TypedArray attrs) { 
        super(context); 
        init(context); 
    } 

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), CELL_SPACE*(ConstantCalendar.DISPLAY_HOURS.length-1)+SPACE_BORDER*2);
    };
    
    private void init(Context context) { 
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); 
        mTextPaint.setTextSize(CELL_SPACE / 4);
        
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL); 
        mRectPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_RECT));
        
        mRectBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectBorderPaint.setStyle(Paint.Style.FILL); 
        mRectBorderPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
        
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop(); 
    } 
  
    private void initDate() { 
        dataList = new ArrayList<MyEvent>();
        MyEvent event1 = new MyEvent();
        event1.id = "1";
        event1.type = 1;
        event1.date = "20151027";
        event1.title = "测试";
        event1.classNum = "GWY151415";
        event1.site = "海淀大钟寺校区437教师";
        event1.cause = "1";
        event1.startHour = 8;
        event1.startMinute = 0;
        event1.endHour = 10;
        event1.endMinute = 20;
        dataList.add(event1);
    } 
    
    private boolean hasEvent(int index){
    	boolean isHas = false;
    	for (MyEvent event : dataList){
    		if(event.startHour <= index && event.endHour >= index){
                isHas = true;
                break;
    		}
    	}
    	return isHas;
    }
    @Override
    protected void onDraw(Canvas canvas) { 
        super.onDraw(canvas); 
        initDate();
    	textStartX = SPACE_BORDER;
    	lineStartX = mTextPaint.measureText(ConstantCalendar.DISPLAY_HOURS[0]) + SPACE_BORDER * 2;
        for (int i = 0; i < ConstantCalendar.DISPLAY_HOURS.length; i++){
        	float textStartY = i * CELL_SPACE + SPACE_BORDER + mTextPaint.measureText(ConstantCalendar.DISPLAY_HOURS[i], 0, 1);
        	float lineStartY = i * CELL_SPACE + SPACE_BORDER + mTextPaint.measureText(ConstantCalendar.DISPLAY_HOURS[i], 0, 1) / 2 - 5;
        	if(hasEvent(i)){
        		mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
        	}else {
        		mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_DEFAULT));
        	}
            canvas.drawText(ConstantCalendar.DISPLAY_HOURS[i], textStartX, textStartY, mTextPaint);
            canvas.drawLine(lineStartX, lineStartY, getWidth(), lineStartY, mTextPaint);
        }
        
        for (int i = 0; i < dataList.size(); i++){
        	MyEvent event = dataList.get(i);
        	float startH = event.startHour * CELL_SPACE + SPACE_BORDER + mTextPaint.measureText(ConstantCalendar.DISPLAY_HOURS[i], 0, 1) / 2 - 5;
        	float startM = event.startMinute == 0 ? mRectBorderWidth : event.startMinute * CELL_SPACE / 60;
        	float endH = event.endHour * CELL_SPACE + SPACE_BORDER + mTextPaint.measureText(ConstantCalendar.DISPLAY_HOURS[i], 0, 1) / 2 - 5;
        	float endM = event.endMinute == 0 ? -mRectBorderWidth : event.endMinute * CELL_SPACE / 60;
        	float startY = startH + startM;
        	float endY = endH + endM;
            int alpha = (int)(255*0.8);
        	mRectPaint.setAlpha(alpha);
        	//矩形
        	//左、上、右、下
            canvas.drawRect(lineStartX, startY, getWidth(), endY, mRectPaint); 
            //矩形左边框
            mRectBorderPaint.setStrokeWidth(mRectBorderWidth);
            canvas.drawLine(lineStartX, startY, lineStartX, endY, mRectBorderPaint);
            float textSpace = mRectBorderWidth*5;
            //添加事件 标题 内容文字
            String title = "" + event.title;
            mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK));
            mTextPaint.setTextSize(CELL_SPACE / 3);
            float startTitleY = startY + textSpace + mTextPaint.measureText(title, 0, 1);
            canvas.drawText(event.title, lineStartX + SPACE_BORDER + mRectBorderWidth, startTitleY, mTextPaint);
            //添加事件 班号 内容文字
            String classNum = "班号：" + event.classNum;
            mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_DEFAULT));
            mTextPaint.setTextSize(CELL_SPACE / 4);
            float startClassNumY = startTitleY + textSpace + mTextPaint.measureText(classNum, 0, 1);
            canvas.drawText(classNum, lineStartX + SPACE_BORDER + mRectBorderWidth, startClassNumY, mTextPaint);
            //添加事件 地址 内容文字
            String site = "地址：" + event.site;
            mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_DEFAULT));
            mTextPaint.setTextSize(CELL_SPACE / 4);
            float startSiteY = startClassNumY + textSpace + mTextPaint.measureText(site, 0, 1);
            canvas.drawText(site, lineStartX + SPACE_BORDER + mRectBorderWidth, startSiteY, mTextPaint);
            //添加事件 课次 内容文字
            String cause = "课次：" + event.cause;
            mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_DEFAULT));
            mTextPaint.setTextSize(CELL_SPACE / 4);
            float startCauseY = startSiteY + textSpace + mTextPaint.measureText(cause, 0, 1);
            canvas.drawText(cause, lineStartX + SPACE_BORDER + mRectBorderWidth, startCauseY, mTextPaint);
        }
    }
    
    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent event) { 
        switch (event.getAction()) { 
        case MotionEvent.ACTION_DOWN: 
            mDownY = event.getY(); 
            break; 
        case MotionEvent.ACTION_UP: 
            float disY = event.getY() - mDownY; 
            if (Math.abs(disY) < touchSlop) { 
                int hour = (int) (mDownY / CELL_SPACE);
                int minute = (int) (mDownY % CELL_SPACE / (CELL_SPACE / 60));
                measureClickCell(hour, minute); 
            }
            break; 
        default: 
            break; 
        } 
  
        return true; 
    } 
  
    /** 
     * 计算点击的单元格 
     * @param col 
     * @param row 
     */
    private void measureClickCell(int hour, int minute) { 
		int clickTime = hour*100+minute;
    	for (MyEvent event : dataList){
    		if(event.startHour*100+event.startMinute <= clickTime && event.endHour*100+event.endMinute >= clickTime){
    			if(onHourCardListener != null){
    				onHourCardListener.onItemClickEvent(event);
    			}
    		}
    	}
    	Log.d("liang", "clickTime:"+clickTime);
    } 
    public class MyEvent {
		/*唯一标识*/
		public String id;
		/*事件类型,有三种,第一种为上课*/
		public int type;
		/*事件日期,YYYYMMDD,如：20150801*/
		public String date;
		/*事件标题*/
		public String title;
	    /*开始小时*/
		public int startHour;
	    /*开始分钟*/
		public int startMinute;
	    /*结束分钟*/
		public int endMinute;
		/*结束小时,没有此参数则默认为startHour+1*/
		public int endHour;
	    /*班号*/
		public String classNum;
	    /*地点*/
		public String site;
	    /*课次*/
		public String cause;
	}
}