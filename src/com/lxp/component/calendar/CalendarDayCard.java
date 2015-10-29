package com.lxp.component.calendar; 
  
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

  
/** 
 * 自定义小时卡
 */
public class CalendarDayCard extends View { 
	
    private Paint mCirclePaint; // 绘制圆形的画笔 
    private Paint mTextPaint; // 绘制文本的画笔 
	private boolean isSelection;
	private int mCellSpace;
	private CustomDate date;
	private CustomDate today;
    
    public CalendarDayCard(Context context) { 
        super(context); 
        init(context); 
    }
    
    public CalendarDayCard(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        init(context); 
    } 
    public CalendarDayCard(Context context, TypedArray attrs) { 
        super(context); 
        init(context); 
    } 

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	mCellSpace = MeasureSpec.getSize(widthMeasureSpec);
    	setMeasuredDimension(mCellSpace, mCellSpace);
    };
    
    private void init(Context context) { 
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); 
        
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL); 
        mCirclePaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTextPaint.setTextSize(mCellSpace/3);
    }
    public void setData(CustomDate date, boolean isSelection){
        today = new CustomDate();
    	this.date = date;
    	this.isSelection = isSelection;
    	invalidate();
    }
  
    @Override
    protected void onDraw(Canvas canvas) { 
        super.onDraw(canvas);
        if(date == null){
        	return;
        }
        if(date.toString().equals(today.toString())){
        	
        }else {
            mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK));
    	}
        if(isSelection){
            if(date.toString().equals(today.toString())){
                mTextPaint.setColor(Color.WHITE);
                mCirclePaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
            }else {
                mTextPaint.setColor(Color.WHITE);
                mCirclePaint.setColor(Color.parseColor(ConstantCalendar.COLOR_ACTIVE));
            }
            canvas.drawCircle((float)(mCellSpace * 0.5), (float)(mCellSpace * 0.5), mCellSpace / 3, mCirclePaint);
    	}else {
    		if(date.toString().equals(today.toString())){
                mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
    		}else {
                mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK));
    		}
    	}
        String content = date.day+"";
        float startX = (float) (0.5 * mCellSpace - mTextPaint.measureText(content) / 2);
        float startY = (float) (0.7 * mCellSpace - mTextPaint.measureText(content, 0, 1) / 2);
        canvas.drawText(content, startX, startY, mTextPaint);
        
        if(date.day % 29 == 0){
            mCirclePaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
            canvas.drawCircle((float)(mCellSpace * 0.5), (float)(mCellSpace * 1 - ConstantCalendar.MARGIN_BOTTOM), ConstantCalendar.CIRCLE_SMALL, mCirclePaint);
        }
    }
}