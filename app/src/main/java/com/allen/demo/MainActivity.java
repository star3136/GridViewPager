package com.allen.demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.allen.AdapterCreator;
import com.allen.FocusableAdapter;
import com.allen.GridViewPager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridViewPager<Integer> gridViewPager;
    private List<Integer> dataList;
    private static final int[] TEST_PICS = {
            R.drawable.test1,
            R.drawable.test2,
            R.drawable.test3,
            R.drawable.test4,
            R.drawable.test5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridViewPager = (GridViewPager<Integer>) findViewById(R.id.grid_viewpager);
        buildData();
        gridViewPager.setDataList(dataList);
        gridViewPager.setAdapterCreator(new AdapterCreator<Integer>() {
            @Override
            public FocusableAdapter createAdapter(GridView gridView, List<Integer> dataList) {
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
        for (int i = 0; i <= 100; i++) {
            dataList.add(TEST_PICS[i % TEST_PICS.length]);
        }
    }

    private class MyAdapter extends FocusableAdapter {
        private List<Integer> data;

        public MyAdapter(List<Integer> data) {
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
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_list_item, null);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
                imageView.setClickable(true);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridViewPager.setFocusedItem(position);
                    }
                });
                holder = new ViewHolder();
                holder.imageView = imageView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imageView.setImageResource(data.get(position));
            if (focusedId == position) {
                holder.imageView.setBackgroundColor(Color.RED);
            } else {
                holder.imageView.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }

        class ViewHolder {
            public ImageView imageView;
        }
    }

}
