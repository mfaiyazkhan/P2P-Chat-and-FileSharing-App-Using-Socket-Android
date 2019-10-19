package com.example.manug.peerchat;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static com.example.manug.peerchat.ChatActivity.mAdapter;
public class Server extends Thread {

    String bgColorCode;
    ChatActivity activity;
    ListView messageList;
    int bgselected = 0;
    ArrayList<Message> messageArray;
    public final static int FILE_SIZE = 6022386;
    public static String FILE_TO_RECEIVE ="";
    int port;
    public Server(ListView messageList, ArrayList<Message> messageArray, int port, ChatActivity activity) {
        this.messageArray = messageArray;
        this.messageList = messageList;
        this.port = port;
        this.activity = activity;
    }
    ServerSocket welcomeSocket=null;


    void showToast(String s){
        Toast toast = new Toast(activity.getBaseContext());
        View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.filesavetoast, null);
        TextView toastTextView = view.findViewById(R.id.toast_file_save);
        toastTextView.setText(s);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);

        toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
        toast.show();
    }

    public String getBgColorCode() {
        return bgColorCode;
    }

    @Override
    public void run(){
        try{
            String sentence;
            welcomeSocket=new ServerSocket(port);
            while (true){
                Socket connectionSocket=welcomeSocket.accept();
                HandleClient c= new HandleClient();
                c.execute(connectionSocket);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public class HandleClient extends AsyncTask<Socket,Void, Message>{
        String sentence;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Message message;
        @Override
        protected Message doInBackground(Socket... sockets) {
            try {

                 ObjectInputStream in = new ObjectInputStream(sockets[0].getInputStream());
                 message = (Message) in.readObject();

                 sentence = message.getMessage();
                 Log.d("problem","R: " + sentence);
                 if(message.isFile()){
                     FILE_TO_RECEIVE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"P2P-Chat-And-File-Sharing-App";
                     Log.d("problem", "doInBackground: "+FILE_TO_RECEIVE);
                     File directory = new File(FILE_TO_RECEIVE);
                     if(!directory.exists()){
                         directory.mkdirs();
                     }
                     FILE_TO_RECEIVE+=File.separator+message.getMessage();
                     message.setImgDir(FILE_TO_RECEIVE);
                     byte [] mybytearray  = message.getMybytearray();
                     fos = new FileOutputStream(FILE_TO_RECEIVE);
                     bos = new BufferedOutputStream(fos);
                     bos.write(mybytearray);
                     bos.flush();

                     activity.runOnUiThread(new Runnable() {

                         String saveFile = "Received file "+"saved in "+FILE_TO_RECEIVE;
                         @Override
                         public void run() {
                             showToast(saveFile);
                         }
                     });

                     Log.d("file", "isFile: " + FILE_TO_RECEIVE);


                 }
                 else if(message.isBackground()){
                     bgColorCode = message.getMessage();
                     bgselected = 1;

                     activity.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             if(message.getMessage().equals("@@bg1")) {
                                 //Log.d("BACKGROUND", "setMessage: ");
                                 activity.message_List.setBackgroundResource(R.drawable.background1);
                             }
                             else if(message.getMessage().equals("@@bg2")) {
                                 activity.message_List.setBackgroundResource(R.drawable.background2);
                             }
                             else if(message.getMessage().equals("@@bg3")) {
                                 activity.message_List.setBackgroundResource(R.drawable.background3);
                             }
                             else if(message.getMessage().equals("@@bg4")) {
                                 activity.message_List.setBackgroundResource(R.drawable.background4);
                             }
                         }
                     });

                     //activity.setMessage(message);
                 }



//                BufferedReader input = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
//                sentence = input.readLine();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return message ;
        }
        protected void onPostExecute(Message result) {

            if(bgselected == 1){
                bgselected = 0;
            }
            else if(result.getMessage().equals("")){
                Log.d("save", "null message");
            }
            else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MediaPlayer mp = MediaPlayer.create(activity.getBaseContext(), R.raw.insight);
                        mp.start();
                    }
                });
                result.setDate(Calendar.getInstance().getTime());
                result.type=1;
                messageArray.add(result);
                messageList.setAdapter(mAdapter);
                messageList.setSelection(messageList.getCount() - 1);

                Log.d("file", "Received: " + result);

            }
        }
    }
}
