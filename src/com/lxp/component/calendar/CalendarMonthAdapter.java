package com.lxp.component.calendar;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import com.lxp.component.calendar.CalendarMonthCard.OnCellBackListener;


 


public class CalendarMonthAdapter extends RecyclerView.Adapter<CalendarMonthAdapter.ViewHolder>{
    private final TypedArray typedArray;
	private final Context context;
	private final OnCellBackListener onCellClickListener;
    private CalendarMonthCard currentItem;
	private List<CustomDate> dataList;

	public CalendarMonthAdapter(Context context, OnCellBackListener onCellClickListener, TypedArray typedArray, List<CustomDate> dataList) {
		this.dataList = dataList;
		this.typedArray = typedArray;
        this.context = context;
		this.onCellClickListener = onCellClickListener;
	}
	
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final CalendarMonthCard calendarCard = new CalendarMonthCard(context, typedArray);
        calendarCard.setShowDate(new CustomDate());
        calendarCard.setCellClickListener(onCellClickListener);
        return new ViewHolder(calendarCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        final CalendarMonthCard calendarCard = viewHolder.calendarCard;
        calendarCard.setShowDate(dataList.get(position));
        calendarCard.update();
        currentItem = calendarCard;
        if(onCellClickListener != null){
        	onCellClickListener.changeDate(dataList.get(position));
        }
    }

    public long getItemId(int position) {
		return position;
	}
    
    public CalendarMonthCard getYearView(){
    	return currentItem;
    }

    @Override
    public int getItemCount()
    {
    	if(dataList == null){
    		return 0;
    	}
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final CalendarMonthCard calendarCard;

        public ViewHolder(View itemView)
        {
            super(itemView);
            calendarCard = (CalendarMonthCard) itemView;
            calendarCard.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            //calendarCard.setClickable(true);
        }
    }
}