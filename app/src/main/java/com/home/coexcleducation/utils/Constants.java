package com.home.coexcleducation.utils;

import com.home.coexcleducation.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Constants {
    public static final String FCM_REGISTRATION_KEY   = "736512491792";  /// Live
    public static String APIKEY_YOUTUBE = "AIzaSyCDAWaYI2H3RXJD9YTMC6Rw4F6MY4JeeRk";



    public static String SUCCESS_MESSEGE_TYPE = "success";
    public static String FAILURE_MESSEGE_TYPE = "failure";

    public static String MONTHLY_SUBSCRIPTION = "coexcl_monthly";
    public static String YEARLY_SUBSCRIPTION = "coexcl_yearly";


    public List<Integer> getColourList() {
        List<Integer> mColourList = new ArrayList<>();
//        mColourList.add(R.color.list_color6);
//        mColourList.add(R.color.list_color2);
//        mColourList.add(R.color.list_color1);
        mColourList.add(R.color.list_color10);
//        mColourList.add(R.color.list_color5);
//        mColourList.add(R.color.list_color7);
//        mColourList.add(R.color.list_color9);
//        mColourList.add(R.color.list_color3);
//        mColourList.add(R.color.list_color11);
//        mColourList.add(R.color.list_color4);
//        mColourList.add(R.color.list_color8);
//        mColourList.add(R.color.list_color13);
//        mColourList.add(R.color.list_color14);
//        mColourList.add(R.color.list_color15);
//        mColourList.add(R.color.list_color12);
//        mColourList.add(R.color.list_color16);
//        mColourList.add(R.color.list_color17);

//        Integer count = new Random().nextInt(mColourList.size());
        return mColourList;
    }

    public Integer getRandomColour() {
        List<Integer> mColourList = new ArrayList<>();
//        mColourList.add(R.color.list_color1);
//        mColourList.add(R.color.list_color2);
//        mColourList.add(R.color.list_color3);
//        mColourList.add(R.color.list_color4);
//        mColourList.add(R.color.list_color5);
//        mColourList.add(R.color.list_color6);
//        mColourList.add(R.color.list_color7);
//        mColourList.add(R.color.list_color8);
//        mColourList.add(R.color.list_color9);
        mColourList.add(R.color.list_color10);
//        mColourList.add(R.color.list_color11);
//        mColourList.add(R.color.list_color12);
//        mColourList.add(R.color.list_color13);
//        mColourList.add(R.color.list_color14);
//        mColourList.add(R.color.list_color15);
//        mColourList.add(R.color.list_color16);
//        mColourList.add(R.color.list_color17);

        Integer count = new Random().nextInt(mColourList.size());
        return mColourList.get(count);
    }

}


//  mColourList.add(R.color.service_color1);
//          mColourList.add(R.color.service_color2);
//          mColourList.add(R.color.service_color3);
//          mColourList.add(R.color.service_color4);
//          mColourList.add(R.color.service_color5);
//          mColourList.add(R.color.service_color6);
//          mColourList.add(R.color.service_color7);
//          mColourList.add(R.color.service_color8);
//          mColourList.add(R.color.service_color9);
//          mColourList.add(R.color.service_color10);
//          mColourList.add(R.color.service_color12);
//          mColourList.add(R.color.service_color13);
//          mColourList.add(R.color.service_color14);
//          mColourList.add(R.color.service_color6);
//          mColourList.add(R.color.service_color2);
//          mColourList.add(R.color.service_color8);
//          mColourList.add(R.color.service_color5);