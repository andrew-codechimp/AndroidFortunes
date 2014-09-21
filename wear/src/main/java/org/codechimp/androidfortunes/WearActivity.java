package org.codechimp.androidfortunes;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class WearActivity extends Activity {

    private static final String TAG = "WearActivity";
    private static final String MSG_QUOTEREQUEST = "/quoterequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread = new Thread() {
            @Override
            public void run() {
                final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getBaseContext())
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
                googleApiClient.blockingConnect();

                NodeApi.GetConnectedNodesResult nodes =
                        Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
                for (Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(
                            googleApiClient, node.getId(), MSG_QUOTEREQUEST, null).await();
                }

                googleApiClient.disconnect();

                finish();
            }
        };

        thread.start();
    }
}
