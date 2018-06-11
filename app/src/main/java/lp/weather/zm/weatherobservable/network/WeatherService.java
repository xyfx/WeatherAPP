package lp.weather.zm.weatherobservable.network;


import lp.weather.zm.weatherobservable.bean.BeautyPic;
import lp.weather.zm.weatherobservable.bean.WeatherBean;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ASUS on 2018/5/15.
 */

public class WeatherService {
    public interface BlobService {
        @POST("weatherApi")
        Call<WeatherBean> getData(@Query("city") String city);
        @POST("meituApi")
        Call<BeautyPic> getImages(@Query("page") int page);
    }

}
