package lp.weather.zm.weatherobservable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import lp.weather.zm.weatherobservable.LocationSelectActivity;
import lp.weather.zm.weatherobservable.R;
import lp.weather.zm.weatherobservable.bean.CityList;
import lp.weather.zm.weatherobservable.bean.ProvinceBean;
import lp.weather.zm.weatherobservable.bean.WeatherBean;
import lp.weather.zm.weatherobservable.network.WeatherService;
import lp.weather.zm.weatherobservable.utils.Constans;
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

/**
 * Created by ASUS on 2018/5/24.
 */

public class WeatherFragment extends Fragment implements View.OnClickListener{
    ImageView ivLocation;
    TextView tvCityArea,tvCity,tvTemperature,tvWeatherDesc,tvForcasrSuggest,tvAdd;
    LinearLayout forcastParent;
    ImageView forecastBackground;
    ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    String mCurrCityName="深圳";
    OptionsPickerView pvOptions;
    private MaterialRefreshLayout refreshLayout;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_weather,container,false);
        initviews(rootView);
        initRefreshViews();
        return rootView;
    }

    private void initviews(View rootView) {
        tvCity=rootView.findViewById(R.id.head_city);
        tvCityArea=rootView.findViewById(R.id.head_city_area);
        tvTemperature=rootView.findViewById(R.id.head_temperature);
        tvWeatherDesc=rootView.findViewById(R.id.head_weath_desc);
        forcastParent=rootView.findViewById(R.id.forcast_parent);
        tvForcasrSuggest=rootView.findViewById(R.id.forcast_suggest);
        forecastBackground=rootView.findViewById(R.id.forecast_background);
        ivLocation=rootView.findViewById(R.id.select_location);
        tvAdd=rootView.findViewById(R.id.head_add);
        refreshLayout=rootView.findViewById(R.id.refresh_view);

        ivLocation.setOnClickListener(this);
        tvCityArea.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        retrofitTest();
        initData();
    }

    private void initRefreshViews() {

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

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String text = TextReaderUtils.getReaderString(getContext(),"province");
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPickView();
                    }
                });

            }
        }.start();
    }

    private void initPickView() {

        pvOptions= new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2);
                mCurrCityName=options2Items.get(options1).get(option2);
                SpUtils.getInstance(getContext()).setCityValue(mCurrCityName);
                SpUtils.getInstance(getContext()).setCityDetailValue(mCurrCityName);
                retrofitTest();
            }
        }).build();
        pvOptions.setPicker(options1Items, options2Items);

    }

    private void retrofitTest() {
        mCurrCityName= SpUtils.getInstance(getContext()).getCityValue();
        if(!NetWorkUtils.isNetworkAvailable(getContext())){
            ToastUtils.Show(getContext(),"请检查您的网络!");
            return;
        }

        SpUtils.getInstance(getContext()).setCityValue(mCurrCityName);

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
                    ToastUtils.Show(getContext(),response.body().getMsg());
                    return;
                }else if(response.body().getCode()==300){
                    ToastUtils.Show(getContext(),response.body().getMsg());
                    return;
                }
                showWeatherInfo(response.body().getData());
                tvForcasrSuggest.setText(response.body().getData().getGanmao());

            }

            @Override
            public void onFailure(Call<WeatherBean> call, Throwable t) {
                ToastUtils.Show(getContext(),"加载失败!");
            }
        });
    }

    private void showWeatherInfo(WeatherBean.DataBean weatherBean) {
        tvCity.setText(mCurrCityName);
        tvCityArea.setText(SpUtils.getInstance(getContext()).getCityDitailValue());

        tvTemperature.setText(weatherBean.getWendu() + "℃");
        tvWeatherDesc.setText(weatherBean.getForecast().get(0).getType());

        //  changeBackground(weatherBean.getForecast().get(0).getType());

        forcastParent.removeAllViews();
        List<WeatherBean.DataBean.ForecastBean> forecast = weatherBean.getForecast();
        for (int i = 0; i < forecast.size(); i++) {
            WeatherCastView weatherCastView = new WeatherCastView(getContext());
            weatherCastView.setData(forecast.get(i), i == 0);
            forcastParent.addView(weatherCastView);
        }

    }

    @Override
    public void onClick(View v) {
        if(v==ivLocation){
            pvOptions.show();
        }else if(v==tvCityArea||v==tvAdd){
            Intent intent=new Intent(getContext(),LocationSelectActivity.class);
            startActivity(intent);
        }
    }
}
