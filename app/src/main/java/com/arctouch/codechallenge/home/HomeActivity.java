package com.arctouch.codechallenge.home;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BaseActivity;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.detail.DetailActivity;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.InfiniteScrollListener;
import com.arctouch.codechallenge.util.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements HomeView {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private GridLayoutManager gridLayoutManager;
    private InfiniteScrollListener infiniteScrollListener;
    private static final int COLUMNS = 1;
    HomeAdapter adapter;
    HomePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);
        presenter= new HomePresenter(this,this);
        setupRecyclerView();
        setCacheFirst();

    }

    private void setCacheFirst() {
        presenter.getGender();
    }

    public void pagination(long page) {
        presenter.upcomingMovies(page);
    }

    @Override
    public void failLoadData() {
        Toast.makeText(this,getString(R.string.generic_error),Toast.LENGTH_LONG).show();
        hideloading();
    }


    public Activity getActivity(){
        return HomeActivity.this;
    }

    private void setupRecyclerView() {
        gridLayoutManager = new GridLayoutManager(this, COLUMNS);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(gridLayoutManager);

        infiniteScrollListener = new InfiniteScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("PAGINATION","PAGING"+page);
                Log.d("PAGINATION","TOTAL"+totalItemsCount);
               pagination(page);
            }
        };
        recyclerView.addOnScrollListener(infiniteScrollListener);

    }

    @Override
    public void hideloading() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showloading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<Movie> movieList) {
        RecyclerViewClickListener listener = (view, position) -> {
            Intent i= new Intent(getActivity(), DetailActivity.class);
            i.putExtra(DetailActivity.movie,movieList.get(position));
            startActivity(i);
        };
        if(adapter==null){
            adapter=new HomeAdapter(movieList,listener);
            recyclerView.setAdapter(adapter);
        }else{
            adapter.addAll(movieList);
        }
        hideloading();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

}
