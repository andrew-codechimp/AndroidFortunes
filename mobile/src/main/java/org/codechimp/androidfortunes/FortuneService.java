package org.codechimp.androidfortunes;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FortuneService extends IntentService {
    static final String TAG = "FortuneService";

    public static final String ACTION_USER_NOTIFICATION = "USER_NOTIFICATION";

    public static final String RESULT = "result";
    public static final String CONTENT = "content";
    public static final String NOTIFICATION = "org.codechimp.androidfortunes.service.receiver";

    private int result = Activity.RESULT_CANCELED;

    public FortuneService() {
        super(TAG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "OnHandleIntent");

        String content = "";

        try {
            Quote q = CloudyFortunesClient.getCloudyFortunesApiClient().randomQuote();

            result = Activity.RESULT_OK;
            content = q.getContent();

            if (intent.getAction().equals(ACTION_USER_NOTIFICATION)) {
                Log.d(TAG, "Notification");
                NotifyHelper.notify(this, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        publishResults(content, result);
    }

    private void publishResults(String content, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(CONTENT, content);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
