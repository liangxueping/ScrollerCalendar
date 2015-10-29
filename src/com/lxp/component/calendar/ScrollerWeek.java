package com.lxp.component.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lxp.component.calendar.CalendarHourCard.MyEvent;
import com.lxp.component.calendar.CalendarHourCard.OnHourCardListener;

@SuppressLint("ClickableViewAccessibility") 
public class ScrollerWeek extends LinearLayout implements OnGestureListener{

	private Context mContext;
	private LinearLayout layoutWeek;
	private ViewFlipper viewFlipper;
	private GridView gridView;
	private CalendarWeekAdapter dateAdapter;
	private TextView tvDate;

	private GestureDetector gestureDetector;
	private int selectPostion;
	private OnScrollerWeekListener onScrollerWeekListener;
	public void setOnScrollerWeekListener(
			OnScrollerWeekListener onScrollerWeekListener) {
		this.onScrollerWeekListener = onScrollerWeekListener;
	}
	private OnHourCardListener onHourCardListener;
	private ScrollView scrollView;
    public void setOnHourCardListener(OnHourCardListener onHourCardListener) {
		this.onHourCardListener = onHourCardListener;
	}
    
	public interface OnScrollerWeekListener {
		public abstract void onItemClick(CustomDate date);

	}
	
	public ScrollerWeek(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setOrientation(VERTICAL);
		gestureDetector = new GestureDetector(mContext, this);
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
		
		//创建周日期容器
		viewFlipper = new ViewFlipper(mContext);
		viewFlipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(viewFlipper);
		
		//创建当前日期显示元素
		tvDate = new TextView(mContext);
		LayoutParams tvDateParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tvDateParams.setMargins(0, 0, 0, 10);
		tvDate.setLayoutParams(tvDateParams);
		tvDate.setGravity(Gravity.CENTER);
		tvDate.setTextColor(Color.parseColor(ConstantCalendar.COLOR_TEXT_TITLE_BLACK));
		addView(tvDate);
		
		//创建周日期元素
		int currentYear = DateUtil.getYear();
		int currentMonth = DateUtil.getMonth();
		int currentDay = DateUtil.getCurrentMonthDay();
		tvDate.setText(currentYear + "年" + currentMonth + "月" + currentDay + "日");
		dateAdapter = new CalendarWeekAdapter(mContext, currentYear, currentMonth, currentDay);
		createGridView();
		gridView.setAdapter(dateAdapter);
		selectPostion = dateAdapter.getSelectPosition();
		viewFlipper.addView(gridView, 0);
		//创建分割线
		View lineMark = new View(mContext);
		android.view.ViewGroup.LayoutParams lineMarkParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		lineMark.setBackgroundColor(Color.parseColor(ConstantCalendar.COLOR_LINE_DEFAULT));
		lineMark.setLayoutParams(lineMarkParams);
		addView(lineMark);
		
		scrollView = new ScrollView(mContext);
		scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		scrollView.setVerticalScrollBarEnabled(false);
		addView(scrollView);
		
		CalendarHourCard hourCard = new CalendarHourCard(mContext);
		hourCard.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		hourCard.setOnHourCardListener(new OnHourCardListener() {
			@Override
			public void onItemClickEvent(MyEvent event) {
				if(onHourCardListener != null){
					onHourCardListener.onItemClickEvent(event);
				}
			}
		});
		scrollView.addView(hourCard);
	}
	
	public void setDate(int year, int month, int day){
		dateAdapter.setDate(year, month, day);
		dateAdapter.notifyDataSetChanged();
		tvDate.setText(year + "年" + month + "月" + day + "日");
	}
	
	private void createGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		gridView = new GridView(mContext);
		gridView.setNumColumns(7);
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility") @Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectPostion = position;
				dateAdapter.setSeclection(selectPostion);
				dateAdapter.notifyDataSetChanged();
				int year = dateAdapter.getCurrentYear(selectPostion);
				int month = dateAdapter.getCurrentMonth(selectPostion);
				int day = dateAdapter.getCurrentDay(selectPostion);
				tvDate.setText(year + "年" + month + "月" + day + "日");
				if(onScrollerWeekListener != null){
					onScrollerWeekListener.onItemClick(new CustomDate(year, month, day));
				}
			}
		});
		gridView.setLayoutParams(params);
	}
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		scrollView.scrollTo(0, CalendarHourCard.CELL_SPACE * 7);
	}

	
	/* **************** 滑动事件 **************** */

	public GestureDetector getTouchListener(){
		return gestureDetector;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int gvFlag = 0;
		if (e1.getX() - e2.getX() > 80) {
			// 向左滑
			createGridView();
			int[] times = DateUtil.getDay(dateAdapter.getCurrentYear(selectPostion), dateAdapter.getCurrentMonth(selectPostion), dateAdapter.getCurrentDay(selectPostion), 7);
			dateAdapter = new CalendarWeekAdapter(mContext, times[0], times[1], times[2]);
			gridView.setAdapter(dateAdapter);
			tvDate.setText(dateAdapter.getCurrentYear(selectPostion) + "年"
					+ dateAdapter.getCurrentMonth(selectPostion) + "月"
					+ dateAdapter.getCurrentDay(selectPostion) + "日");
			gvFlag++;
			viewFlipper.addView(gridView, gvFlag);
			//设置由左向左移动的进入动画
			AnimationSet animationSetLeftIn = new AnimationSet(true);
			TranslateAnimation translateAnimationLeftIn = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			translateAnimationLeftIn.setDuration(500);
			AlphaAnimation alphaAnimationLeftIn = new AlphaAnimation(0.1f, 1.0f);
			alphaAnimationLeftIn.setDuration(500);
			animationSetLeftIn.addAnimation(translateAnimationLeftIn);
			animationSetLeftIn.addAnimation(alphaAnimationLeftIn);
			//设置由左向左移动的走出动画
			AnimationSet animationSetLeftOut = new AnimationSet(true);
			TranslateAnimation translateAnimationLeftOut = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			translateAnimationLeftOut.setDuration(500);
			AlphaAnimation alphaAnimationLeftOut = new AlphaAnimation(1.0f, 0.1f);
			alphaAnimationLeftOut.setDuration(500);
			animationSetLeftOut.addAnimation(translateAnimationLeftOut);
			animationSetLeftOut.addAnimation(alphaAnimationLeftOut);
			
			this.viewFlipper.setInAnimation(animationSetLeftIn);
			this.viewFlipper.setOutAnimation(animationSetLeftOut);
			this.viewFlipper.showNext();
			viewFlipper.removeViewAt(0);
			return true;

		} else if (e1.getX() - e2.getX() < -80) {
			createGridView();
			int[] times = DateUtil.getDay(dateAdapter.getCurrentYear(selectPostion), dateAdapter.getCurrentMonth(selectPostion), dateAdapter.getCurrentDay(selectPostion), -7);
			dateAdapter = new CalendarWeekAdapter(mContext, times[0], times[1], times[2]);
			gridView.setAdapter(dateAdapter);
			tvDate.setText(dateAdapter.getCurrentYear(selectPostion) + "年"
					+ dateAdapter.getCurrentMonth(selectPostion) + "月"
					+ dateAdapter.getCurrentDay(selectPostion) + "日");
			gvFlag++;
			viewFlipper.addView(gridView, gvFlag);
			
			//设置由左向右移动的进入动画
			AnimationSet animationSetRightIn = new AnimationSet(true);
			TranslateAnimation translateAnimationRightIn = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, -1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			translateAnimationRightIn.setDuration(500);
			AlphaAnimation alphaAnimationRightIn = new AlphaAnimation(0.1f, 1.0f);
			alphaAnimationRightIn.setDuration(500);
			animationSetRightIn.addAnimation(translateAnimationRightIn);
			animationSetRightIn.addAnimation(alphaAnimationRightIn);
			//设置由左向右移动的走出动画
			AnimationSet animationSetRightOut = new AnimationSet(true);
			TranslateAnimation translateAnimationRightOut = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			translateAnimationRightOut.setDuration(500);
			AlphaAnimation alphaAnimationRightOut = new AlphaAnimation(1.0f, 0.1f);
			alphaAnimationRightOut.setDuration(500);
			animationSetRightOut.addAnimation(translateAnimationRightOut);
			animationSetRightOut.addAnimation(alphaAnimationRightOut);
			
			this.viewFlipper.setInAnimation(animationSetRightIn);
			this.viewFlipper.setOutAnimation(animationSetRightOut);
			
			this.viewFlipper.showPrevious();
			viewFlipper.removeViewAt(0);
			return true;
			// }
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
