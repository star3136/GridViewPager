package com.allen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Allen on 2016/10/20.
 */
public class GridViewGroup<T extends Serializable> extends FrameLayout {
    private GridView gridView;
    private List<T> dataList;
    private int columns;
    private int index;
    private AdapterCreator adapterCreator;
    private int focusedItem = Focusable.INVALID_FOCUSED_ID;  //用于fragment被销毁后，重建时可以恢复原来的focusedItem


    Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object data) {
            int position = (int) data;
            Log.d("LEE", "update..." + index);
            /**
             * 如果不是当前页，并且这页的焦点是有效的，则需要去除焦点
             */
            if (position != index) {
                if (((FocusableAdapter) gridView.getAdapter()).getFocusedId() != Focusable.INVALID_FOCUSED_ID) {
                    Log.d("LEE", "clear..." + index);
                    ((FocusableAdapter) gridView.getAdapter()).setFocusedId(Focusable.INVALID_FOCUSED_ID);
                }
                focusedItem = Focusable.INVALID_FOCUSED_ID;
            }
        }
    };

    public static <T> GridViewGroup newInstance(Context context, List<T> dataList, int index, int columns, AdapterCreator adapterCreator){
        GridViewGroup view = new GridViewGroup(context);
        view.init(dataList, index, columns);
        view.setAdapterCreator(adapterCreator);
        return view;
    }

    public GridViewGroup(Context context) {
        super(context);
    }

    public GridViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(List<T> dataList, int index, int columns){
        this.dataList = dataList;
        this.index = index;
        this.columns = columns;

        gridView = new CustomGridView(getContext());
        gridView.setGravity(Gravity.CENTER);
        gridView.setNumColumns(columns);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOverScrollMode(AbsListView.OVER_SCROLL_NEVER);

        gridView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(gridView);
        if(adapterCreator != null){
            setAdapter();
        }
    }



    public void setAdapterCreator(AdapterCreator adapterCreator) {
        this.adapterCreator = adapterCreator;
        if (adapterCreator != null && gridView != null) {
            setAdapter();
        }
    }

    private void setAdapter(){
        FocusableAdapter adapter = adapterCreator.createAdapter(gridView, dataList);
        if(focusedItem != Focusable.INVALID_FOCUSED_ID){
            adapter.setFocusedId(focusedItem);
        }
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(adapterCreator.createOnItemClickListener());
    }

    public void setFocusedItem(int position) {
        if (gridView != null) {
            ((FocusableAdapter) gridView.getAdapter()).setFocusedId(position);
        }
    }
}
