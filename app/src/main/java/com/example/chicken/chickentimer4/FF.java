package com.example.chicken.chickentimer4;

import android.media.MediaPlayer;

import java.util.ArrayList;

public class FF {
    String name;//제품 이름
    ArrayList<Integer> time;
    ArrayList<Integer> time_p;
    boolean isActive; // 현재 타이머가 동작하고 있는지 알려주는 변수
    public MediaPlayer mediaPlayer; // 알람음을 울리게 해주는 필드

    public FF(String name, ArrayList<Integer> time, ArrayList<Integer> time_p, boolean isActive) {
        this.name = name;
        this.time = time;
        this.time_p = time_p;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getTime() {
        return time;
    }

    public void setTime(ArrayList<Integer> time) {
        this.time = time;
    }

    public ArrayList<Integer> getTime_p() {
        return time_p;
    }

    public void setTime_p(ArrayList<Integer> time_p) {
        this.time_p = time_p;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
