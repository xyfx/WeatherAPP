package lp.weather.zm.weatherobservable;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import lp.weather.zm.weatherobservable.bean.CityList;
import lp.weather.zm.weatherobservable.bean.ProvinceBean;
import lp.weather.zm.weatherobservable.bean.WeatherBean;
import lp.weather.zm.weatherobservable.network.WeatherService;
import lp.weather.zm.weatherobservable.utils.Constans;
import lp.weather.zm.weatherobservable.utils.LogUtils;
import lp.weather.zm.weatherobservable.utils.NetWorkUtils;
import lp.weather.zm.weatherobservable.utils.SpUtils;
import lp.weather.zm.weatherobservable.utils.TextReaderUtils;
import lp.weather.zm.weatherobservable.utils.ToastUtils;
import lp.weather.zm.weatherobservable.view.WeatherCastView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends BaseActivity implements View.OnClickListener{
    ImageView ivLocation;
    TextView tvCityArea,tvCity,tvTemperature,tvWeatherDesc,tvForcasrSuggest,tvAdd;
    LinearLayout forcastParent;
    ImageView forecastBackground;
    ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    String mCurrCityName="深圳";
    OptionsPickerView pvOptions;
    private MaterialRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        initViews();
        initRefreshViews();


    }

    private void initRefreshViews() {
        refreshLayout=findViewById(R.id.refresh_view);
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                 retrofitTest();
                 initData();
                 refreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                refreshLayout.finishRefreshLoadMore();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrofitTest();
        initData();
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String text = TextReaderUtils.getReaderString(getBaseContext(),"province");
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                final JsonArray Jarray = parser.parse(text).getAsJsonArray();
                int i=0;
                for(JsonElement obj : Jarray ){
                    CityList city = gson.fromJson( obj , CityList.class);
                    options1Items.add(new ProvinceBean(i, city.getName(), "描述部分", "其他数据"));
                    ArrayList<String> options2Items_01=new ArrayList<>();
                    for (int j=0;j<city.getCity().size();j++){
                        options2Items_01.add(city.getCity().get(j).getName());
                    }
                    options2Items.add(options2Items_01);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPickView();
                    }
                });

            }
        }.start();
    }

    private void initPickView() {

        pvOptions= new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2);
                mCurrCityName=options2Items.get(options1).get(option2);
                SpUtils.getInstance(getBaseContext()).setCityValue(mCurrCityName);
                SpUtils.getInstance(getBaseContext()).setCityDetailValue(mCurrCityName);
                retrofitTest();
            }
        }).build();
        pvOptions.setPicker(options1Items, options2Items);

    }

    private void initViews() {
        tvCity=findViewById(R.id.head_city);
        tvCityArea=findViewById(R.id.head_city_area);
        tvTemperature=findViewById(R.id.head_temperature);
        tvWeatherDesc=findViewById(R.id.head_weath_desc);
        forcastParent=findViewById(R.id.forcast_parent);
        tvForcasrSuggest=findViewById(R.id.forcast_suggest);
        forecastBackground=findViewById(R.id.forecast_background);
        ivLocation=findViewById(R.id.select_location);
        tvAdd=findViewById(R.id.head_add);

        ivLocation.setOnClickListener(this);
        tvCityArea.setOnClickListener(this);
        tvAdd.setOnClickListener(this);




    }

    private void retrofitTest() {
        mCurrCityName=SpUtils.getInstance(this).getCityValue();
        if(!NetWorkUtils.isNetworkAvailable(this)){
            ToastUtils.Show(this,"请检查您的网络!");
            return;
        }

        SpUtils.getInstance(this).setCityValue(mCurrCityName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constans.WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService.BlobService service = retrofit.create(WeatherService.BlobService.class);
        Call<WeatherBean> call = service.getData(mCurrCityName);
        call.enqueue(new Callback<WeatherBean>() {
            @Override
            public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
                if(response.body().getCode()==201){
                    ToastUtils.Show(getBaseContext(),response.body().getMsg());
                    return;
                }else if(response.body().getCode()==300){
                    ToastUtils.Show(getBaseContext(),response.body().getMsg());
                    return;
                }
                showWeatherInfo(response.body().getData());
                tvForcasrSuggest.setText(response.body().getData().getGanmao());

            }

            @Override
            public void onFailure(Call<WeatherBean> call, Throwable t) {
                ToastUtils.Show(getBaseContext(),"加载失败!");
            }
        });
    }

    private void showWeatherInfo(WeatherBean.DataBean weatherBean) {
        tvCity.setText(mCurrCityName);
        tvCityArea.setText(SpUtils.getInstance(this).getCityDitailValue());

        tvTemperature.setText(weatherBean.getWendu() + "℃");
        tvWeatherDesc.setText(weatherBean.getForecast().get(0).getType());

      //  changeBackground(weatherBean.getForecast().get(0).getType());

        forcastParent.removeAllViews();
        List<WeatherBean.DataBean.ForecastBean> forecast = weatherBean.getForecast();
        for (int i = 0; i < forecast.size(); i++) {
            WeatherCastView weatherCastView = new WeatherCastView(this);
            weatherCastView.setData(forecast.get(i), i == 0);
            forcastParent.addView(weatherCastView);
        }

    }

    private void changeBackground(String type) {
        if(type.contains("雨") && !type.contains("雷阵雨")){
            forecastBackground.setImageResource(R.mipmap.weather_rain);
        }else if(type.contains("雷阵雨")){
            forecastBackground.setImageResource(R.mipmap.weather_thunder);
        }else if(type.contains("阴")||type.contains("多云")){
            forecastBackground.setImageResource(R.mipmap.weather_cloudy);
        }else if(type.contains("雪")){
            forecastBackground.setImageResource(R.mipmap.weather_snow);
        }else{
            forecastBackground.setImageResource(R.mipmap.weather_sunshine);
        }

    }

    @Override
    public void onClick(View v) {
        if(v==ivLocation){
            pvOptions.show();
        }else if(v==tvCityArea||v==tvAdd){
            Intent intent=new Intent(this,LocationSelectActivity.class);
            startActivity(intent);
        }
    }
}
