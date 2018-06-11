package lp.weather.zm.weatherobservable.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ASUS on 2018/5/16.
 */

public class NetWorkUtils {
    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

            // 获得网络状态管理器
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager == null) {
                return false;
            } else {
                // 建立网络数组
                NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

                if (net_info != null) {
                    for (int i = 0; i < net_info.length; i++) {
                        // 判断获得的网络状态是否是处于连接状态
                        if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;

    }

}
