package org.codechimp.androidfortunes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm Received");

        NotifyHelper.Notify(context, context.getString(R.string.app_name),
                "Medication now due");
    }
}
