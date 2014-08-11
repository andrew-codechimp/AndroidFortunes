package org.codechimp.androidfortunes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.codechimp.util.WakeLock;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm Received");

        this.context = context;
        WakeLock.acquire(context);

        new GetRandomQuoteTask().execute();

        WakeLock.release();
    }

    private class GetRandomQuoteTask extends AsyncTask<Void, Void, Quote> {
        @Override
        protected Quote doInBackground(Void... params) {
            Quote q;
            q = CloudyFortunesClient.getCloudyFortunesApiClient().randomQuote();
            return q;
        }

        @Override
        protected void onPostExecute(Quote result) {
            if (result != null)
                NotifyHelper.Notify(context, result.getContent());
        }
    }
}
