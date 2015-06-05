package com.dejamobile.ardeco_library;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dejamobile.ardeco.lib.IServiceEntryPoint;
import com.dejamobile.ardeco.lib.ServiceEntryPoint;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = BuildConfig.LOG_ADB_KEY;

    private static IServiceEntryPoint entryPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, ServiceEntryPoint.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            entryPoint = IServiceEntryPoint.Stub.asInterface(service);
            String libraryVersion;
            try {
                // Library Call is here for demo purpose
                libraryVersion = entryPoint.getVersion();
                Log.d(TAG, "Library version is : " + libraryVersion);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            entryPoint = null;
        }
    };
}