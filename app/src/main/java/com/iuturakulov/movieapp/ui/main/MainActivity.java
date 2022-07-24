package com.iuturakulov.movieapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denzcoskun.imageslider.models.SlideModel;
import com.iuturakulov.movieapp.R;
import com.iuturakulov.movieapp.ui.adapter.NowPlayingAdapter;
import com.iuturakulov.movieapp.ui.adapter.TopRatedAdapter;
import com.iuturakulov.movieapp.ui.adapter.PopularAdapter;
import com.iuturakulov.movieapp.databinding.ActivityMainBinding;
import com.iuturakulov.movieapp.api.model.Movie;
import com.iuturakulov.movieapp.utils.Constant;
import com.iuturakulov.movieapp.utils.NetworkVerification;
import com.iuturakulov.movieapp.api.ApiInterface;
import com.iuturakulov.movieapp.api.ApiClient;

import android.content.res.Resources;
import android.content.res.Configuration;

import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private ApiInterface apiInterface;
    private TopRatedAdapter topRatedAdapter;
    private PopularAdapter popularAdapter;
    private NowPlayingAdapter nowPlayingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolbarHome);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_home);

        // Set Flag layout No limit (full frame layout)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        activityMainBinding.languageButton.setOnClickListener(lang -> {
            if (Constant.LANGUAGE.equals("en-EN")) {
                Constant.LANGUAGE = "ru-RU";
            } else {
                Constant.LANGUAGE = "en-EN";
            }
            Locale locale = new Locale(Constant.LANGUAGE.split("-")[0]);
            // Locale.setDefault(locale);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            recreate();
        });
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (NetworkVerification.isNetworkAvailable(this)) {
            getPoster();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        mainViewModel.getTopRated().observe(this, resultsBeans -> {
            if (NetworkVerification.isNetworkAvailable(this)) {
                topRatedAdapter = new TopRatedAdapter(this, resultsBeans);
                activityMainBinding.topRatedMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                activityMainBinding.topRatedMovies.setAdapter(topRatedAdapter);
                Toast.makeText(this, "GET TOP RATED " + resultsBeans.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        mainViewModel.getPopular().observe(this, resultsBeans -> {
            if (NetworkVerification.isNetworkAvailable(this)) {
                popularAdapter = new PopularAdapter(this, resultsBeans);
                activityMainBinding.popularMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                activityMainBinding.popularMovies.setAdapter(popularAdapter);
                Toast.makeText(this, "GET POPULAR " + resultsBeans.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        mainViewModel.getNowPlaying().observe(this, resultsBeans -> {
            if (NetworkVerification.isNetworkAvailable(this)) {
                nowPlayingAdapter = new NowPlayingAdapter(this, resultsBeans);
                activityMainBinding.nowPlayingMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                activityMainBinding.nowPlayingMovies.setAdapter(nowPlayingAdapter);
                Toast.makeText(this, "NOW PLAYING " + resultsBeans.size(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPoster() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Movie> call = apiInterface.getMovies(Constant.CATEGORYIMAGE, Constant.API_KEY, Constant.LANGUAGE, Constant.PAGE);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                if (response.body().getTotalResults() != 0) {
                    List<Movie.ResultsBean> list = movie.getResults();
                    // Carousel Library
                    List<SlideModel> Slider = new ArrayList<>();
                    for (int i = 0; i < movie.getResults().size(); i++) {
                        Movie.ResultsBean movieList = list.get(i);
                        Slider.add(new SlideModel(Constant.IMAGE_REQUEST + movieList.getBackdropPath()));
                    }
                    activityMainBinding.posterSlider.setImageList(Slider, true);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.getMessage();
            }
        });
    }
}
