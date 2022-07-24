package com.iuturakulov.movieapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iuturakulov.movieapp.R;
import com.iuturakulov.movieapp.api.model.Movie;
import com.iuturakulov.movieapp.ui.detail.DetailMovieActivity;
import com.iuturakulov.movieapp.utils.Constant;

import java.util.List;

public class TopRatedAdapter extends RecyclerView.Adapter<TopRatedAdapter.ViewHolder> {

    private final Context context;
    private final List<Movie.ResultsBean> mData;

    public TopRatedAdapter(Context context, List<Movie.ResultsBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.list_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie.ResultsBean results = mData.get(position);
        String image = results.getPosterPath();
        Glide.with(context).load(Constant.IMAGE_REQUEST + image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.poster);
        String title = results.getTitle();
        String poster = results.getBackdropPath();
        String overview = results.getOverview();
        int[] genreId = results.getGenre_ids();
        String release = results.getReleaseDate();
        String ori_title = results.getOriginalTitle();
        String ori_lang = results.getOriginalLanguage();
        int vote_count = results.getVoteCount();
        int vote_avg = (int) results.getVoteAverage();
        Double popular = results.getPopularity();
        holder.release.setText(context.getResources().getText(R.string.release_date) + " " + release);
        holder.cardMovie.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailMovieActivity.class);
            intent.putExtra("TITLE", title);
            intent.putExtra("POSTER", poster);
            intent.putExtra("OVERVIEW", overview);
            intent.putExtra("GENRE", genreId);
            intent.putExtra("RELEASE", release);
            intent.putExtra("ORI_TITLE", ori_title);
            intent.putExtra("TITLE", title);
            intent.putExtra("ORI_LANG", ori_lang);
            intent.putExtra("VOTE_COUNT", vote_count);
            intent.putExtra("VOTE_AVG", vote_avg);
            intent.putExtra("POPULAR", popular);
            context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView poster;
        private final TextView release;
        private final LinearLayout cardMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            cardMovie = itemView.findViewById(R.id.card_view_movie);
            release = itemView.findViewById(R.id.movie_release);
        }
    }
}
