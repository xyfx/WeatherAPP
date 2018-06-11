package lp.weather.zm.weatherobservable;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;
import lp.weather.zm.weatherobservable.utils.SpUtils;
import lp.weather.zm.weatherobservable.utils.ToastUtils;


public class GuideActivity extends AppCompatActivity {
    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;
    public LocationClient mLocationClient = null;
    private GuideActivity.MyLocationListener myListener = new GuideActivity.MyLocationListener();
    private String[] permisions=new String[]{
            Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private List<String> mPermisionList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setListener();
        processLogic();
        checkPermition(this);
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
            ActivityCompat.requestPermissions(GuideActivity.this, permissions, 1);
        }

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

            if(null==city||null==district){
                city="深圳";
                district="龙岗区";
                street="黄金山街";
            }
            SpUtils.getInstance(getBaseContext()).setCityValue(city);
            SpUtils.getInstance(getBaseContext()).setCityDetailValue(district+street);

            mLocationClient.unRegisterLocationListener(myListener);

        }
    }


    boolean mShowRequestPermission = true;//用户是否禁止权限



    private void initView() {
        setContentView(R.layout.content_guide);
        mBackgroundBanner = findViewById(R.id.banner_guide_background);
        mForegroundBanner = findViewById(R.id.banner_guide_foreground);
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();

            }
        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        mBackgroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.mipmap.weather_bg_03,
                R.mipmap.weather_bg_02,
                R.mipmap.weather_bg_01);

        mForegroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.mipmap.weather_fg,
                R.mipmap.weather_fg,
                R.mipmap.weather_fg);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(GuideActivity.this, permissions[i]);
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
