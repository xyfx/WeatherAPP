package lp.weather.zm.weatherobservable.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import lp.weather.zm.weatherobservable.R;

/**
 * Created by ASUS on 2018/5/27.
 */

public class MusicServices extends Service{
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(mediaPlayer==null){

// R.raw.mmp是资源文件，MP3格式的
            mediaPlayer = MediaPlayer.create(this, R.raw.cd);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
        mediaPlayer.stop();
    }
}