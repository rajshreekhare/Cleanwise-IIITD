package com.zuccessful.cleanwise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class CheckNetworkReceiver_MT17010 extends BroadcastReceiver {
    AlertDialog alertDialog;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        NetworkInfo info = (NetworkInfo) extras
                .getParcelable("networkInfo");

        NetworkInfo.State state = info.getState();
        Log.d("TEST Internet", info.toString() + " "
                + state.toString());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        boolean b = true;
//        if (state == NetworkInfo.State.CONNECTED)
//            b = true;
//        else b = false;


        alertDialog = alertDialogBuilder.setTitle("Internet Connection Required")
                .setMessage("Please enable wifi or mobile data to sign-in.")
                .setPositiveButton(R.string.connect_wifi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setCancelable(b)
                .create();

        if (state == NetworkInfo.State.CONNECTED) {
//            Toast.makeText(context, "Internet connection is on", Toast.LENGTH_LONG).show();
//            alertDialog.setCancelable(true);
//            (AppCompatActivity)context
//            alertDialog.dismiss();
//            alertDialog.hide();
//            alertDialog.cancel();

        } else {
//            Toast.makeText(context, "Internet connection is Off", Toast.LENGTH_LONG).show();
//            alertDialog.setCancelable(false);
            alertDialog.show();
        }

    }
}