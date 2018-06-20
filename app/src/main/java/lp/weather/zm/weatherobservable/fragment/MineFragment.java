package lp.weather.zm.weatherobservable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.xudaojie.qrcodelib.CaptureActivity;
import lp.weather.zm.weatherobservable.R;
import lp.weather.zm.weatherobservable.ViewPagerLayoutManagerActivity;
import lp.weather.zm.weatherobservable.services.MusicServices;

/**
 * Created by ASUS on 2018/6/6.
 */

public class MineFragment extends Fragment implements View.OnClickListener{
    View rootView;
    TextView headBarReturn,headBarName,tvDouYin,tvScan;
    public static final int REQUEST_QR_CODE=100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_mine,container,false);
        initViews(rootView);
        resetHeadBar();
        return rootView;
    }

    private void resetHeadBar() {
        headBarName.setText("我的");
        headBarReturn.setVisibility(View.INVISIBLE);
    }

    private void initViews(View rootView) {
       headBarName=rootView.findViewById(R.id.head_bar_name);
       headBarReturn=rootView.findViewById(R.id.head_bar_return);
       tvDouYin=rootView.findViewById(R.id.fragment_mine_douyin);
       tvDouYin.setOnClickListener(this);
       tvScan=rootView.findViewById(R.id.fragment_mine_scan);
       tvScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       if(v==tvDouYin){
           startActivity(new Intent(getActivity(), ViewPagerLayoutManagerActivity.class));
           stopMusic();
       }else if(v==tvScan){
           Intent i = new Intent(getContext(), CaptureActivity.class);
           getActivity().startActivityForResult(i, REQUEST_QR_CODE);
       }
    }

    private void stopMusic(){
        Intent intentMusic = new Intent(getContext(), MusicServices.class);
        getContext().stopService(intentMusic);
    }
}
