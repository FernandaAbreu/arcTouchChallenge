package com.arctouch.codechallenge.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by fernanda on 18/08/2018.
 */

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private int minItemsBeforeNextLoad = 3;
    private int startingPage = 1;
    private int currentPage = 1;
    private int latestTotalItemCount = 0;
    private boolean isLoading = true;

    GridLayoutManager layoutManager;

    public InfiniteScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        minItemsBeforeNextLoad *= layoutManager.getSpanCount();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        Log.d("TOTAL",""+totalItemCount);
        Log.d("latestTotalItemCount",""+latestTotalItemCount);

        Log.d("lastVisibleItemPosition",""+lastVisibleItemPosition );
        Log.d("minItemsBeforeNextLoad",""+minItemsBeforeNextLoad );
        Log.d("Teste",""+lastVisibleItemPosition + minItemsBeforeNextLoad );
        Log.d("totalItemCount",""+totalItemCount);
        Log.d("isLoading",""+isLoading);
        // Assume list was invalidated -- set back to default
        if (totalItemCount < latestTotalItemCount) {
            this.currentPage = this.startingPage;
            this.latestTotalItemCount = totalItemCount;
        }


        // If still loading and dataset size has been updated,
        // update load state and last item count
        if (isLoading && totalItemCount > latestTotalItemCount) {
            isLoading = false;
            latestTotalItemCount = totalItemCount;
        }

        // If not loading and within threashold limit, increase current page and load more data
        if (!isLoading && (lastVisibleItemPosition + minItemsBeforeNextLoad > totalItemCount)) {
            Log.d("PAGIND","LOADING MORE");
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            isLoading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}
