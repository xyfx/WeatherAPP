package lp.weather.zm.weatherobservable.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ASUS on 2018/5/15.
 */

public class TextReaderUtils {

    /**
     * 流转换成字符串
     *
     * @param path
     * @return
     */
    public static String getReaderString(Context context, String path) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] bytes = new byte[512];
        int length;
        String result = null;
        InputStream in = null;
        try {
            in = context.getAssets().open("" + path + ".json");
            while ((length = in.read(bytes)) != -1) {
                buffer.write(bytes, 0, length);
            }
            result = new String(buffer.toByteArray(), "UTF-8");
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }
            try {
                buffer.close();
            } catch (IOException e) {
                in = null;
            }
        }
        return result;
    }

}
