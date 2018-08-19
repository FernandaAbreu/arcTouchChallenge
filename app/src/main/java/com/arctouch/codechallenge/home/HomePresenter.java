package com.arctouch.codechallenge.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.arctouch.codechallenge.api.RESTInteractor;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.detail.DetailActivity;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.RecyclerViewClickListener;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fernanda on 19/08/2018.
 */

public class HomePresenter {


    Context mcontext;
    HomeView view;
    RESTInteractor interactor= new RESTInteractor();
    TmdbApi api;
    public HomePresenter(Context context, HomeView view) {
        this.mcontext=context;
        this.view=view;
      api = interactor.getApi();
    }
    public void getGender(){
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                    view.pagination(1L);
                },error ->{
                    view.failLoadData();

                });

    }
    public void upcomingMovies(Long page){
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (Movie movie : response.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }
                    view.showData(response.results);
                },error ->{
                    view.failLoadData();

                });
    }

}
