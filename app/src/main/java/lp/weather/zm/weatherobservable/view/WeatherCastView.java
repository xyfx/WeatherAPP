package lp.weather.zm.weatherobservable.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import lp.weather.zm.weatherobservable.R;
import lp.weather.zm.weatherobservable.bean.WeatherBean;


/**
 * Created by ASUS on 2018/5/15.
 */

public class WeatherCastView extends LinearLayout{
    TextView forcastText,forcastTime,forcastType,forcastMaxTemplature,forcastMinTemplature;
    public WeatherCastView(Context context) {
        super(context);
        initViews(context);
    }

    public WeatherCastView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public WeatherCastView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }


    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.forecast_item,this);
        forcastText=findViewById(R.id.forcast_text);
        forcastTime=findViewById(R.id.forcast_time);
        forcastType=findViewById(R.id.forcast_type);
        forcastMaxTemplature=findViewById(R.id.forcast_max_temperature);
    //    forcastMinTemplature=findViewById(R.id.forcast_min_temperature);
    }

    public void  setData(WeatherBean.DataBean.ForecastBean forecastBean, boolean isFirst){
        if(!isFirst){
            forcastText.setVisibility(View.GONE);
        }else{
            forcastText.setVisibility(View.VISIBLE);
        }

        String dateStr=forecastBean.getDate().substring(3);
        forcastTime.setText(dateStr);

        setWeatherType(forecastBean.getType());


        forcastMaxTemplature.setText(forecastBean.getHigh().substring(3)+"/"+forecastBean.getLow().substring(3));
      //  forcastMinTemplature.setText(forecastBean.getLow());
    }


    private void setWeatherType(String type) {
        forcastType.setText(type);
        Drawable drawable=null;
        if(type.contains("雨") && !type.contains("阵雨")){
            drawable=getResources().getDrawable(R.mipmap.weather_small_icon_rain);
        }else if(type.contains("雷")){
            drawable=getResources().getDrawable(R.mipmap.weather_small_icon_thunder);
        }else if(type.contains("阴")||type.contains("多云")){
            drawable=getResources().getDrawable(R.mipmap.weather_small_icon_morecloudy);
        }else if(type.contains("雪")){
            drawable=getResources().getDrawable(R.mipmap.weather_small_icon_rain);
        }else{
            drawable=getResources().getDrawable(R.mipmap.weather_small_icon_sun);
        }
        drawable.setBounds(
                -10,
                0,
                 70,
               70);
        forcastType.setCompoundDrawables(drawable,null,null,null);
    }
}
