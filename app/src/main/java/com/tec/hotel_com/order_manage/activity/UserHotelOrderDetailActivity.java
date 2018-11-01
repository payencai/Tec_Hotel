package com.tec.hotel_com.order_manage.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.order_manage.entity.LxsHotelOrderDetailBean;
import com.tec.hotel_com.order_manage.entity.UserHotelOrderDetailBean;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.widget.KyLoadingBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class UserHotelOrderDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.productNum)
    TextView productNum;
    @BindView(R.id.timeInfo)
    TextView timeInfo;

    @BindView(R.id.bedTypeAndOther)
    TextView bedTypeAndOther;


    @BindView(R.id.checkInPerson)
    TextView checkInPerson;
    @BindView(R.id.telephoneNum)
    TextView telephoneNum;
    @BindView(R.id.peopleNum)
    TextView peopleNum;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.paymentTime)
    TextView paymentTime;
    @BindView(R.id.modeOfPayment)
    TextView modeOfPayment;
    @BindView(R.id.liveNum)
    TextView liveNum;

    @BindView(R.id.cancelApplyLayout)
    LinearLayout cancelApplyLayout;
    @BindView(R.id.cancelResronLayout)
    LinearLayout cancelResronLayout;
    @BindView(R.id.cancelSucceedLayout)
    LinearLayout cancelSucceedLayout;

    @BindView(R.id.cancelText)
    TextView cancelText;
    @BindView(R.id.reasonText)
    TextView reasonText;
    @BindView(R.id.cancelApplyCanceTime)
    TextView cancelApplyCanceTime;
    @BindView(R.id.cancelSucceedText)
    TextView cancelSucceedText;


//    //申请取消
//    @BindView(R.id.applyCanceTime)
//    TextView applyCanceTime;
//    @BindView(R.id.applyCancelLayout)
//    LinearLayout applyCancelLayout;
//
//    //申请取消成功
//    @BindView(R.id.applyCancelSucceedTime)
//    TextView applyCancelSucceedTime;
//    @BindView(R.id.applyCancelSucceedLayout)
//    LinearLayout applyCancelSucceedLayout;

    //    //取消原因
//    @BindView(R.id.cancelReasonText)
//    TextView cancelReasonText;
//    @BindView(R.id.cancelReasonLayout)
//    LinearLayout cancelReasonLayout;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.orderSource)
    TextView orderSource;

    @BindView(R.id.commentLayout)
    LinearLayout commentLayout;
    @BindView(R.id.commentTime)
    TextView commentTime;
    @BindView(R.id.commentScore)
    TextView commentScore;
    @BindView(R.id.commentContent)
    TextView commentContent;

    @BindView(R.id.bottom_column)
    RelativeLayout bottom_column;

    @BindView(R.id.left_btn)
    TextView left_btn;
    @BindView(R.id.right_btn)
    TextView right_btn;


    private UserHotelOrderDetailBean mUserOrderBeanDetail;
    private LxsHotelOrderDetailBean mLxsOrderBeanDetail;

    private KyLoadingBuilder mApplyOrderCancelView;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    //订单id
    private String mOrderId;
    private int intoType = -1;

    /**
     * @param activity
     * @param orderId  订单id
     * @param type     1为用户订单  2为旅行社订单
     */
    public static final void startHotelOrderDetailActivity(Context activity, String orderId, int type) {
        Intent intent = new Intent(activity, UserHotelOrderDetailActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("type", type);
        activity.startActivity(intent);

    }

    @Override
    protected int getContentId() {
        return R.layout.activity_user_hotel_order_detail;
    }


    @Override
    protected void initView() {
        Intent intent = getIntent();
        mOrderId = intent.getStringExtra("orderId");
        intoType = intent.getIntExtra("type", -1);
        if (TextUtils.isEmpty(mOrderId)) {
            finish();
            return;
        }

        String url = "";
        if (intoType == 1) {
            url = PlatformContans.Hotel.getForUserHotel;
        } else {
            url = PlatformContans.Hotel.getForLxsHotel;
        }
        requestOrderDetail(url);
    }

    private void requestOrderDetail(String url) {
        Map<String, Object> parame = new HashMap<>();
        parame.put("id", mOrderId);
        HttpProxy.obtain().get(url, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("userOrderDetail", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        if (intoType == 1) {
                            mUserOrderBeanDetail = new Gson().fromJson(object.getJSONObject("data").toString(), UserHotelOrderDetailBean.class);
                            setUpdateUserUI(mUserOrderBeanDetail);
                        } else {
                            mLxsOrderBeanDetail = new Gson().fromJson(object.getJSONObject("data").toString(), LxsHotelOrderDetailBean.class);
                            setUpdateLxsUI(mLxsOrderBeanDetail);
                        }
                    } else {
                        ToaskUtil.showToast(UserHotelOrderDetailActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });
    }

    private void setUpdateUserUI(UserHotelOrderDetailBean bean) {

        UserHotelOrderDetailBean.HotelOrderBean hotelOrder = bean.getHotelOrder();
        UserHotelOrderDetailBean.HotelBean hotel = bean.getHotel();

        productNum.setText(hotelOrder.getRoomtypeName());
        liveNum.setText(hotelOrder.getOrderRoomNum() + "间");


        String firstDate = hotelOrder.getFirstDate();
        String lastDate = hotelOrder.getLastDate();

        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date firstParse = null;
        Date endParse = null;
        try {
            firstParse = format0.parse(firstDate);
            endParse = format0.parse(lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String start = format.format(firstParse);
        String end = format.format(endParse);

        timeInfo.setText("入住:" + start + "--离店" + end + "\t\t" + hotelOrder.getDays() + "晚");
        bedTypeAndOther.setText(hotelOrder.getBedType() + " | " + hotelOrder.getBreakfast());
        amount.setText("合计:￥ " + hotelOrder.getAmount());

        checkInPerson.setText(hotelOrder.getCheckInPerson());
        telephoneNum.setText(hotelOrder.getTelephoneNum());
        peopleNum.setText(hotelOrder.getPeopleNum() + "人");

        orderId.setText("订单编号:" + hotelOrder.getOrderNum());
        paymentTime.setText(hotelOrder.getPaymentTime());

        String modeOfPayment = hotelOrder.getModeOfPayment();
        if (modeOfPayment.equals("1")) {
            this.modeOfPayment.setText("线上支付");
        } else {
            this.modeOfPayment.setText("线下支付");
        }

        setMainUI();


    }

    @SuppressLint("ResourceAsColor")
    private void setMainUI() {
        String status = "";
        if (intoType == 1) {
            status = mUserOrderBeanDetail.getHotelOrder().getStatus();
        } else {
            status = mLxsOrderBeanDetail.getHotelOrderOfTravelAgency().getStatus();

        }
//        String status = "1";
        if (status.equals("1")) {
            title.setText("待消费");
            cancelApplyLayout.setVisibility(View.GONE);
            left_btn.setVisibility(View.GONE);
            right_btn.setText("确定入住");
            right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToaskUtil.showToast(UserHotelOrderDetailActivity.this, right_btn.getText().toString());
                }
            });
        } else if (status.equals("2")) {
            title.setText("申请中");

            cancelApplyLayout.setVisibility(View.VISIBLE);
            cancelResronLayout.setVisibility(View.VISIBLE);

            left_btn.setVisibility(View.VISIBLE);
            left_btn.setText("拒绝取消");
            right_btn.setText("同意取消");
            left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToaskUtil.showToast(UserHotelOrderDetailActivity.this, left_btn.getText().toString());

                }
            });

            right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToaskUtil.showToast(UserHotelOrderDetailActivity.this, right_btn.getText().toString());

                }
            });
            if(mUserOrderBeanDetail!=null){
                List<UserHotelOrderDetailBean.RefundListBean> refundList = mUserOrderBeanDetail.getRefundList();
                UserHotelOrderDetailBean.RefundListBean bean = refundList.get(refundList.size() - 1);
                cancelApplyCanceTime.setText(bean.getCreateTime());
                reasonText.setText(bean.getRefundReason());
            }

        } else if (status.equals("3")) {
            title.setText("已取消");

            cancelApplyLayout.setVisibility(View.VISIBLE);
            cancelResronLayout.setVisibility(View.VISIBLE);
            cancelSucceedLayout.setVisibility(View.VISIBLE);

            bottom_column.setVisibility(View.GONE);
//            left_btn.setVisibility(View.GONE);
//            right_btn.setVisibility(View.GONE);

            if(mUserOrderBeanDetail!=null){
                List<UserHotelOrderDetailBean.RefundListBean> refundList = mUserOrderBeanDetail.getRefundList();
                try {
                    UserHotelOrderDetailBean.RefundListBean bean = refundList.get(refundList.size() - 1);
                    cancelApplyCanceTime.setText(bean.getDisposeTime() == null ? "" : bean.getDisposeTime());
                    cancelSucceedText.setText(bean.getDisposeTime() == null ? "" : bean.getDisposeTime());
                    reasonText.setText(bean.getRefundReason());

                } catch (IndexOutOfBoundsException e) {

                }
            }


        } else if (status.equals("0")) {
            title.setText("已完成");
            bottom_column.setVisibility(View.GONE);
            commentLayout.setVisibility(View.VISIBLE);
            if(mUserOrderBeanDetail!=null){
                UserHotelOrderDetailBean.HotelCommentBean hotelCommentBean=mUserOrderBeanDetail.getHotelComment();
                if(hotelCommentBean!=null){
                    commentTime.setText(hotelCommentBean.getCreateTime());
                    commentScore.setText(hotelCommentBean.getScore() + "分");
                    commentContent.setText(hotelCommentBean.getContent());
                }
            }

        } else if (status.equals("4")) {
            title.setText("已完成");
            bottom_column.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);

//            String createTime = mUserOrderBeanDetail.getHotelComment().getCreateTime();
//            commentTime.setText(createTime == null ? "" : createTime);
//            int score = mUserOrderBeanDetail.getHotelComment().getScore();
//            commentScore.setText(score + "分");
//            commentContent.setText(mUserOrderBeanDetail.getHotelComment().getContent());
        }

    }

    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date parse = format.parse(pTime);
        c.setTime(parse);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }

        return dayForWeek;
    }

    public static String getWeekString(int dayForWeek) {

        String weekString = "";
        if (dayForWeek == 1) {
            weekString = "周一";
        } else if (dayForWeek == 2) {
            weekString = "周二";

        } else if (dayForWeek == 3) {
            weekString = "周三";

        } else if (dayForWeek == 4) {
            weekString = "周四";

        } else if (dayForWeek == 5) {
            weekString = "周五";

        } else if (dayForWeek == 6) {
            weekString = "周六";

        } else if (dayForWeek == 7) {
            weekString = "周日";
        }
        return weekString;
    }


    private Date getDate(String times) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(times);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String getDate2(String times) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
            Date date = sdf.parse(times);
            String format = sdf2.format(date);
            return format;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @OnClick({})
    public void onViewClicked(View view) {
        switch (view.getId()) {

        }
    }

    public void setUpdateLxsUI(LxsHotelOrderDetailBean updateLxsUI) {

//        UserHotelOrderDetailBean.HotelOrderBean hotelOrder = bean.getHotelOrder();
//        UserHotelOrderDetailBean.HotelBean hotel = bean.getHotel();
        LxsHotelOrderDetailBean.HotelOrderOfTravelAgencyBean hotelOrder = updateLxsUI.getHotelOrderOfTravelAgency();
        String hotel = updateLxsUI.getHotel();


        productNum.setText(hotelOrder.getRoomtypeName());
        liveNum.setText(hotelOrder.getOrderRoomNum() + "间");


        String firstDate = hotelOrder.getFirstDate();
        String lastDate = hotelOrder.getLastDate();

        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date firstParse = null;
        Date endParse = null;
        try {
            firstParse = format0.parse(firstDate);
            endParse = format0.parse(lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String start = format.format(firstParse);
        String end = format.format(endParse);

        timeInfo.setText("入住:" + start + "--离店" + end + "\t\t" + hotelOrder.getDays() + "晚");
        bedTypeAndOther.setText(hotelOrder.getBedType() + " | " + hotelOrder.getBreakfast());
        amount.setText("合计:￥ " + hotelOrder.getAmount());

        checkInPerson.setText(hotelOrder.getCheckInPerson());
        telephoneNum.setText(hotelOrder.getTelephoneNum());
        peopleNum.setText(hotelOrder.getPeopleNum() + "人");

        orderId.setText("订单编号:" + hotelOrder.getOrderNum());
        paymentTime.setText(hotelOrder.getPaymentTime());

        String modeOfPayment = hotelOrder.getModeOfPayment();
        if (modeOfPayment.equals("1")) {
            this.modeOfPayment.setText("线上支付");
        } else {
            this.modeOfPayment.setText("线下支付");
        }

        setMainUI();

    }
}
