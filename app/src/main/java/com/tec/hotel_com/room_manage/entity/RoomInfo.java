package com.tec.hotel_com.room_manage.entity;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseCell;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.room_manage.activity.AddRoomActivity;
import com.tec.hotel_com.room_manage.activity.OneMouthDataActivity;
import com.tec.hotel_com.room_manage.adapter.OptionAdapter;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.widget.CustomPopWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：凌涛 on 2018/8/6 11:04
 * 邮箱：771548229@qq..com
 */
public class RoomInfo extends RVBaseCell implements Serializable {


    private String id;
    private String hotelId;
    private String setOutAmount;
    private double hotelPrice;
    private double managePrice;
    private String name;
    private String describe;
    private String image1;
    private String image1Url;
    private String image2;
    private String image2Url;
    private String image3;
    private String image3Url;
    private String image4;
    private String image4Url;
    private String image5;
    private String image5Url;
    private String createTime;
    private String updateTime;
    private String sequence;
    private int roomNum;
    private String liveNum;
    private String bedNum;
    private String floor;
    private String bedType;
    private String window;
    private String bathroom;
    private String intnet;
    private String breakfast;

    private String isCanCancel;
    private String isUseCoupon;
    private String isUsed;
    private String isDeleted;
    private String regulations;

    public RoomInfo() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roominfo_rv_layout, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position, final Context context, RVBaseAdapter adapter) {

//        final RecyclerView holderView = (RecyclerView) holder.getView(R.id.hotel_type_rv);
//        holderView.setLayoutManager(new LinearLayoutManager(context));
//        RVBaseAdapter<RoomItem> roomItemAdapter = new RVBaseAdapter<RoomItem>() {
//            @Override
//            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {
//
//            }
//        };
//        roomItemAdapter.setData(mRoomInfoList);
//        holderView.setAdapter(roomItemAdapter);

//        final ImageView openOrCloseImg = holder.getImageView(R.id.openOrClose);
//        openOrCloseImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (openOrCloseImg.isSelected()) {
//                    openOrCloseImg.setSelected(false);
//                    holderView.setVisibility(View.VISIBLE);
//                } else {
//                    openOrCloseImg.setSelected(true);
//                    holderView.setVisibility(View.GONE);
//                }
//            }
//        });
        holder.getTextView(R.id.add_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneMouthDataActivity.startOneMonthActivity(context, RoomInfo.this);
            }
        });
        holder.getImageView(R.id.hotel_room_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRoomActivity.startAddRoomOrChange(context, 3, RoomInfo.this);
            }
        });
        holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRoomActivity.startAddRoomOrChange(context, 2, RoomInfo.this);
            }
        });

        holder.getImageView(R.id.hotel_room_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToaskUtil.showToast(context, "删除全部");
            }
        });

        holder.setText(R.id.price, "定价：¥" + hotelPrice);
        holder.setText(R.id.roomTypeNumber, "数量：" + roomNum + "个");
        holder.setText(R.id.name, name);
        holder.setText(R.id.breakfast, breakfast.equals("1") ? "早餐:有" : "早餐:没有");
        holder.setText(R.id.isCanCancel, isCanCancel.equals("1") ? "是否可取消：是" : "是否可取消：否");
//        holder.setText(R.id.isCanCancel, isUseCoupon.equals("1")?"是否可取消：是":"是否可取消：否");
        if (!TextUtils.isEmpty(describe)) {
            holder.setText(R.id.des, describe.equals("null") ? "房间描述：" : "房间描述：" + describe);
        } else {
            holder.setText(R.id.des, "房间描述：");
        }

    }

    private void showPwBrandSelector(View view, Context context) {
        View brandView = LayoutInflater.from(context).inflate(R.layout.pw_list_selector, null);
        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(brandView)
                .sizeByPercentage(context, 0.7f, 0.5f)
                .setOutsideTouchable(true)
                .enableBackgroundDark(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle)
                .setBgDarkAlpha(0.5f)
                .create();
        handlerBrandView(context, brandView, customPopWindow);
        customPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void handlerBrandView(final Context context, View view,
                                  final CustomPopWindow customPopWindow) {
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
            }
        });
        ListView pwList = (ListView) view.findViewById(R.id.pwList);
        List<String> list = new ArrayList<>();
        list.add("飞利浦（PHILIPS）");
        list.add("卓尔（ZOLL）");
        list.add("迈瑞（mindray）");
        list.add("普美康（PRIMEDIC）");
        list.add("日本光电（NIHON KOHDEN）");
        list.add("瑞士席勒（SCHILLER）");
        list.add("捷斯特（CHEST）");
        OptionAdapter adapter = new OptionAdapter(context, list);
        pwList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemString = (String) parent.getItemAtPosition(position);
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
                ToaskUtil.showToast(context, "修改状态成功");
            }
        });
        pwList.setAdapter(adapter);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getSetOutAmount() {
        return setOutAmount;
    }

    public void setSetOutAmount(String setOutAmount) {
        this.setOutAmount = setOutAmount;
    }

    public double getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(double hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public double getManagePrice() {
        return managePrice;
    }

    public void setManagePrice(double managePrice) {
        this.managePrice = managePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage1Url() {
        return image1Url;
    }

    public void setImage1Url(String image1Url) {
        this.image1Url = image1Url;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage2Url() {
        return image2Url;
    }

    public void setImage2Url(String image2Url) {
        this.image2Url = image2Url;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage3Url() {
        return image3Url;
    }

    public void setImage3Url(String image3Url) {
        this.image3Url = image3Url;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage4Url() {
        return image4Url;
    }

    public void setImage4Url(String image4Url) {
        this.image4Url = image4Url;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage5Url() {
        return image5Url;
    }

    public void setImage5Url(String image5Url) {
        this.image5Url = image5Url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public String getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(String liveNum) {
        this.liveNum = liveNum;
    }

    public String getBedNum() {
        return bedNum;
    }

    public void setBedNum(String bedNum) {
        this.bedNum = bedNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getIntnet() {
        return intnet;
    }

    public void setIntnet(String intnet) {
        this.intnet = intnet;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getIsCanCancel() {
        return isCanCancel;
    }

    public void setIsCanCancel(String isCanCancel) {
        this.isCanCancel = isCanCancel;
    }

    public String getIsUseCoupon() {
        return isUseCoupon;
    }

    public void setIsUseCoupon(String isUseCoupon) {
        this.isUseCoupon = isUseCoupon;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRegulations() {
        return regulations;
    }

    public void setRegulations(String regulations) {
        this.regulations = regulations;
    }
}
