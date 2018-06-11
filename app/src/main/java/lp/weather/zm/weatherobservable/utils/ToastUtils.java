package lp.weather.zm.weatherobservable.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ASUS on 2018/5/15.
 */

public class ToastUtils {
    public  static void Show(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
