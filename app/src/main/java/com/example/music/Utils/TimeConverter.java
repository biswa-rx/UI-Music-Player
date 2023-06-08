package com.example.music.Utils;

public class TimeConverter {
    public static String millisecondToString(int duration) {
        String time = "";
        int sec = duration / 1000;
        int min = sec / 60;
        if(min>9){
            time += min/10;
        }else{
            time += "0";
        }
        time += min + ":";
        sec = sec % 60;
        if (sec < 10) {
            time += '0';
        }
        time += sec;
        return time;
    }
}
