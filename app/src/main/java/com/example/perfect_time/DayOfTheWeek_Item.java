package com.example.perfect_time;

public class DayOfTheWeek_Item {
    private Boolean check_mark;
    private String day_of_the_week;
    private int day_of_the_week_TextColor;


    public DayOfTheWeek_Item(Boolean check_mark, String day_of_the_week, int day_of_the_week_TextColor) {
        this.check_mark = check_mark;
        this.day_of_the_week = day_of_the_week;
        this.day_of_the_week_TextColor = day_of_the_week_TextColor;
    }

    public Boolean getCheck_mark() {
        return check_mark;
    }

    public void setCheck_mark(Boolean check_mark) {
        this.check_mark = check_mark;
    }

    public String getDay_of_the_week() {
        return day_of_the_week;
    }

    public void setDay_of_the_week(String day_of_the_week) {
        this.day_of_the_week = day_of_the_week;
    }

    public int getDay_of_the_week_TextColor() {
        return day_of_the_week_TextColor;
    }

    public void setDay_of_the_week_TextColor(int day_of_the_week_TextColor) {
        this.day_of_the_week_TextColor = day_of_the_week_TextColor;
    }
}
