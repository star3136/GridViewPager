package com.allen;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Allen on 2016/10/20.
 */
public class GridViewPager<T extends Serializable> extends FrameLayout {
    private Context context;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private List<T> dataList;  //所有数据
    private List<List<T>> pagedDataList; //分页后的数据
    private AdapterCreator<T> adapterCreator;

    /**
     * 如果lines或columns为0则所有数据在一页内显示
     */
    private int lines = 0;
    private int columns = 0;
    private Observable observable = new Observable(){
        @Override
        public void notifyObservers(Object data) {
            setChanged();
            super.notifyObservers(data);
        }
    }; //被观察者

    public GridViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GridViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        viewPager = new CustomVeiwPager(context);
        viewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        viewPager.setOffscreenPageLimit(2);
        addView(viewPager);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridViewPager);
            lines = a.getInteger(R.styleable.GridViewPager_lines, 0);
            columns = a.getInteger(R.styleable.GridViewPager_columns, 5);
            a.recycle();
        }
    }

    public void setAdapterCreator(AdapterCreator<T> adapterCreator) {
        this.adapterCreator = adapterCreator;
    }

    public void setCurrentPage(int page){
        viewPager.setCurrentItem(page);
    }
    /**
     * 设置获得焦点的item
     * @param position
     */
    public void setFocusedItem(int position){

        if(adapter != null){
            GridViewGroup fragment = (GridViewGroup) adapter.getItem(viewPager.getCurrentItem());
            fragment.setFocusedItem(position);
            /**
             * 发出通知事件
             */
            observable.notifyObservers(viewPager.getCurrentItem());
        }
    }

    /**
     * 获取一页的项的个数
     * @return
     */
    public int getItemsPerPage() {
        return columns * lines;
    }

    /**
     * 获取第page页的第position项
     * @param page
     * @param position
     * @return
     */
    public T getItem(int page, int position){
        if(page < 0 || page >= pagedDataList.size()){
            throw new IllegalArgumentException("Invalidate page, page must between 0 and pagedDataList.size()");
        }
        if(position < 0 || position >= pagedDataList.get(page).size()){
            throw new IllegalArgumentException("Invalidate position, position must between 0 and pagedDataList.get(page).size()");
        }
        return pagedDataList.get(page).get(position);
    }

    /**
     * 获取当前页的项
     * @param position
     * @return
     */
    public T getItemInCurrentPage(int position) {
        return getItem(viewPager.getCurrentItem(), position);
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        pagedDataList = new ArrayList<>();
        if(lines == 0 || columns == 0){
            pagedDataList.add(dataList);
        }else {
            int itemsPerPage = lines * columns;
            int pages = 0;
            if(dataList.size() % itemsPerPage == 0){
                pages = dataList.size() / itemsPerPage;
            }else {
                pages = dataList.size() / itemsPerPage + 1;
            }
            for(int i = 0; i < pages; i++) {
                if(i == pages - 1) {
                    pagedDataList.add(dataList.subList(i * itemsPerPage, dataList.size()));
                }else {
                    pagedDataList.add(dataList.subList(i * itemsPerPage, (i + 1) * itemsPerPage));
                }
            }
        }
        if(viewPager != null){
            adapter = new MyPagerAdapter();
            viewPager.setAdapter(adapter);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private SparseArray<GridViewGroup<T>> fragmentMap = new SparseArray<>();
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridViewGroup<T> viewGroup = (GridViewGroup<T>) getItem(position);
            container.addView(viewGroup, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return viewGroup;
        }

        public Object getItem(int position){
            GridViewGroup<T> viewGroup = fragmentMap.get(position);
            if(viewGroup == null) {
                viewGroup = GridViewGroup.newInstance(context, pagedDataList.get(position), position, columns, adapterCreator);
                observable.addObserver(viewGroup.observer);
                fragmentMap.put(position, viewGroup);
            }
            return viewGroup;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(fragmentMap.get(position));
        }

        @Override
        public int getCount() {
            return pagedDataList != null ? pagedDataList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
