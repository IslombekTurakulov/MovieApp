package com.iuturakulov.movieapp.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iuturakulov.movieapp.api.model.Movie;
import com.iuturakulov.movieapp.api.ApiClient;
import com.iuturakulov.movieapp.api.ApiInterface;
import com.iuturakulov.movieapp.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<Movie.ResultsBean>> postersList;
    private MutableLiveData<List<Movie.ResultsBean>> topRatedList;
    private MutableLiveData<List<Movie.ResultsBean>> popularList;
    private MutableLiveData<List<Movie.ResultsBean>> nowPlayingList;

    public LiveData<List<Movie.ResultsBean>> getPoster() {
        if (postersList == null) {
            postersList = new MutableLiveData<List<Movie.ResultsBean>>();
            postersList.setValue(reloadMovies(Constant.CATEGORYIMAGE));
        }
        return postersList;
    }

    public LiveData<List<Movie.ResultsBean>> getTopRated() {
        if (topRatedList == null) {
            topRatedList = new MutableLiveData<List<Movie.ResultsBean>>();
            topRatedList.setValue(reloadMovies(Constant.CATEGORY_TOP_RATED));
        }
        return topRatedList;
    }

    public LiveData<List<Movie.ResultsBean>> getPopular() {
        if (popularList == null) {
            popularList = new MutableLiveData<List<Movie.ResultsBean>>();
            popularList.setValue(reloadMovies(Constant.CATEGORY_POPULAR));
        }
        return popularList;
    }

    public LiveData<List<Movie.ResultsBean>> getNowPlaying() {
        if (nowPlayingList == null) {
            nowPlayingList = new MutableLiveData<List<Movie.ResultsBean>>();
            nowPlayingList.setValue(reloadMovies(Constant.CATEGORY_NOW_PLAYING));
        }
        return nowPlayingList;
    }

    private List<Movie.ResultsBean> reloadMovies(String category) {
        List<Movie.ResultsBean> list = new ArrayList<>();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Movie> call = apiInterface.getMovies(category, Constant.API_KEY, Constant.LANGUAGE, Constant.PAGE);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                if (movie != null) {
                    List<Movie.ResultsBean> temp = movie.getResults();
                    list.addAll(temp);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.getMessage();
            }
        });
        return list;
    }
}