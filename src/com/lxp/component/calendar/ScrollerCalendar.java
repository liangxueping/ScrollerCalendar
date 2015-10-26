package com.lxp.component.calendar;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.lxp.component.calendar.CalendarCard.OnCellBackListener;

public class ScrollerCalendar extends RecyclerView
{
	private final int DATE_LENGTH = 50;
	private CalendarAdapter calendarAdapter;
    private TypedArray typedArray;
    private OnCellBackListener onCellClickListener;
	protected LinearLayoutManager layoutManager;
	private ArrayList<CustomDate> dataList;
	private int positionForToday;

    public ScrollerCalendar(Context context)
    {
        this(context, null, 0);
    }

    public ScrollerCalendar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    @SuppressLint("Recycle")
	public ScrollerCalendar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            init(context);
        }
    }
    
	public void init(Context paramContext) {
		layoutManager = new LinearLayoutManager(paramContext);
        setLayoutManager(layoutManager);
        setVerticalScrollBarEnabled(false);
		setFadingEdgeLength(0);
		setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                View firstView = getChildAt(0);
        		int top = firstView.getTop();
                if(getScrollState() == RecyclerView.SCROLL_STATE_SETTLING){
                	if(dy < 0 && dy > -20){
                    	stopScroll();
                    	smoothScrollBy(0, top < -80 ? -80 : top);
                	}
                	else if(dy > 1 && dy < 20 && top < 80){
                		top = firstView.getHeight() + top;
                    	stopScroll();
                    	smoothScrollBy(0, top > 80 ? 80 : top);
                	}
                	//Log.d("liang", "scrollY:"+top + " dy:"+dy + " scrollY/dy:"+(top/dy));
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            	super.onScrollStateChanged(recyclerView, newState);
            	if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                	int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();
                    if (lastVisibleItem >= totalItemCount - 4) {
                    	for (int i = 0; i < DATE_LENGTH; i++){
                    		dataList.add(new CustomDate(dataList.get(dataList.size() - 1).year, dataList.get(dataList.size() - 1).month+1));
                    	}
                    	calendarAdapter.notifyDataSetChanged();
                    }else if(firstVisibleItem <= 0){
                    	for (int i = 0; i < DATE_LENGTH; i++){
                    		dataList.add(0, new CustomDate(dataList.get(0).year, dataList.get(0).month-1));
                        	calendarAdapter.notifyItemInserted(0);
                        	positionForToday++;
                    	}
                    }
            	}
            }
        });
	}
	public void scrollToToday(){
		scrollToPosition(positionForToday);
		
		CalendarCard card = (CalendarCard) getChildAt(1);
		CustomDate itemDate = card.getShowDate();
		CustomDate today = new CustomDate();
		if(itemDate.year*100+itemDate.month < today.year*100+today.month){
			smoothScrollBy(0, 80);
		}
		Log.d("liang", "itemDate:"+itemDate.toString());
	}
	
    public void setOnCellClickListener(OnCellBackListener onCellClickListener)
    {
        this.onCellClickListener = onCellClickListener;
        initAdapter();
    }

	private void initAdapter() {
		if (calendarAdapter == null) {
			calendarAdapter = new CalendarAdapter(getContext(), onCellClickListener, typedArray, createData());
        }
		positionForToday = calendarAdapter.getItemCount() / 2;
		scrollToPosition(positionForToday);
		calendarAdapter.notifyDataSetChanged();
		setAdapter(calendarAdapter);
	}
	private List<CustomDate> createData(){
		dataList = new ArrayList<CustomDate>();
		for (int i = 0; i < DATE_LENGTH; i++){
			dataList.add(new CustomDate(DateUtil.getYear(), DateUtil.getMonth() + (i - DATE_LENGTH / 2)));
		}
		return dataList;
	}
}