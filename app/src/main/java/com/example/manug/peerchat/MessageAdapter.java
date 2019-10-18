package com.example.manug.peerchat;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MessageAdapter extends BaseAdapter{

    Context context;
    ArrayList<Message> arr = new ArrayList<>();

    public MessageAdapter(Context context,ArrayList<Message> arr) {

        this.context = context;
        this.arr = arr;

        for(Message mssg: arr){
            String sst = mssg.getMessage();
            Log.d("problem","              "+sst);
        }
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //View listItemView=convertView;

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.message_list,parent,false);

        }
        Message currentMessage= (Message) getItem(position);
        String message=currentMessage.getMessage();

        //Log.d("problem","    Current Message:    " + message);
        ConstraintLayout sent_list = (ConstraintLayout) convertView.findViewById(R.id.sent_list_body);
        ConstraintLayout received_list = (ConstraintLayout) convertView.findViewById(R.id.received_list_body);
        TextView sent=(TextView) convertView.findViewById(R.id.sent_message_body);
        TextView sent_time = (TextView) convertView.findViewById(R.id.sent_message_time);
        TextView received= (TextView) convertView.findViewById(R.id.received_message_body);
        TextView received_time = (TextView) convertView.findViewById(R.id.received_message_time);
        ImageView sent_image = (ImageView) convertView.findViewById(R.id.sent_image);
        ImageView received_image = (ImageView) convertView.findViewById(R.id.received_image);

        sent_list.setVisibility(View.GONE);
        received_list.setVisibility(View.GONE);
        sent.setText("");
        sent.setVisibility(View.GONE);
        received.setText("");
        received.setVisibility(View.GONE);
        sent_time.setText("");
        sent_time.setVisibility(View.GONE);
        received_time.setText("");
        received_time.setVisibility(View.GONE);
        sent_image.setVisibility(View.GONE);
        received_image.setVisibility(View.GONE);



        String directory = currentMessage.getImgDir();
        Log.d("Image", "directory: "+directory);

        if(currentMessage.isSent()){

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(currentMessage.getDate());
            sent_time.setText(currentDateTimeString);
            sent_time.setVisibility(View.VISIBLE);

            if(currentMessage.isImage){
                Log.d("Image", "Image" + directory);
                File imgFile = new File(directory);
                if (imgFile.exists()) {
                    Log.d("Image", "bind: +Yeah Exists");
                    Glide.with(context)
                            .load(directory)
                            .override(280, 280)
                            .into(sent_image);
                }
                sent_image.setVisibility(View.VISIBLE);
                sent_list.setVisibility(View.VISIBLE);
            }
            else {
                sent.setText(message);
                sent.setVisibility(View.VISIBLE);
                sent_list.setVisibility(View.VISIBLE);
            }



        }
        else{

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(currentMessage.getDate());
            received_time.setText(currentDateTimeString);
            received_time.setVisibility(View.VISIBLE);

            if(currentMessage.isImage){
                Log.d("Image", "Image" + directory);
                File imgFile = new File(directory);
                if (imgFile.exists()) {
                    Log.d("Image", "bind: +Yeah Exists");
                    Glide.with(context)
                            .load(directory)
                            .override(280, 280)
                            .into(received_image);
                }
                received_image.setVisibility(View.VISIBLE);
                received_list.setVisibility(View.VISIBLE);
            }
            else {
                received.setText(message);
                //received.setAnimation(animation);
                received.setVisibility(View.VISIBLE);
                received_list.setVisibility(View.VISIBLE);
            }


        }

        return convertView;
    }
}
