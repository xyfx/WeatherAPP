package lp.weather.zm.weatherobservable.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ASUS on 2018/5/16.
 */

public class SpUtils {


    public static final String CITY_SAVE = "city_save";
    public static final String CITY_DETAIL_SAVE = "city_detail_save";

    public static SpUtils INSTANCE;

    private SharedPreferences preferences;

    private SpUtils()
    {
    }

    public static synchronized SpUtils getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new SpUtils();
            INSTANCE.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return INSTANCE;
    }

    /**
     * 储存城市值
     *
     * @param value
     * @param value
     */
    public void setCityValue(String value)
    {
        preferences.edit().putString(CITY_SAVE, value).apply();
    }

    /**
     * 获取存储的城市值
     * @return
     */
    public String getCityValue()
    {
        return preferences.getString(CITY_SAVE,"深圳");
    }

    /**
     * 储存详细城市值
     *
     * @param value
     * @param value
     */
    public void setCityDetailValue(String value)
    {
        preferences.edit().putString(CITY_DETAIL_SAVE, value).apply();
    }

    /**
     * 获取存储详细的城市值
     * @return
     */
    public String getCityDitailValue()
    {
        return preferences.getString(CITY_DETAIL_SAVE,"深圳");
    }
}
