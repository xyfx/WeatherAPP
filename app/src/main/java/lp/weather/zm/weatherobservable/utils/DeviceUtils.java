package lp.weather.zm.weatherobservable.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by ASUS on 2018/5/25.
 */

public class DeviceUtils {

   public static int getScreenWidth(Context context){
       WindowManager wm = (WindowManager) context
               .getSystemService(Context.WINDOW_SERVICE);

       int width = wm.getDefaultDisplay().getWidth();
       int height = wm.getDefaultDisplay().getHeight();
       return width;
   }
}
