package com.example.manug.peerchat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    Context context;
    EditText ip;
    EditText port;
    EditText portText;
    Button connectButton;
    Button showIPtextId;
    String showIPaddress;
    boolean WIFI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        portText = (EditText) findViewById(R.id.myPortEditText);
        ip= (EditText) findViewById(R.id.ipEditText);
        port = (EditText) findViewById(R.id.portEditText);
        connectButton = (Button) findViewById(R.id.connectButton);
        showIPtextId = (Button)findViewById(R.id.showIPtextId);
        networkDetect();
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!WIFI){
                    showToast("Not Connected !!! Try to reconnect.");
                }
                if(ip.length()==0 || port.length()==0 || portText.length()==0){
                    if(ip.length()==0){
                        ip.setError("Receiver's ip address can't be empty");
                    }
                    if(port.length()==0){
                        port.setError("Receiver's port No. can't be empty");
                    }
                    if(portText.length()==0){
                        portText.setError("your port No. can't be empty");
                    }
                    if(ip.length()==0 && port.length()==0 && portText.length()==0){
                        String s = "All fields can't be empty!!!";
                        showToast(s);
                    }
                }
                else if(!Patterns.IP_ADDRESS.matcher(ip.getText()).matches()){
                    String s = "Enter a Valid IP Address";
                    showToast(s);
                }
                else{
                    String info = getInfo();
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("ip&port",info);
                    startActivity(intent);
                }
            }
        });


    }


    public void reconnect(View view){
        networkDetect();
    }




    void showToast(String s){
        Toast toast = new Toast(getApplicationContext());
        View view = LayoutInflater.from(this).inflate(R.layout.toast_layout, null);
        TextView toastTextView = view.findViewById(R.id.toast_error);
        toastTextView.setText(s);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);

        toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
        toast.show();
    }

    String getInfo(){

        String info = this.ip.getText().toString()+" "+this.port.getText().toString()+" "+this.portText.getText().toString();
        return info;
    }



    public void networkDetect(){
        WIFI = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) //checking if connected network is wifi
                if (netInfo.isConnected())
                    WIFI = true;
        }

        if(WIFI == true){
            showIPaddress = GetDeviceipWiFiData();
            showIPtextId.setText("IP ADDRESS : "+showIPaddress);
        }

        else{
            showIPtextId.setText("Not Connected !!!\nPress again to reconnect ");
        }

    }


    //This method is getting the wifi ip address
    public String GetDeviceipWiFiData(){
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }



}
