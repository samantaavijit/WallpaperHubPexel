package com.avijitsamanta.wallpaperhub.adopter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avijitsamanta.wallpaperhub.R;
import com.avijitsamanta.wallpaperhub.modal.Wallpaper;
import com.bumptech.glide.Glide;

import java.util.List;

public class WallpaperAdopter extends RecyclerView.Adapter<WallpaperAdopter.MyViewHolder> {

    private Context context;
    private List<Wallpaper> list;
    private WallpaperItemClick click;

    public WallpaperAdopter(Context context, List<Wallpaper> list, WallpaperItemClick click) {
        this.context = context;
        this.list = list;
        this.click = click;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Wallpaper wallpaper = list.get(position);

        Glide.with(context)
                .asBitmap()
                .load(wallpaper.getMediumUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onWallpaperClick(position, wallpaper);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewItem);

        }
    }

    public interface WallpaperItemClick {
        void onWallpaperClick(int position, Wallpaper w);
    }
}
