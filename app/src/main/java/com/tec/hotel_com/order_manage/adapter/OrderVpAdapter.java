package com.tec.hotel_com.order_manage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 作者：凌涛 on 2018/8/7 10:29
 * 邮箱：771548229@qq..com
 */
public class OrderVpAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String[] titles;

    public OrderVpAdapter(FragmentManager fm, List<Fragment> list,String[] titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    //返回每一项的数据
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    //返回ViewPager中Fragment的总数
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
