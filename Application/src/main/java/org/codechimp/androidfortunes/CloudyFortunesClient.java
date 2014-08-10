package org.codechimp.androidfortunes;

import retrofit.RestAdapter;

public class CloudyFortunesClient {
    private static CloudyFortunesApiInterface CloudyFortunesService;

    public static CloudyFortunesApiInterface getCloudyFortunesApiClient() {
        if (CloudyFortunesService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://cloudyfortunes.appspot.com/_ah/api/cloudyfortunes/v1")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            CloudyFortunesService = restAdapter.create(CloudyFortunesApiInterface.class);
        }

        return CloudyFortunesService;
    }
}
