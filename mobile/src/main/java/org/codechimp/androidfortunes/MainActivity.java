package org.codechimp.androidfortunes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int SHOW_SETTINGS = 12;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @InjectView(R.id.content)
    TextView contentTextView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String content = bundle.getString(FortuneService.CONTENT);
                int resultCode = bundle.getInt(FortuneService.RESULT);
                if (resultCode == RESULT_OK) {
                    contentTextView.setText(content);
                }
            }

            swipeLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();

        AlarmHelper.setDailyAlarm(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshQuote();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, new IntentFilter(FortuneService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initializeUI();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_main);

        // Get references to UI widgets
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar);
            } catch (Throwable t) {
                // WTF SAMSUNG 4.2.2!
            }
        }

        ButterKnife.inject(this);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    @Override
    public void onRefresh() {
        refreshQuote();
    }

    private void refreshQuote() {
        swipeLayout.setRefreshing(true);
        Intent serviceIntent = new Intent(this, FortuneService.class);
        startService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_towear:
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        NotifyHelper.sendMessageToWear(getBaseContext(), contentTextView.getText().toString());
                    }
                };
                new Thread(runnable).start();
                return true;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                i.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                        SettingsFragment.class.getName());
                i.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);

                startActivityForResult(i, SHOW_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SHOW_SETTINGS: {
                AlarmHelper.setDailyAlarm(this);
                break;
            }
        }
    }
}
