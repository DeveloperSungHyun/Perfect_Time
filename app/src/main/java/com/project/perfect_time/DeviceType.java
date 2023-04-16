package com.project.perfect_time;

import android.content.Context;
import android.content.res.Configuration;

public class DeviceType {

    Context context;
    public DeviceType(Context context){
        this.context = context;
    }

    public boolean IsPhone(){
        int screenSizeType = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if(screenSizeType== Configuration.SCREENLAYOUT_SIZE_NORMAL || screenSizeType==Configuration.SCREENLAYOUT_SIZE_SMALL){
            return true;
        }
        return false;

    }


    public boolean IsTablet(){
        int screenSizeType = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if(screenSizeType== Configuration.SCREENLAYOUT_SIZE_XLARGE || screenSizeType==Configuration.SCREENLAYOUT_SIZE_LARGE){
            return true;
        }
        return false;

    }

}
