package com.allen;

import android.widget.AdapterView;
import android.widget.GridView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Allen on 2016/10/20.
 */
public interface AdapterCreator<T extends Serializable> {
    FocusableAdapter createAdapter(GridView gridView, List<T> dataList);
    AdapterView.OnItemClickListener createOnItemClickListener();
}
