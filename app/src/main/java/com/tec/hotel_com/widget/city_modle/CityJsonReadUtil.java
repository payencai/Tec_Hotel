package com.tec.hotel_com.widget.city_modle;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 作者：凌涛 on 2018/10/17 10:11
 * 邮箱：771548229@qq..com
 */
public class CityJsonReadUtil {
    public CityJsonReadUtil() {
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));

            String line;
            while((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return stringBuilder.toString();
    }

}
