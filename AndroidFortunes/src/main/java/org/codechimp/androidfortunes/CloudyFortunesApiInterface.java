package org.codechimp.androidfortunes;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface CloudyFortunesApiInterface {
    @GET("/quotes/random")
    Quote randomQuote();
}
