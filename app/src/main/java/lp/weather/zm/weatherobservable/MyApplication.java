package lp.weather.zm.weatherobservable;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;

/**
 * Created by ASUS on 2018/5/21.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, null, null, UMConfigure.DEVICE_TYPE_PHONE, null);
    }
}
