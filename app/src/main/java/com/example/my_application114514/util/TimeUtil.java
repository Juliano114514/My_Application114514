package com.example.my_application114514.util;

public class TimeUtil {
  public static String msToMMSS(int ms){
    int second = (ms/1000)  % 60;
    int minute = ms / 60000;

    String str = "";
    if(minute<10) {
      str = "0";
    }
    str = str+minute+":";

    if(second<10){
      str = str + "0";
    }
    str = str + second;
    return str;
  }
}
