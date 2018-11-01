package com.tec.hotel_com.common.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseCell;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;


import java.io.Serializable;

/**
 * 作者：凌涛 on 2018/10/22 15:41
 * 邮箱：771548229@qq..com
 */
public class Notify extends RVBaseCell implements Serializable {

    /**
     * content : string
     * createTime : 2018-10-22T06:39:21.393Z
     * id : string
     * systemId : string
     * travelAgencyId : string
     * typeCode : string
     * typeName : string
     * userId : string
     */

    private String content;
    private String createTime;
    private String id;
    private String systemId;
    private String travelAgencyId;
    private String typeCode;
    private String typeName;
    private String userId;

    public Notify(Object o) {
        super(null);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getTravelAgencyId() {
        return travelAgencyId;
    }

    public void setTravelAgencyId(String travelAgencyId) {
        this.travelAgencyId = travelAgencyId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false);
        RVBaseViewHolder holder = new RVBaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position, Context context, RVBaseAdapter adapter) {
             holder.setText(R.id.content,content);
             holder.setText(R.id.time,createTime);
    }
}
