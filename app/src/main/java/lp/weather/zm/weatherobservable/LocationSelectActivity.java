package lp.weather.zm.weatherobservable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import lp.weather.zm.weatherobservable.bean.CityList;
import lp.weather.zm.weatherobservable.bean.ProvinceBean;
import lp.weather.zm.weatherobservable.utils.SpUtils;
import lp.weather.zm.weatherobservable.utils.TextReaderUtils;
import lp.weather.zm.weatherobservable.utils.ToastUtils;
import lp.weather.zm.weatherobservable.view.TagGroup;

/**
 * Created by ASUS on 2018/5/17.
 */

public class LocationSelectActivity extends BaseActivity implements View.OnClickListener{
    private TagGroup mTagGroup;
    private TextView tvReturn;
    private AutoCompleteTextView autoCompleteTextView;
    List<String> cityList=new ArrayList<>();
    ArrayAdapter<String> cityAdapter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatiom);

        initViews();
        initCityDatas();
    }

    private void initCityDatas() {
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
                    for (int j=0;j<city.getCity().size();j++){
                        cityList.add(city.getCity().get(j).getName());
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //创建适配器
                        cityAdapter=new ArrayAdapter<>(
                                getBaseContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                cityList);
                        autoCompleteTextView.setAdapter(cityAdapter);

                    }
                });

            }
        }.start();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpUtils.getInstance(getBaseContext()).setCityValue(autoCompleteTextView.getText().toString().trim());
                SpUtils.getInstance(getBaseContext()).setCityDetailValue(autoCompleteTextView.getText().toString().trim());
                finish();
            }
        });

    }

    private void initViews() {
        tvReturn=findViewById(R.id.head_bar_return);
        tvReturn.setOnClickListener(this);
        mTagGroup=findViewById(R.id.mTagGroup);
        autoCompleteTextView=findViewById(R.id.auto_search);
        List<String> lists=new ArrayList<>();


        lists.add("北京市");
        lists.add("上海市");
        lists.add("广州市");
        lists.add("深圳市");
        lists.add("苏州市");
        lists.add("郑州市");
        lists.add("南京市");
        lists.add("济南市");
        lists.add("武汉市");
        lists.add("青岛市");
        lists.add("成都市");
        lists.add("沈阳市");

        lists.add("台北市");
        lists.add("常州市");
        lists.add("无锡市");
        lists.add("天津市");
        lists.add("重庆市");
        lists.add("丽江市");
        lists.add("金华市");
        lists.add("烟台市");
        lists.add("南昌市");
        initChildViews(lists);





    }

    //标签设置宽高
    private void initChildViews(List<String> list) {
        // TODO Auto-generated method stub

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                -2 , -2);
        lp.leftMargin = 8;
        lp.rightMargin = 8;
        lp.topMargin = 8;
        lp.bottomMargin = 8;
        mTagGroup.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final TextView view = new TextView(this);
            view.setText(list.get(i));
            view.setTextColor(getResources().getColor(R.color.white));
            view.setTag(i);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String city=view.getText().toString().trim();
                    SpUtils.getInstance(getBaseContext()).setCityValue(city);
                    SpUtils.getInstance(getBaseContext()).setCityDetailValue(city);
                    finish();
                }
            });
            mTagGroup.addView(view, lp);
            final int finalI = i;

        }

    }

    @Override
    public void onClick(View v) {
        if(v==tvReturn){
            finish();
        }
    }
}
