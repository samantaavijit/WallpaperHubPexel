package com.avijitsamanta.wallpaperhub.adopter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.avijitsamanta.wallpaperhub.CategoryActivity;
import com.avijitsamanta.wallpaperhub.R;
import com.avijitsamanta.wallpaperhub.modal.Category;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdopter extends PagerAdapter {
    private List<Category> list;
    private Context context;
    public static final String CATEGORY_NAME = "category";

    public CategoryAdopter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.category_item, container, false);
        ImageView imageView = view.findViewById(R.id.categoryImageItem);
        TextView textView = view.findViewById(R.id.categoryTitle);
        MaterialCardView parent = view.findViewById(R.id.categoryContainer);

        Glide.with(context).asBitmap().load(list.get(position).getImgUrl()).into(imageView);
        textView.setText(list.get(position).getTitle());
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CategoryActivity.class)
                        .putExtra(CATEGORY_NAME, list.get(position).getTitle().toLowerCase()));
            }
        });
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
