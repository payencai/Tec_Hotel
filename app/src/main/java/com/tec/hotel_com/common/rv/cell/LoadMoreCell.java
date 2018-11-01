package com.tec.hotel_com.common.rv.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.common.rv.base.RVSimpleAdapter;
import com.tec.hotel_com.utils.DisplayUtils;


/**
 * Created by HIAPAD on 2017/12/2.
 */

public class LoadMoreCell extends RVAbsStateCell {

    public static final int mDefaultHeight = 80;//dp

    public LoadMoreCell(Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.LOAD_MORE_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position, Context context, RVBaseAdapter adapter) {

    }

    @Override
    protected View getDefaultView(Context context) {
        // 设置LoadMore View显示的默认高度
        setHeight(DisplayUtils.dpToPx(context, mDefaultHeight));
        return LayoutInflater.from(context).inflate(R.layout.rv_load_more_layout, null);

    }
}
