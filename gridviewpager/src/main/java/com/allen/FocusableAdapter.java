package com.allen;

import android.widget.BaseAdapter;

/**
 * Created by Allen on 2016/10/20.
 */
public abstract class FocusableAdapter extends BaseAdapter implements Focusable {
    protected int focusedId = INVALID_FOCUSED_ID;

    @Override
    public void setFocusedId(int id) {
        if(focusedId != id){
            focusedId = id;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getFocusedId() {
        return focusedId;
    }
}
