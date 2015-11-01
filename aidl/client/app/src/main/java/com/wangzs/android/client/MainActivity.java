package com.wangzs.android.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.wangzs.android.aidl.IAidlInterface;
import com.wangzs.android.aidl.IAidlListener;

public class MainActivity extends Activity {

    private Button bind_btn, unbind_btn, call_server_btn, set_listener_btn;

    private IAidlInterface mAidl;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("====> [client] onServiceConnected");
            mAidl = IAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("====> [client] onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUIi();
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


    private void initUIi() {
        bind_btn = (Button) findViewById(R.id.bind_btn);
        unbind_btn = (Button) findViewById(R.id.unbind_btn);
        call_server_btn = (Button) findViewById(R.id.call_server_btn);
        set_listener_btn = (Button) findViewById(R.id.set_listener_btn);

        bind_btn.setOnClickListener(v -> {
            Logger.d("====> [client] click bind button");
            Intent intent = new Intent("com.wangzs.android.server.aidl");
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        });

        unbind_btn.setOnClickListener(v -> {
            Logger.d("====> [client] click unbind button");
            unbindService(mConnection);
        });

        call_server_btn.setOnClickListener(v -> {
            Logger.d("====> [client] click call server function button");
            try {
                mAidl.normalFunc();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        set_listener_btn.setOnClickListener(v -> {
            Logger.d("====> [client] click set listener button");
            try {
                mAidl.setAidlListener(new IAidlListener.Stub() {
                    @Override
                    public void handleX() throws RemoteException {
                        Logger.d("====> [client] server trigger client handleX");
                    }

                    @Override
                    public void handleY() throws RemoteException {
                        Logger.d("====> [client] server trigger client handleY");
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
