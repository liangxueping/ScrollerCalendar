package com.lxp.component.calendar;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import com.lxp.component.calendar.CalendarCard.OnCellBackListener;


 


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{
    private final TypedArray typedArray;
	private final Context context;
	private final OnCellBackListener onCellClickListener;
    private CalendarCard currentItem;
	private List<CustomDate> dataList;

	public CalendarAdapter(Context context, OnCellBackListener onCellClickListener, TypedArray typedArray, List<CustomDate> dataList) {
		this.dataList = dataList;
		this.typedArray = typedArray;
        this.context = context;
		this.onCellClickListener = onCellClickListener;
	}
	
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final CalendarCard calendarCard = new CalendarCard(context, typedArray);
        calendarCard.setShowDate(new CustomDate());
        calendarCard.setCellClickListener(onCellClickListener);
        return new ViewHolder(calendarCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        final CalendarCard calendarCard = viewHolder.calendarCard;
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
    
    public CalendarCard getYearView(){
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
        final CalendarCard calendarCard;

        public ViewHolder(View itemView)
        {
            super(itemView);
            calendarCard = (CalendarCard) itemView;
            calendarCard.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            //calendarCard.setClickable(true);
        }
    }
}