package com.allen.demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.allen.AdapterCreator;
import com.allen.FocusableAdapter;
import com.allen.GridViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridViewPager<String> gridViewPager;
    private List<String> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridViewPager = (GridViewPager<String>) findViewById(R.id.grid_viewpager);
        buildData();
        gridViewPager.setDataList(dataList);
        gridViewPager.setAdapterCreator(new AdapterCreator<String>() {
            @Override
            public FocusableAdapter createAdapter(GridView gridView, List<String> dataList) {
                return new MyAdapter(dataList);
            }

            @Override
            public AdapterView.OnItemClickListener createOnItemClickListener() {
                return null;
            }
        });
    }

    private void buildData() {
        dataList = new ArrayList<>();
        int i = 1;
        for (; i <= 100; i++) {
            dataList.add("data " + i);
        }
    }

    private class MyAdapter extends FocusableAdapter {
        private List<String> data;

        public MyAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data != null ? data.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return data == null ? null : data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, null);
                TextView textView = ((TextView) convertView.findViewById(android.R.id.text1));
                textView.setClickable(true);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridViewPager.setFocusedItem(position);
                    }
                });
                holder = new ViewHolder();
                holder.textView = textView;
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView = ((TextView) convertView.findViewById(android.R.id.text1));
            holder.textView.setText(data.get(position));
            if(focusedId == position){
                holder.textView.setBackgroundColor(Color.RED);
            }else {
                holder.textView.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }

        class ViewHolder{
            public TextView textView;
        }
    }

}
