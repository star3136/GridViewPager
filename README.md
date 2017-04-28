# GridViewPager
带翻页的GridView

#### 布局文件
```xml
<!-- 3行4列  水平、垂直间隔为2dp-->
<com.allen.GridViewPager
        android:id="@+id/grid_viewpager"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:horizontal_spacing="2dp"
        app:vertical_spacing="2dp"
        app:rows="3"
        app:columns="4" />
```

#### 代码
```java
gridViewPager = (GridViewPager<Integer>) findViewById(R.id.grid_viewpager);
gridViewPager.setDataList(dataList);  //dataList是总得数据
gridViewPager.setAdapterCreator(new AdapterCreator<Integer>() {
            @Override
            public FocusableAdapter createAdapter(GridView gridView, List<Integer> dataList) {
			 //这里的dataList是按照行数、列数分割后得到的
                return new MyAdapter(dataList);   
            }

            @Override
            public AdapterView.OnItemClickListener createOnItemClickListener() {
				//点击事件
                return null;
            }
        });


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
```