package com.avijitsamanta.wallpaperhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avijitsamanta.wallpaperhub.modal.Wallpaper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.card.MaterialCardView;

import static com.avijitsamanta.wallpaperhub.MainActivity.PARCELABLE_WALLPAPER;

public class FullScreenActivity extends AppCompatActivity {
    private PhotoView photoView;
    private ProgressBar progressBar;
    private View decorView;
    public static final int REQUEST_CODE = 101;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_full_screen);

        decorView = window.getDecorView();


        photoView = findViewById(R.id.photoView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        MaterialCardView download = findViewById(R.id.download);

        Intent intent = getIntent();
        if (intent != null) {
            final Wallpaper wallpaper = intent.getParcelableExtra(PARCELABLE_WALLPAPER);

            if (wallpaper != null) {
                Glide.with(this).asBitmap().load(wallpaper.getMediumUrl()).into(photoView);
                url = wallpaper.getOriginalUrl();
                Glide.with(this)
                        .asBitmap()
                        .load(wallpaper.getOriginalUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Glide.with(FullScreenActivity.this)
                                        .load(resource).into(photoView);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FullScreenActivity.this, "Original size", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(FullScreenActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) + (ContextCompat.checkSelfPermission(FullScreenActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(FullScreenActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                        } else
                            downloadImage();
                    }
                });
            }
        }
    }

    private void downloadImage() {
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        manager.enqueue(request);
        Toast.makeText(this, "Download start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int per : grantResults) {
                    if (per != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Some permission Not granted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                downloadImage();
            }
        }
    }
}