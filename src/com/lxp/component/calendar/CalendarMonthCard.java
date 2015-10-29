package com.lxp.component.calendar; 
  
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

  
/** 
 * 自定义日历卡 
 */
public class CalendarMonthCard extends View { 
  
    public static CustomDate currentClickDate;
	private int TOTAL_COL = 7; // 7列 
    private int TOTAL_ROW = 7; // 7行 
  
    private Paint mCirclePaint; // 绘制圆形的画笔 
    private Paint mTextPaint; // 绘制文本的画笔 
    private int mViewWidth; // 视图的宽度 
    private int mViewHeight; // 视图的高度 
    private int mCellSpace; // 单元格间距 
    private Row rows[] = new Row[TOTAL_ROW]; // 行数组，每个元素代表一行 
    private CustomDate mShowDate; // 自定义的日期，包括year,month,day 
    private OnCellBackListener mCellClickListener; // 单元格点击回调事件 
    private int touchSlop; // 
  
    private Cell mClickCell; 
    private float mDownX; 
    private float mDownY; 
  
    public interface OnCellBackListener { 
        void clickDate(CustomDate date); // 回调点击的日期 
        void changeDate(CustomDate date);
    } 
    
    public CalendarMonthCard(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        init(context); 
    } 
    public CalendarMonthCard(Context context, TypedArray attrs) { 
        super(context); 
        init(context); 
    } 
    
    public void setCellClickListener(OnCellBackListener listener){
        this.mCellClickListener = listener;
    }
    public void setShowDate(CustomDate showDate){
    	mShowDate = showDate;
    }
    public CustomDate getShowDate(){
    	return mShowDate;
    }
  
    private void init(Context context) { 
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); 
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG); 
        mCirclePaint.setStyle(Paint.Style.FILL); 
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop(); 
    } 
  
    private void initDate() { 
    	if(mShowDate == null){
    		mShowDate = new CustomDate(); 
    	}
        fillDate();
    } 
  
    private void fillDate() {
        int monthDay = DateUtil.getCurrentMonthDay(); // 今天 
//        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1); // 上个月的天数 
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month); // 当前月的天数 
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month); 
        boolean isCurrentMonth = false; 
        if (DateUtil.isCurrentMonth(mShowDate)) { 
            isCurrentMonth = true; 
        } 
        int day = 0; 
        for (int j = 0; j < TOTAL_ROW; j++) { 
            rows[j] = new Row(j); 
            if(j == 0){
            	continue;
            }
            for (int i = 0; i < TOTAL_COL; i++) { 
                int position = i + (j-1) * TOTAL_COL + 1; // 单元格位置 
                // 这个月的 
                if (position >= firstDayWeek 
                        && position < firstDayWeek + currentMonthDays) { 
                    day++; 
                    rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day), State.CURRENT_MONTH_DAY, i, j); 
                    // 今天 
                    if (isCurrentMonth && day == monthDay ) { 
                        CustomDate date = CustomDate.modifiDayForObject(mShowDate, day); 
                        rows[j].cells[i] = new Cell(date, State.TODAY, i, j); 
                    } 
  
                    if (isCurrentMonth && day > monthDay) { // 如果比这个月的今天要大，表示还没到 
                        rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day), State.UNREACH_DAY, i, j); 
                    } 
                } 
//                else if (position < firstDayWeek) { 
//                	// 过去一个月 
//                    rows[j].cells[i] = new Cell(new CustomDate(mShowDate.year, mShowDate.month - 1, lastMonthDays - (firstDayWeek - position - 1)), State.PAST_MONTH_DAY, i, j); 
//                } 
//                else if (position >= firstDayWeek + currentMonthDays) { 
//                	// 下个月 
//                    rows[j].cells[i] = new Cell((new CustomDate(mShowDate.year, mShowDate.month + 1, position - firstDayWeek - currentMonthDays + 1)), State.NEXT_MONTH_DAY, i, j); 
//                }
            } 
        }
    } 
  
    @Override
    protected void onDraw(Canvas canvas) { 
        super.onDraw(canvas); 
        initDate();
        for (int i = 0; i < TOTAL_ROW; i++) { 
            if (rows[i] != null) { 
                rows[i].drawCells(canvas); 
            } 
        }
    } 
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec); 
        mCellSpace = mViewWidth / TOTAL_COL;
        mViewHeight = measureHeight(heightMeasureSpec)+5; 
    	setMeasuredDimension(mViewWidth, mViewHeight);
    };
    
    private int measureHeight(int measureSpec) {  
        int result = 0;  
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month); // 当前月的天数 
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month); 
        int rowCount = (currentMonthDays+firstDayWeek-1)/7+1;
        if((currentMonthDays+firstDayWeek-1)%7 != 0){
        	rowCount++;
        }
        result = mCellSpace*rowCount;
        return result;  
    }
  
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh); 
        mViewWidth = w; 
        mViewHeight = h+5;
        mCellSpace = mViewWidth / TOTAL_COL; 
        mTextPaint.setTextSize(mCellSpace / 3); 
    } 
  
    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent event) { 
        switch (event.getAction()) { 
        case MotionEvent.ACTION_DOWN: 
            mDownX = event.getX(); 
            mDownY = event.getY();
            break; 
        case MotionEvent.ACTION_UP: 
            float disX = event.getX() - mDownX; 
            float disY = event.getY() - mDownY; 
            if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) { 
                int col = (int) (mDownX / mCellSpace); 
                int row = (int) (mDownY / mCellSpace); 
                measureClickCell(col, row); 
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
    private void measureClickCell(int col, int row) { 
        if (col >= TOTAL_COL || row >= TOTAL_ROW) 
            return; 
        if (mClickCell != null) { 
            rows[mClickCell.j].cells[mClickCell.i] = mClickCell; 
        } 
        if (rows[row] != null && rows[row].cells[col] != null) { 
            mClickCell = new Cell(rows[row].cells[col].date, rows[row].cells[col].state, rows[row].cells[col].i, rows[row].cells[col].j); 
            CustomDate date = rows[row].cells[col].date; 
            date.week = col; 
            if(mCellClickListener != null){
            	mCellClickListener.clickDate(date); 
            }
            CalendarMonthCard.currentClickDate = date;
            invalidate();
        }
    }
    /** 
     * 组元素 
     */
    class Row { 
        public int j; 
  
        Row(int j) { 
            this.j = j; 
        } 
  
        public Cell[] cells = new Cell[TOTAL_COL]; 
  
        // 绘制单元格 
        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) { 
                if (cells[i] != null) { 
                    cells[i].drawSelf(canvas); 
                } 
            }
        } 
  
    } 
  
    /** 
     * 单元格元素 
     */
    class Cell { 
        public CustomDate date; 
        public State state; 
        public int i; 
        public int j; 
  
        public Cell(CustomDate date, State state, int i, int j) { 
            super(); 
            this.date = date; 
            this.state = state; 
            this.i = i; 
            this.j = j; 
        } 
  
        public void drawSelf(Canvas canvas) {
        	boolean isShow = true;
            switch (state) { 
            case TODAY: // 今天 
                mTextPaint.setColor(Color.WHITE); 
                mCirclePaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
                canvas.drawCircle((float) (mCellSpace * (i + 0.5)), (float) ((j + 0.5) * mCellSpace), mCellSpace / 3, mCirclePaint);
                break; 
            case CURRENT_MONTH_DAY: // 当前月日期 
                mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK)); 
                break; 
            case UNREACH_DAY: // 还未到的天 
                mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK)); 
                break; 
            case PAST_MONTH_DAY: // 过去一个月 
            case NEXT_MONTH_DAY: // 下一个月 
                mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK));
                isShow = false;
                break; 
            }
            if(isShow){
                // 绘制文字 
                String content = date.day + ""; 
                canvas.drawText(content, 
                        (float) ((i + 0.5) * mCellSpace - mTextPaint.measureText(content) / 2), 
                        (float) ((j + 0.7) * mCellSpace - mTextPaint.measureText(content, 0, 1) / 2), 
                        mTextPaint);

                if(j == 1 && date.day == 1){
                    //绘制日历年月
                    mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
                    //绘制文字 
                    String month = date.year + "年" + date.month + "月";
                    float startX = (float) ((i + 0.5) * mCellSpace - mTextPaint.measureText(month) / 2);
                    float startY = (float) ((0 + 0.7) * mCellSpace - mTextPaint.measureText(month, 0, 1) / 2);
                    if((i + 0.5) * mCellSpace + mTextPaint.measureText(month) > mViewWidth){
                    	startX = mViewWidth - mTextPaint.measureText(month) - 5;
                    }else if(startX < 5){
                    	startX = 5;
                    }
                    canvas.drawText(month, startX, startY, mTextPaint);
                    mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_LINE_DEFAULT));
                    canvas.drawLine(mCellSpace*i, mCellSpace, mViewWidth, mCellSpace, mTextPaint);
                }
                mTextPaint.setColor(Color.parseColor(ConstantCalendar.COLOR_LINE_DEFAULT));
                canvas.drawLine(mCellSpace*i, mCellSpace*j+mCellSpace, mCellSpace*(i+1), mCellSpace*j+mCellSpace, mTextPaint);

                //绘制数据小点
                if(date.day % 29 == 0){
                    mCirclePaint.setColor(Color.parseColor(ConstantCalendar.COLOR_TODAY));
                    canvas.drawCircle((float) (mCellSpace * (i + 0.5)), (float) ((j + 1) * mCellSpace - ConstantCalendar.MARGIN_BOTTOM), ConstantCalendar.CIRCLE_SMALL, mCirclePaint); 
                }
                //绘制被点击的天数
                if(CalendarMonthCard.currentClickDate != null && CalendarMonthCard.currentClickDate.toString().equals(date.toString())){
                    
                    mCirclePaint.setColor(Color.parseColor(ConstantCalendar.COLOR_ACTIVE));
                    canvas.drawCircle((float) (mCellSpace * (i + 0.5)), (float) ((j + 0.5) * mCellSpace), mCellSpace / 3, mCirclePaint);
                    
                    mTextPaint.setColor(Color.WHITE); 
                    canvas.drawText(content, 
                            (float) ((i + 0.5) * mCellSpace - mTextPaint.measureText(content) / 2), 
                            (float) ((j + 0.7) * mCellSpace - mTextPaint.measureText(content, 0, 1) / 2), 
                            mTextPaint);
                    
                    CalendarMonthCard.currentClickDate = null;
                }
            }
        }
    } 
  
    /** 
     * 单元格的状态 当前月日期，过去的月的日期，下个月的日期 
     */
    enum State { 
        TODAY,CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, UNREACH_DAY; 
    } 
    public void update() { 
        fillDate();
        invalidate(); 
    } 
  
}