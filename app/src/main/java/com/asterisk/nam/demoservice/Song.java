package com.asterisk.nam.demoservice;

public class Song {

    private String mTitle;
    private int mFile;

    public Song (String title, int file){
        this.mTitle = title;
        this.mFile = file;
    }

    public String getTitle(){

        return mTitle;
    }

    public int getFile(){
        return mFile;
    }
}
