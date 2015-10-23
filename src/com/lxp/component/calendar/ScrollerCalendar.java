package com.lxp.component.calendar;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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
                    	}
                    }
            	}
            }
        });
	}
	public void scrollToToday(){
		int currentYear = DateUtil.getYear();
		int currentMonth = DateUtil.getMonth();
		for (int i = 0; i < dataList.size(); i++){
			CustomDate item = dataList.get(i);
			if(currentYear == item.year && currentMonth == item.month){
				scrollToPosition(i);
				break;
			}
		}
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
		scrollToPosition(calendarAdapter.getItemCount() / 2);
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