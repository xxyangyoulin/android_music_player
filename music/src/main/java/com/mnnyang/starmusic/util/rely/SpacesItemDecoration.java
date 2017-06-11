package com.mnnyang.starmusic.util.rely;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
            //注释这两行是为了上下间距相同
            int position = parent.getChildAdapterPosition(view);
            if (position == 0 || position == 1) {
                outRect.top = space * 2;
            }

            if (position % 2 == 0) {
                outRect.right = space / 2;
            } else {
                outRect.left = space / 2;
            }
        }
    }