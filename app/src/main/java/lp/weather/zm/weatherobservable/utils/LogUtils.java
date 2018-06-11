package lp.weather.zm.weatherobservable.utils;

import android.util.Log;

/**
 * Created by ASUS on 2018/5/15.
 */

public class LogUtils {
    static boolean isDubug=true;

    public static void Error(String tag,String msg){
        if(isDubug){
            Log.e(tag,msg);
        }
    }
}
