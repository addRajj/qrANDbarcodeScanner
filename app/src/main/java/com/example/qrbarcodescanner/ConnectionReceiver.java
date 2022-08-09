package com.example.qrbarcodescanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {
    Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        if (isconnected(context)) {
            Toast.makeText(context, "internet availaible", Toast.LENGTH_SHORT).show();
        }
        else
        {
            showDialog();
        }
    }

    public void showDialog() {

        AlertDialog.Builder builder= new AlertDialog.Builder(mContext);
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.no_internet_connection ,null);
        Button button=view.findViewById(R.id.button_ok);
        builder.setView(view);
        final Dialog dialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public boolean isconnected(Context context)
    {
        try{

            ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=cm.getActiveNetworkInfo();
            return(networkInfo!=null && networkInfo.isConnected());

        }catch (NullPointerException e){

            e.printStackTrace();
            return false;
        }

    }
}
