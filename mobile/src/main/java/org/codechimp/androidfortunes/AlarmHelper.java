package org.codechimp.androidfortunes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.codechimp.util.TimePreference;

public class AlarmHelper {

    private static final String TAG = "AlarmHelper";

    public static void setDailyAlarm(Context context) {
        //Cancel any existing alarm
        cancelAlarm(context);

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);

        boolean prefDailyAlarm = sp.getBoolean(
                "prefDaily", true);

        if (prefDailyAlarm) {
            String prefTime = sp.getString("prefTime", "08:00");
            int hour = TimePreference.getHour(prefTime);
            int minute = TimePreference.getMinute(prefTime);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);  // Tomorrow
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, getPendingIntent(context.getApplicationContext()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.i(TAG, "Alarm set " + sdf.format(calendar.getTime()));
        }
    }

    public static void cancelAlarm(Context context) {
        AlarmManager mgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        mgr.cancel(getPendingIntent(context.getApplicationContext()));
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent i = new Intent(context, FortuneService.class);
        i.setAction(FortuneService.ACTION_USER_NOTIFICATION);
        return (PendingIntent.getService(context, 0, i, 0));
    }
}
