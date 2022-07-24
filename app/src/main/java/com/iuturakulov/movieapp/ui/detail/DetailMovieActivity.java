package com.iuturakulov.movieapp.ui.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iuturakulov.movieapp.R;
import com.iuturakulov.movieapp.databinding.ActivityDetailMovieBinding;
import com.iuturakulov.movieapp.api.model.Genres;
import com.iuturakulov.movieapp.api.model.Genre;
import com.iuturakulov.movieapp.utils.Constant;
import com.iuturakulov.movieapp.api.ApiInterface;
import com.iuturakulov.movieapp.api.ApiClient;
import com.iuturakulov.movieapp.utils.NetworkVerification;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieActivity extends AppCompatActivity {

    private DetailMovieViewModel detailMovieViewModel;
    private ActivityDetailMovieBinding activityDetailMovieBinding;
    private ApiInterface apiInterface;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailMovieBinding = ActivityDetailMovieBinding.inflate(getLayoutInflater());
        setContentView(activityDetailMovieBinding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Intent intent = getIntent();

        getSupportActionBar().setTitle(intent.getStringExtra("TITLE"));
        Glide.with(this).load(Constant.IMAGE_REQUEST + intent.getStringExtra("POSTER")).diskCacheStrategy(DiskCacheStrategy.DATA).into(activityDetailMovieBinding.detailMoviePoster);
        getGenre(intent.getIntArrayExtra("GENRE"));
        activityDetailMovieBinding.detailMovieOverview.setText(intent.getStringExtra("OVERVIEW"));
        activityDetailMovieBinding.detailMovieTitle.setText(String.format(" : %s", intent.getStringExtra("TITLE")));
        activityDetailMovieBinding.detailMovieTitleOri.setText(String.format(" : %s", intent.getStringExtra("ORI_TITLE")));
        activityDetailMovieBinding.detailMovieRelease.setText(String.format(" : %s", intent.getStringExtra("RELEASE")));
        activityDetailMovieBinding.detailMovieVoteCount.setText(String.format(" : %d", intent.getIntExtra("VOTE_COUNT", 0)));
        activityDetailMovieBinding.detailMoviePopular.setText(String.format(" : %s", intent.getDoubleExtra("POPULAR", 0)));
        activityDetailMovieBinding.detailMovieVoteAvg.setText(String.format(" : %d", intent.getIntExtra("VOTE_AVG", 0)));
    }

    private void getGenre(int[] genre_ids) {
        Call<Genre> genreCall = apiInterface.getGenres(Constant.API_KEY, Constant.LANGUAGE);
        genreCall.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                Genre data = response.body();
                Genres[] genres = data.getGenres();
                HashMap<Integer, String> map = new HashMap<>();
                for (Genres genre : genres) {
                    map.put(genre.getId(), genre.getName());
                }
                for (int i = 0; i < genre_ids.length; i++) {
                    String genreName = map.get(genre_ids[i]);
                    activityDetailMovieBinding.detailMovieGenre.setText(String.format(" : %s", genreName));
                }
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                t.getMessage();
            }
        });
    }
}
