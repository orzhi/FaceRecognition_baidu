package com.wang.testface.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 夜雨飘零 on 2017/9/29.
 */

public class CompressBitmapUtil {

    //保存压缩的图片
    public static String CompressBitmap(String path) {
        try {
            String savePath = path.substring(0, path.length() - 7) + "_temp.jpg";
            File file = new File(path);
            if (file.exists()) {
                //保存的路径
                OutputStream out = new FileOutputStream(savePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = ((int) (file.length() / 1024 / 1024)) + 1;
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                //保存压缩的图片
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                //删除原来的图片
                //file.delete();
            }
            return savePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
