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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lxp.demo.R;

@SuppressLint("ClickableViewAccessibility") 
public class ScrollerWeek extends LinearLayout implements OnGestureListener{

	private Context mContext;
	private LinearLayout layoutWeek;
	private ViewFlipper viewFlipper;
	private GridView gridView;
	private WeekAdapter dateAdapter;
	private TextView tvDate;

	private GestureDetector gestureDetector;
	private int selectPostion;
	
	
	
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
			tvWeek.setText(DateUtil.weekName[i]);
			layoutWeek.addView(tvWeek);
		}
		
		//创建周日期容器
		viewFlipper = new ViewFlipper(mContext);
		viewFlipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(viewFlipper);
		
		//创建当前日期显示元素
		tvDate = new TextView(mContext);
		tvDate.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tvDate.setGravity(Gravity.CENTER);
		addView(tvDate);
		
		//创建周日期元素
		int currentYear = DateUtil.getYear();
		int currentMonth = DateUtil.getMonth();
		int currentDay = DateUtil.getCurrentMonthDay();
		tvDate.setText(currentYear + "年" + currentMonth + "月" + currentDay + "日");
		dateAdapter = new WeekAdapter(mContext, currentYear, currentMonth, currentDay);
		createGridView();
		gridView.setAdapter(dateAdapter);
		selectPostion = dateAdapter.getSelectPosition();
		viewFlipper.addView(gridView, 0);
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
				tvDate.setText(dateAdapter.getCurrentYear(selectPostion) + "年"
						+ dateAdapter.getCurrentMonth(selectPostion) + "月"
						+ dateAdapter.getCurrentDay(selectPostion) + "日");
			}
		});
		gridView.setLayoutParams(params);
	}
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
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
			dateAdapter = new WeekAdapter(mContext, times[0], times[1], times[2]);
			gridView.setAdapter(dateAdapter);
			tvDate.setText(dateAdapter.getCurrentYear(selectPostion) + "年"
					+ dateAdapter.getCurrentMonth(selectPostion) + "月"
					+ dateAdapter.getCurrentDay(selectPostion) + "日");
			gvFlag++;
			viewFlipper.addView(gridView, gvFlag);
			this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
			this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_out));
			this.viewFlipper.showNext();
			viewFlipper.removeViewAt(0);
			return true;

		} else if (e1.getX() - e2.getX() < -80) {
			createGridView();
			int[] times = DateUtil.getDay(dateAdapter.getCurrentYear(selectPostion), dateAdapter.getCurrentMonth(selectPostion), dateAdapter.getCurrentDay(selectPostion), -7);
			dateAdapter = new WeekAdapter(mContext, times[0], times[1], times[2]);
			gridView.setAdapter(dateAdapter);
			tvDate.setText(dateAdapter.getCurrentYear(selectPostion) + "年"
					+ dateAdapter.getCurrentMonth(selectPostion) + "月"
					+ dateAdapter.getCurrentDay(selectPostion) + "日");
			gvFlag++;
			viewFlipper.addView(gridView, gvFlag);
			this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_in));
			this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_right_out));
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
