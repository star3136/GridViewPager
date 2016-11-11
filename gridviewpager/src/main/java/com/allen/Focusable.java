package com.allen;

/**
 * Created by Allen on 2016/10/20.
 */
public interface Focusable {
    int INVALID_FOCUSED_ID = -1;
    void setFocusedId(int id);
    int getFocusedId();
}
