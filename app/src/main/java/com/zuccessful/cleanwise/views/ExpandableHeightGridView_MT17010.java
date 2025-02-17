package com.zuccessful.cleanwise.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;


/*
*   Reference from the solution of Neil Traft at https://stackoverflow.com/questions/4523609/grid-of-images-inside-scrollview/4536955#4536955
*   Also here: https://stackoverflow.com/questions/19242001/expandable-height-gridview-inside-scrollview
*/

public class ExpandableHeightGridView_MT17010 extends GridView {

    boolean expanded = false;

    public ExpandableHeightGridView_MT17010(Context context) {
        super(context);
    }

    public ExpandableHeightGridView_MT17010(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableHeightGridView_MT17010(Context context, AttributeSet attrs,
                                            int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
