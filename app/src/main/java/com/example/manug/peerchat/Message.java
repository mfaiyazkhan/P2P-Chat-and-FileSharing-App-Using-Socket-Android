package com.example.manug.peerchat;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
    String message = null;
    public int type;
    int fileOrNot = 0;
    private Date date;
    public String imgDir;
    int bg = 0;
    public boolean isImage=false;
    byte [] mybytearray;


    public Message(String message, byte [] mybytearray, int type){
        this.message=message;
        this.mybytearray = mybytearray;
        this.type = type;
        fileOrNot = 1;
    }

    public Message(String message,int type){
        this.message=message;
        this.type=type;
    }


    public void setBG(){
        bg = 1;
    }

    public boolean isBackground(){
        if(bg == 1)
            return true;
        else
            return false;
    }

    public String getImgDir() {
        return imgDir;
    }

    public void setImgDir(String imgDir) {
        this.imgDir = imgDir;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }


    public boolean isFile(){
        if(fileOrNot == 1)
            return true;
        else
            return false;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public boolean isSent(){
        if(type==0){
            return true;
        }
        return false;
    }
    public String getMessage(){
        return message;
    }

    public byte[] getMybytearray() {
        return mybytearray;
    }
}
