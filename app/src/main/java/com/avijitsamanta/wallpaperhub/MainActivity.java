package com.avijitsamanta.wallpaperhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijitsamanta.wallpaperhub.adopter.CategoryAdopter;
import com.avijitsamanta.wallpaperhub.adopter.WallpaperAdopter;
import com.avijitsamanta.wallpaperhub.modal.Category;
import com.avijitsamanta.wallpaperhub.modal.MyItemDecoration;
import com.avijitsamanta.wallpaperhub.modal.Wallpaper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements WallpaperAdopter.WallpaperItemClick {

    private ProgressBar progressBarMiddle, progressBarBottom;
    private List<Wallpaper> list;
    private WallpaperAdopter adopter;
    public static final String API_KEY = "563492ad6f917000010000014d7bce7014c34390b071660b7efb8423";
    private int pageNumber = new Random().nextInt(79);
    private boolean isScrolling = false;
    private int currentItem, totalItem, scrollOutItems;
    public static final String PARCELABLE_WALLPAPER = "wal";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_main);




        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        progressBarMiddle = findViewById(R.id.progressbarMiddle);
        progressBarBottom = findViewById(R.id.progressBarBottom);


        final ViewPager viewPager = findViewById(R.id.viewPager);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("https://1.bp.blogspot.com/-LHlD5Kj86FE/X3Cu9laK3zI/AAAAAAAAHbI/eyotG1BWnXEDj_UbsLO38nT13xOGO3LtACLcBGAsYHQ/s320/nature.jpg", "Nature"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-tUJRJyTremI/X3Cu-w6ihXI/AAAAAAAAHbU/Q6rTLHZFpHkl7OxXB78d-z9USCnWdQFXACLcBGAsYHQ/s320/wallpaper.jpg", "Wallpaper"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-eXqMUT6_Ls8/X3Cu7XW2fOI/AAAAAAAAHbA/nWfH8ShccowSZAy91EYYAuXtbQ0yWaUOgCLcBGAsYHQ/s1600/animal.jpg", "Animal"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-yhN-VzTYsws/X3Cu6jFvMNI/AAAAAAAAHa4/TkrzJYoI6v0-vzJ_E5J8yYryzD9wRJlrACLcBGAsYHQ/s320/bird.jpg", "Bird"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-pcEn8Jds6_c/X3HIyhSpljI/AAAAAAAAHcE/HB65AHFRNcEfczaAAD6Ry4a2lRMeeCTlQCLcBGAsYHQ/s320/house.jpg", "House"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-vk2c254mFgA/X3Cu8qblsVI/AAAAAAAAHbE/t_rmeZtSHr4WjYJHKOAk10lj5Hi-VGv8wCLcBGAsYHQ/s320/flower.jpg", "Flower"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-fHhNg2Vd674/X3Cu9o21t3I/AAAAAAAAHbM/q3_cssyHyjUv7jo2zN5nnl8Avr588U3xwCLcBGAsYHQ/s320/pattern.jpg", "Pattern"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-fiOBYFJVBM0/X3Cu-UYGrMI/AAAAAAAAHbQ/RWsfZ3Cqb3oaajC21ew0NMQhIx5XU6zLwCLcBGAsYHQ/s320/sky.jpg", "Sky"));
        categoryList.add(new Category("https://1.bp.blogspot.com/-YlV12tCnqzc/X3Cu_HpWD6I/AAAAAAAAHbY/4Ei6dGb-nzobEILMTQAknS4zEs7ENkWtQCLcBGAsYHQ/s320/texture.jpg", "Texture"));
        CategoryAdopter categoryAdopter = new CategoryAdopter(categoryList, this);
        viewPager.setAdapter(categoryAdopter);
        // get width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final int width = displayMetrics.widthPixels;
        // set middle of the screen - 50
        viewPager.setPadding(width / 2 - 50, 5, 150, 10);
        // set page margin
        viewPager.setPageMargin(20);


        list = new ArrayList<>();
        adopter = new WallpaperAdopter(this, list, this);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adopter);
        recyclerView.addItemDecoration(new MyItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        progressBarMiddle.setVisibility(View.VISIBLE);
        fetchWallpaper();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItem = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItem + scrollOutItems == totalItem)) {
                    isScrolling = false;
                    progressBarBottom.setVisibility(View.VISIBLE);
                    fetchWallpaper();
                }

            }
        });

    }

    private void fetchWallpaper() {
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/curated/?page=" + pageNumber + "&per_page=80",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray photosArray = jsonObject.getJSONArray("photos");

                            for (int i = 0; i < photosArray.length(); i++) {
                                JSONObject object = photosArray.getJSONObject(i);
                                int id = object.getInt("id");
                                String photographerName = object.getString("photographer");
                                String photographerUrl = object.getString("photographer_url");

                                // it get the src object
                                JSONObject objectImages = object.getJSONObject("src");

                                String originalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                Wallpaper wallpaper = new Wallpaper(id, photographerName, photographerUrl, originalUrl, mediumUrl);
                                list.add(wallpaper);
                            }

                            adopter.notifyDataSetChanged();
                            pageNumber++;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            progressBarMiddle.setVisibility(View.GONE);
                            progressBarBottom.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarMiddle.setVisibility(View.GONE);
                        progressBarBottom.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            // it is Authorization the developer
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", API_KEY);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public void onWallpaperClick(int position, Wallpaper w) {
        Intent intent = new Intent(MainActivity.this, FullScreenActivity.class);
        intent.putExtra(PARCELABLE_WALLPAPER, w);
        startActivity(intent);

    }
}