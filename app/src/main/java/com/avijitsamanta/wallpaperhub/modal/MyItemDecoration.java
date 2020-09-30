package com.avijitsamanta.wallpaperhub.modal;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avijitsamanta.wallpaperhub.R;

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public MyItemDecoration(Context context) {
        this.margin = context.getResources().getDimensionPixelSize(R.dimen.image_item_margin);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }

}