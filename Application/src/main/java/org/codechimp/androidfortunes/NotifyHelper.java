package org.codechimp.androidfortunes;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.support.v4.app.NotificationCompat.Builder;

public class NotifyHelper {
    public static final int NOTIFICATION_ID = 10002;
    public static void Notify(Context context, String quote) {

        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText(quote);

        Notification notificationBuilder =
                new Builder(context)
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
