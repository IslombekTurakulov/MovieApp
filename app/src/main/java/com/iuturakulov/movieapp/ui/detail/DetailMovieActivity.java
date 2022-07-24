package com.iuturakulov.movieapp.ui.detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iuturakulov.movieapp.R;
import com.iuturakulov.movieapp.databinding.ActivityDetailMovieBinding;
import com.iuturakulov.movieapp.api.model.Genres;
import com.iuturakulov.movieapp.utils.Constant;

import java.util.HashMap;
import java.util.Objects;

public class DetailMovieActivity extends AppCompatActivity {

    private DetailMovieViewModel detailMovieViewModel;
    private ActivityDetailMovieBinding activityDetailMovieBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        activityDetailMovieBinding = ActivityDetailMovieBinding.inflate(getLayoutInflater());
        setSupportActionBar(activityDetailMovieBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Get data form intent
        Intent intent = getIntent();
        // Set Image and title
        getSupportActionBar().setTitle(activityDetailMovieBinding.detailMovieTitle.getText());
        Glide.with(this).load(Constant.IMAGE_REQUEST + intent.getStringExtra("POSTER")).diskCacheStrategy(DiskCacheStrategy.DATA).into(activityDetailMovieBinding.detailMoviePoster);
        detailMovieViewModel = ViewModelProviders.of(this).get(DetailMovieViewModel.class);
        int[] genreIds = intent.getIntArrayExtra("GENRE");
        detailMovieViewModel.getGenres().observe(this, new Observer<Genres[]>() {
            @Override
            public void onChanged(Genres[] genres) {
                HashMap<Integer, String> map = new HashMap<>();
                for (Genres genre : genres) {
                    map.put(genre.getId(), genre.getName());
                }
                for (int genre_id : genreIds) {
                    String genreName = map.get(genre_id);
                    activityDetailMovieBinding.detailMovieGenre.setText(String.format(" : %s", intent.getIntArrayExtra("GENRE")));
                }
            }
        });
        activityDetailMovieBinding.detailMovieOverview.setText(intent.getStringExtra("OVERVIEW"));
        activityDetailMovieBinding.detailMovieTitle.setText(String.format(" : %s", intent.getStringExtra("TITLE")));
        activityDetailMovieBinding.detailMovieTitleOri.setText(String.format(" : %s", intent.getStringExtra("ORI_TITLE")));
        activityDetailMovieBinding.detailMovieRelease.setText(String.format(" : %s", intent.getStringExtra("RELEASE")));
        activityDetailMovieBinding.detailMovieVoteCount.setText(String.format(" : %d", intent.getIntExtra("VOTE_COUNT", 0)));
        activityDetailMovieBinding.detailMoviePopular.setText(String.format(" : %s", intent.getDoubleExtra("POPULAR", 0)));
        activityDetailMovieBinding.detailMovieVoteAvg.setText(String.format(" : %d", intent.getIntExtra("VOTE_AVG", 0)));
    }
}
