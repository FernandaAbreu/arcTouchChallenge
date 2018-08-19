package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

/**
 * Created by fernanda on 19/08/2018.
 */

public interface HomeView {
    void hideloading();
    void showloading();
    void showData(List<Movie> movieList);
    void pagination(long page);

    void failLoadData();
}
