package com.arctouch.codechallenge.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.DateUtil;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static String movie = "movie";
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.year)
    TextView year;
   MovieImageUrlBuilder  movieImageUrlBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieImageUrlBuilder= new MovieImageUrlBuilder();
        setupScreen();
    }

    private void setupScreen() {

        Intent intent = getIntent();
        Movie objmovie = (Movie)intent.getSerializableExtra(movie);

        String path=movieImageUrlBuilder.buildBackdropUrl(objmovie.backdropPath);
        Glide.with(this)
                .load(path)
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(image);
        year.setText(DateUtil.getDate(objmovie.releaseDate));
        String stringgenre="";
        for (Genre genre:objmovie.genres){
            if(!TextUtils.isEmpty(stringgenre)){
                stringgenre += ",";
            }
            stringgenre += genre.name;

        }
        if(TextUtils.isEmpty(stringgenre))
            stringgenre= getString(R.string.without_genre);
        duration.setText(stringgenre);
        description.setText(objmovie.overview);
        setTitle(objmovie.title);
        toolbar.setTitle(objmovie.title);
    }
}
