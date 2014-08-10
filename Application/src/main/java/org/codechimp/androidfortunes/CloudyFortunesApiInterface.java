package org.codechimp.androidfortunes;

import retrofit.http.GET;

public interface CloudyFortunesApiInterface {
    @GET("/quotes/random")
    Quote randomQuote();
}
