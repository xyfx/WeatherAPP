package lp.weather.zm.weatherobservable;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import lp.weather.zm.weatherobservable.fragment.BeautyPicFragment;
import lp.weather.zm.weatherobservable.fragment.MineFragment;
import lp.weather.zm.weatherobservable.fragment.WeatherFragment;
import lp.weather.zm.weatherobservable.services.MusicServices;

import static lp.weather.zm.weatherobservable.fragment.MineFragment.REQUEST_QR_CODE;

/**
 * Created by ASUS on 2018/5/24.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private WeatherFragment weatherFragment;
    private BeautyPicFragment beautyPicFragment;
    private MineFragment mineFragment;
    private FragmentManager fm;
    private ImageView ivMainHomeImg,ivMainBeauty,ivMainMine;
    private TextView tvMainHomeName,tvMainBeautyName,tvMainMine;
    private  Intent intentMusic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initMusic();

    }

    private void initMusic() {
        intentMusic = new Intent(this, MusicServices.class);
        startService(intentMusic);
    }

    private void initViews() {
        ivMainHomeImg=findViewById(R.id.main_home_img);
        ivMainBeauty=findViewById(R.id.main_beauty_img);
        tvMainHomeName=findViewById(R.id.main_home_name);
        tvMainBeautyName=findViewById(R.id.main_beauty_name);
        ivMainMine=findViewById(R.id.main_mine_img);
        tvMainMine=findViewById(R.id.main_mine_name);

        ivMainHomeImg.setOnClickListener(this);
        ivMainBeauty.setOnClickListener(this);
        ivMainMine.setOnClickListener(this);

        fm = getSupportFragmentManager();

        showFragment(0);
        ivMainHomeImg.setBackgroundResource(R.mipmap.ic_show_press);
        tvMainHomeName.setTextColor(getResources().getColor(R.color.colorPet));

    }

    private void showFragment(int position)
    {
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (position)
        {
            case 0:
                if (weatherFragment != null)
                {
                    ft.show(weatherFragment);
                }else
                {
                    weatherFragment = new WeatherFragment();
                    ft.add(R.id.main_fragment, weatherFragment);
                }
                break;
            case 1:
                if (beautyPicFragment != null)
                {
                    ft.show(beautyPicFragment);
                }else
                {
                    beautyPicFragment = new BeautyPicFragment();
                    ft.add(R.id.main_fragment, beautyPicFragment);
                }
                break;
            case 2:
                if (mineFragment != null)
                {
                    ft.show(mineFragment);
                }else
                {
                    mineFragment = new MineFragment();
                    ft.add(R.id.main_fragment, mineFragment);
                }
                break;
            default:
                break;
        }
        ft.commit();
    }


    private void hideFragment(FragmentTransaction ft)
    {
        if (weatherFragment != null)
        {
            ft.hide(weatherFragment);
        }
        if (beautyPicFragment != null)
        {
            ft.hide(beautyPicFragment);
        }
        if(mineFragment!=null){
            ft.hide(mineFragment);
        }

    }

    @Override
    public void onClick(View v) {
        reSetBackground();
        if(ivMainHomeImg==v){
            showFragment(0);
            ivMainHomeImg.setBackgroundResource(R.mipmap.ic_show_press);
            tvMainHomeName.setTextColor(getResources().getColor(R.color.colorPet));
        }else if(ivMainBeauty==v){
            showFragment(1);
            ivMainBeauty.setBackgroundResource(R.mipmap.ic_service_press);
            tvMainBeautyName.setTextColor(getResources().getColor(R.color.colorPet));
        }else if(ivMainMine==v){
            showFragment(2);
            ivMainMine.setBackgroundResource(R.mipmap.ic_me_press);
            tvMainMine.setTextColor(getResources().getColor(R.color.colorPet));
        }
    }

    //    重置底部Button的背景和字体颜色
    private void reSetBackground()
    {
        ivMainHomeImg.setBackgroundResource(R.mipmap.ic_show);
        tvMainHomeName.setTextColor(getResources().getColor(R.color.color9));

        ivMainBeauty.setBackgroundResource(R.mipmap.ic_service);
        tvMainBeautyName.setTextColor(getResources().getColor(R.color.color9));

        ivMainMine.setBackgroundResource(R.mipmap.ic_me);
        tvMainMine.setTextColor(getResources().getColor(R.color.color9));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentMusic);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            String result = data.getStringExtra("result");
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
