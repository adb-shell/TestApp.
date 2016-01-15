package com.karthik.imager.Recycler;

/**
 * Created by karthikrk on 21/12/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * A {@link RecyclerView.ItemDecoration} which draws dividers (along the right & bottom)
 * for certain {@link RecyclerView.ViewHolder} types.
 */
public class GridItemDividerDecoration extends RecyclerView.ItemDecoration {

    private final Class[] dividedClasses;
    private final float dividerSize;
    private final Paint paint;

    public GridItemDividerDecoration(Class[] dividedClasses,
                                     float dividerSize,
                                     @ColorInt int dividerColor) {
        this.dividedClasses = dividedClasses;
        this.dividerSize = dividerSize;
        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStyle(Paint.Style.FILL);
    }

    public GridItemDividerDecoration(Class[] dividedClasses,
                                     @NonNull Context context,
                                     @DimenRes int dividerSizeResId,
                                     @ColorRes int dividerColorResId) {
        this(dividedClasses,
                context.getResources().getDimensionPixelSize(dividerSizeResId),
                ContextCompat.getColor(context, dividerColorResId));
    }


    //onDraw() method draws in grid which ofcourse overlapped by grid items in adpter.


    //onDrawOver method is drawen over the child typically used to for dividers
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(child);

            if (requiresDivider(viewHolder)) {
                final int right = lm.getDecoratedRight(child);
                final int bottom = lm.getDecoratedBottom(child);
                // draw the bottom divider
                canvas.drawRect(lm.getDecoratedLeft(child),
                        bottom - dividerSize,
                        right,
                        bottom,
                        paint);
                // draw the right edge divider
                canvas.drawRect(right - dividerSize,
                        lm.getDecoratedTop(child),
                        right,
                        bottom - dividerSize,
                        paint);
            }

        }
    }

    private boolean requiresDivider(RecyclerView.ViewHolder viewHolder) {
        for (int i = 0; i < dividedClasses.length; i++) {
            if (dividedClasses[i].isInstance(viewHolder)) return true;
        }
        return false;
    }

}

