package lp.weather.zm.weatherobservable;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import lp.weather.zm.weatherobservable.utils.SpUtils;
import lp.weather.zm.weatherobservable.utils.ToastUtils;

/**
 * Created by ASUS on 2018/5/16.
 */

public class SplashActivity extends BaseActivity{
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String[] permisions=new String[]{
            Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private List<String> mPermisionList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkPermition(this);
    }



    private void startLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }

    private void checkPermition(Context mContext) {
        mPermisionList.clear();
        for(int i=0;i<permisions.length;i++){
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(mContext, permisions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermisionList.add(permisions[i]);
                }
            }else{
                startLocation();
                return;
            }

        }

        /**
         * 判断是否为空
         */
        if (mPermisionList.isEmpty()) {//未授予的权限为空，表示都授予了
           startLocation();
        } else {//请求权限方法
            String[] permissions = mPermisionList.toArray(new String[mPermisionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, 1);
        }

    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            ToastUtils.Show(getBaseContext(),"您当前的城市为:"+city);
            SpUtils.getInstance(getBaseContext()).setCityValue(city);
            mLocationClient.unRegisterLocationListener(myListener);
            Intent intent=new Intent(getBaseContext(),WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }


    boolean mShowRequestPermission = true;//用户是否禁止权限

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissions[i]);
                        if (showRequestPermission) {//
                            //judgePermission();//重新申请权限
                            return;
                        } else {
                            mShowRequestPermission = false;//已经禁止
                        }
                    }
                }
               startLocation();
                break;
            default:
                break;
        }
    }


}
