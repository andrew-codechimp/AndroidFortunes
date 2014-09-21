package org.codechimp.androidfortunes;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MobileListenerService extends WearableListenerService {

    private static final String TAG = "MobileListenerService";
    private static final String MSG_QUOTEREQUEST = "/quoterequest";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();

        if (path.equals(MSG_QUOTEREQUEST)) {
            Log.d(TAG, "Quote Request Received");

            Quote q;
            q = CloudyFortunesClient.getCloudyFortunesApiClient().randomQuote();

            NotifyHelper.sendMessageToWear(this.getBaseContext(), q.getContent());

        }
    }
}