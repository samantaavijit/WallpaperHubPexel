package com.avijitsamanta.wallpaperhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijitsamanta.wallpaperhub.adopter.WallpaperAdopter;
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

import static com.avijitsamanta.wallpaperhub.MainActivity.API_KEY;
import static com.avijitsamanta.wallpaperhub.MainActivity.PARCELABLE_WALLPAPER;
import static com.avijitsamanta.wallpaperhub.adopter.CategoryAdopter.CATEGORY_NAME;

public class CategoryActivity extends AppCompatActivity implements WallpaperAdopter.WallpaperItemClick {
    private ProgressBar progressBarMiddle, progressBarBottom;
    private List<Wallpaper> list;
    private WallpaperAdopter adopter;
    private int pageNumber = new Random().nextInt(79);
    private String category = "";
    private boolean isScrolling = false;
    private int currentItem, totalItem, scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));

        setContentView(R.layout.activity_category);

        View decorView = window.getDecorView();
        // Hide Action bar and Navigation bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory);
        progressBarMiddle = findViewById(R.id.progressbarMiddle);
        progressBarBottom = findViewById(R.id.progressBarBottom);

        list = new ArrayList<>();
        adopter = new WallpaperAdopter(this, list, this);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adopter);
        recyclerView.addItemDecoration(new MyItemDecoration(this));
        recyclerView.setHasFixedSize(true);

        Intent cc = getIntent();
        if (cc != null) {
            category = cc.getStringExtra(CATEGORY_NAME);
            if (category != null) {
                progressBarMiddle.setVisibility(View.VISIBLE);
                fetchWallpaper();
            }
        }

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
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=" + category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object1 = new JSONObject(response);
                            JSONArray photosArray = object1.getJSONArray("photos");

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
                            progressBarBottom.setVisibility(View.GONE);
                            progressBarMiddle.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarBottom.setVisibility(View.GONE);
                        progressBarMiddle.setVisibility(View.GONE);
                        Toast.makeText(CategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
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
        startActivity(new Intent(CategoryActivity.this, FullScreenActivity.class)
                .putExtra(PARCELABLE_WALLPAPER, w));
    }
}