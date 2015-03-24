package com.seedsoft.ykt.widget;

import android.graphics.drawable.Drawable;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);
    Drawable[] getIconRes(int index);
    String getIconURL(int index);
    String[] getIconURLS(int index);
    Object getIconObject(int index);

    // From PagerAdapter
    int getCount();
}
