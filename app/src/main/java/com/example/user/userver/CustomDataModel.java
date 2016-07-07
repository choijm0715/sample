package com.example.user.userver;

/**
 * Created by user on 2016-07-07.
 */
public class CustomDataModel {

    public CustomDataModel(String _company, String _explanation, String _date, String _situation, String money) {
        company = _company;
        explanation = _explanation;
        date = _date;
        situation = _situation;
        this.money = money;
    }

    String company;
    String explanation;
    String date;
    String situation;
    String money;


}
