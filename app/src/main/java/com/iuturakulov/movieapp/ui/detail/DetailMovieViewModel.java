package com.iuturakulov.movieapp.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iuturakulov.movieapp.api.model.Genre;
import com.iuturakulov.movieapp.api.model.Genres;
import com.iuturakulov.movieapp.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.iuturakulov.movieapp.api.ApiClient;
import com.iuturakulov.movieapp.api.ApiInterface;

public class DetailMovieViewModel extends ViewModel {

    private MutableLiveData<Genres[]> genreList;

    private void reloadGenre() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Genre> genreCall = apiInterface.getGenres(Constant.API_KEY, Constant.LANGUAGE);
        genreCall.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                Genre data = response.body();
                genreList.setValue(data.getGenres());
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                t.getMessage();
                genreList.setValue(null);
            }
        });
    }

    public LiveData<Genres[]> getGenres() {
        if (genreList == null) {
            genreList = new MutableLiveData<Genres[]>();
            reloadGenre();
        }
        return genreList;
    }
}
