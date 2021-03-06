package com.tec.hotel_com.common.rv.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.common.rv.base.RVSimpleAdapter;


/**
 * Created by HIAPAD on 2017/12/2.
 */

public class ErrorCell extends RVAbsStateCell {

    public ErrorCell(Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.ERROR_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position, Context context, RVBaseAdapter adapter) {

    }


    @Override
    protected View getDefaultView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.rv_error_layout, null);
    }
}
