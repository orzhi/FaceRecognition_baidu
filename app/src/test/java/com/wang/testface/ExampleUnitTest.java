package com.wang.testface;

import com.wang.testface.util.CompressBitmapUtil;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        File file = new File("E:\\11.jpg");
        System.out.println(file.length()/1024/1024);
    }
}