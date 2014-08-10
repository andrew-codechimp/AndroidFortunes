package org.codechimp.androidfortunes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int SHOW_SETTINGS = 12;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @InjectView(R.id.content)
    TextView contentTextView;

    Quote currentQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        swipeLayout.setRefreshing(true);
        new GetRandomQuoteTask().execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initializeUI();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if (currentQuote != null)
            contentTextView.setText(currentQuote.getContent());
    }

    @Override
    public void onRefresh() {
        new GetRandomQuoteTask().execute();
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
                if (currentQuote != null)
                    NotifyHelper.Notify(this, currentQuote.getContent());
                return true;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                i.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                        GeneralUserPreferencesFragment.class.getName());
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


    private class GetRandomQuoteTask extends AsyncTask<Void, Void, Quote> {
        @Override
        protected Quote doInBackground(Void... params) {
            Quote q;
            q = CloudyFortunesClient.getCloudyFortunesApiClient().randomQuote();
            return q;
        }

        @Override
        protected void onPostExecute(Quote result) {
            swipeLayout.setRefreshing(false);

            currentQuote = result;

            contentTextView.setText(currentQuote.getContent());
        }
    }
}
