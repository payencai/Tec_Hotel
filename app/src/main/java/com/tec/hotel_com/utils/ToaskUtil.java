package com.tec.hotel_com.utils;

import android.content.Context;
import android.widget.Toast;


public class ToaskUtil {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
    public static void showToast(Context context, int stringRes) {
        if (toast == null) {
            toast = Toast.makeText(context, stringRes, Toast.LENGTH_SHORT);
        } else {
            toast.setText(stringRes);
        }
        toast.show();
    }


}
