package com.arctouch.codechallenge.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.arctouch.codechallenge.util.RecyclerViewClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Filterable {

    private List<Movie> moviesAll;
    private List<Movie> moviesFiltered;
    private List<Movie> movies;
    private static RecyclerViewClickListener listener;

    public HomeAdapter(List<Movie> movies,RecyclerViewClickListener listener) {
        this.movies = movies;
        this.moviesAll = movies;
        this.listener=listener;
    }

    public void addAll(List<Movie> results) {
        movies.addAll(results);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genresTextView = itemView.findViewById(R.id.genresTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            titleTextView.setText(movie.title);
            genresTextView.setText(TextUtils.join(", ", movie.genres));
            releaseDateTextView.setText(movie.releaseDate);

            String posterPath = movie.posterPath;
            if (!TextUtils.isEmpty(posterPath)) {
                Glide.with(itemView)
                        .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(posterImageView);
            }else{
                posterImageView.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_image_placeholder));

            }
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    movies = moviesAll;
                } else {
                    List<Movie> filteredList = new ArrayList<>();
                    for (Movie row : moviesAll) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.title.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    movies = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = movies;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movies = (ArrayList<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
