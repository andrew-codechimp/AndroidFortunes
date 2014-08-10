package org.codechimp.androidfortunes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.codechimp.util.WakeLock;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm Received");

        WakeLock.acquire(context);

        Quote q;
        q = CloudyFortunesClient.getCloudyFortunesApiClient().randomQuote();

        if (q != null)
            NotifyHelper.Notify(context, q.getContent());

        WakeLock.release();
    }
}
