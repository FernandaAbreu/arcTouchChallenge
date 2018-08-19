package com.arctouch.codechallenge.api;

import retrofit2.Retrofit;

/**
 * Created by fernanda on 19/08/2018.
 */

public class RESTInteractor {
    TmdbApi api;
    public RESTInteractor() {
        Retrofit adapter = RestAdapterProvider.instance().getAdapter();
        api = adapter.create(TmdbApi.class);
    }

    public TmdbApi getApi(){

        return api;
    }

}
