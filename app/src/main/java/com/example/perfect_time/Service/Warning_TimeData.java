package com.example.perfect_time.Service;

public class Warning_TimeData {

    int Timer_H, Timer_M;
    String Timer_Name, Timer_Memo;
    boolean Important;

    Warning_TimeData(int Timer_H,int Timer_M,String Timer_Name,String Timer_Memo,boolean Important){
        this.Timer_H = Timer_H;
        this.Timer_M = Timer_M;
        this.Timer_Name = Timer_Name;
        this.Timer_Memo = Timer_Memo;
        this.Important = Important;
    }

}
