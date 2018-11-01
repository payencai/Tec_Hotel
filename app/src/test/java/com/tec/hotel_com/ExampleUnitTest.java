package com.tec.hotel_com;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.print("测试开始了");
        getDays("2018.1.1", "2018.12.31");
    }

    public static void getDays(String from, String to) {
        Calendar calendar = Calendar.getInstance();
        String[] array = {from, to};
        Date[] ds = new Date[array.length];
        for (int i = 0; i < array.length; i++) {
            String[] fs = array[i].split("[^\\d]+");
            calendar.set(Integer.parseInt(fs[0]), Integer.parseInt(fs[1]) - 1, Integer.parseInt(fs[2]));
            ds[i] = calendar.getTime();
        }
        for (Date x = ds[0]; x.compareTo(ds[1]) <= 0; ) {
            calendar.setTime(x);
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            String[] week = "日一二三四五六".split("");
            if (calendar.get(Calendar.DATE) == 1) {
                System.out.println(calendar.get(Calendar.YEAR) + "的" + (calendar.get(Calendar.MONTH) + 1) + "月1号是 星期" + week[today - 1]);
            }
            calendar.add(Calendar.MONTH, 1);
            x = calendar.getTime();
        }
    }
}