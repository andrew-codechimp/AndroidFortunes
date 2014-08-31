package org.codechimp.androidfortunes;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class WearListenerService extends WearableListenerService {

    private static final String TAG = "WearListenerService";
    private GoogleApiClient googleApiClient;

    private static final String MSG_QUOTE = "/quote";
    public static final int NOTIFICATION_ID = 10002;

    @Override
    public void onCreate() {
        super.onCreate();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the data layer API
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        googleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        byte[] data = messageEvent.getData();

        if (path.equals(MSG_QUOTE)) {
            Log.d(TAG, "Quote Received");

            String quote = new String(data);

            showNotification(this, quote);
        }
    }

    private static void showNotification(Context context, String quote) {
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText(quote);

        Notification notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(quote)
                        .setStyle(bigStyle)
                        .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);
    }
}