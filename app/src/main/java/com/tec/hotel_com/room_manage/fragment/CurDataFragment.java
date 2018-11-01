package com.tec.hotel_com.room_manage.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseFragment;
import com.tec.hotel_com.room_manage.entity.DayInfo;
import com.tec.hotel_com.room_manage.entity.RoomInfo;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.widget.MonthDataFormView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：凌涛 on 2018/8/14 16:38
 * 邮箱：771548229@qq..com
 */
@SuppressLint("ValidFragment")
public class CurDataFragment extends BaseFragment {
    @BindView(R.id.monthSignFormView)
    MonthDataFormView monthSignFormView;

    RoomInfo roomInfo;

    public CurDataFragment(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_curdata;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        monthSignFormView.setRoomInfo(roomInfo);
    }

    @Subscribe
    public void upDateInfo(List<DayInfo> list) {
        monthSignFormView.setList(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
