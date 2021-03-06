package com.tec.hotel_com.common.rv.base;

import android.content.Context;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * 一种 ViewType 对应一个Cell
 * Created by HIAPAD on 2017/12/2.
 */

public interface Cell extends Serializable{

    /**
     * 回收资源
     */
    public void releaseResource();

    /**
     * 获取viewType
     *
     * @return
     */
    public int getItemType();

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);


    /**
     * 数据绑定
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RVBaseViewHolder holder, int position, Context context, RVBaseAdapter adapter);


}
