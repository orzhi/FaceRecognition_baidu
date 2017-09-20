package com.wang.testface.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 夜雨飘零 on 2017/9/19.
 */

public class ToastUtil {
    private static Toast toast;

    public static void show(Context context, String title){
        if (toast == null){
            toast = Toast.makeText(context,title,Toast.LENGTH_LONG);
        }else {
            toast.setText(title);
        }
        toast.show();
    }
}
