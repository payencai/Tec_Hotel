package com.tec.hotel_com.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tec.hotel_com.R;
import com.tec.hotel_com.widget.city_modle.CityJsonReadUtil;
import com.tec.hotel_com.widget.city_modle.WheelView;
import com.tec.hotel_com.widget.city_modle.bean.CityBean;
import com.tec.hotel_com.widget.city_modle.bean.DistrictBean;
import com.tec.hotel_com.widget.city_modle.bean.ProvinceBean;
import com.tec.hotel_com.widget.city_modle.WheelView.OnWheelViewListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：凌涛 on 2018/10/17 10:05
 * 邮箱：771548229@qq..com
 */
public class CityPickerView extends LinearLayout {

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    ArrayList<ProvinceBean> mProvinceBeanArrayList = new ArrayList();
    ArrayList<ArrayList<CityBean>> mCityBeanArrayList;
    ArrayList<ArrayList<ArrayList<DistrictBean>>> mDistrictBeanArrayList;
    protected Map<String, CityBean[]> mPro_CityMap = new HashMap();
    protected Map<String, DistrictBean[]> mCity_DisMap = new HashMap();
    protected Map<String, DistrictBean> mDisMap = new HashMap();
    private ProvinceBean[] mProvinceBeenArray;
    private String defaultProvinceName = "江苏";
    private String defaultCityName = "常州";
    private String defaultDistrict = "新北区";
    private ProvinceBean mProvinceBean;
    private CityBean mCityBean;
    private DistrictBean mDistrictBean;

    public CityPickerView(Context context) {
        super(context);
        this.initView();
    }

    public CityPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public CityPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }

    private void initView() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.citypick_layout, this);

        this.mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        this.mViewCity = (WheelView) view.findViewById(R.id.id_city);
        this.mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
        this.mViewProvince.setOnWheelViewListener(new OnWheelViewListener() {
            public void onSelected(int selectedIndex, String item) {
                CityPickerView.this.updateCities();
            }
        });
        this.mViewCity.setOnWheelViewListener(new OnWheelViewListener() {
            public void onSelected(int selectedIndex, String item) {
                CityPickerView.this.updateAreas();
            }
        });
        this.mViewDistrict.setOnWheelViewListener(new OnWheelViewListener() {
            public void onSelected(int selectedIndex, String item) {
                CityPickerView.this.mDistrictBean = ((DistrictBean[]) CityPickerView.this.mCity_DisMap.get(CityPickerView.this.mProvinceBean.getName() + CityPickerView.this.mCityBean.getName()))[CityPickerView.this.mViewDistrict.getSeletedIndex()];
            }
        });
        this.initProvinceDatas(this.getContext());
        this.setUpData();
    }

    public String getName() {
        return this.getProvince() + this.getCity() + this.getDistrict();
    }

    public String getProvince() {
        return this.mProvinceBean.getName();
    }

    public String getCity() {
        return this.mCityBean.getName();
    }

    public String getDistrict() {
        return this.mDistrictBean.getName();
    }

    protected void initProvinceDatas(Context context) {
        String cityJson = CityJsonReadUtil.getJson(context, "city_20170724.json");
        Type type = (new TypeToken<ArrayList<ProvinceBean>>() {
        }).getType();
        this.mProvinceBeanArrayList = (ArrayList) (new Gson()).fromJson(cityJson, type);
        this.mCityBeanArrayList = new ArrayList(mProvinceBeanArrayList.size());
        this.mDistrictBeanArrayList = new ArrayList(mProvinceBeanArrayList.size());
        if (this.mProvinceBeanArrayList != null && !this.mProvinceBeanArrayList.isEmpty()) {
            this.mProvinceBean = (ProvinceBean) this.mProvinceBeanArrayList.get(0);
            List<CityBean> cityList = this.mProvinceBean.getCityList();
            if (cityList != null && !cityList.isEmpty() && cityList.size() > 0) {
                this.mCityBean = (CityBean) cityList.get(0);
                List<DistrictBean> districtList = this.mCityBean.getCityList();
                if (districtList != null && !districtList.isEmpty() && districtList.size() > 0) {
                    this.mDistrictBean = (DistrictBean) districtList.get(0);
                }
            }
        }

        this.mProvinceBeenArray = new ProvinceBean[this.mProvinceBeanArrayList.size()];

        for (int p = 0; p < this.mProvinceBeanArrayList.size(); ++p) {
            ProvinceBean itemProvince = (ProvinceBean) this.mProvinceBeanArrayList.get(p);
            ArrayList<CityBean> cityList = itemProvince.getCityList();
            CityBean[] cityNames = new CityBean[cityList.size()];

            for (int j = 0; j < cityList.size(); ++j) {
                cityNames[j] = (CityBean) cityList.get(j);
                List<DistrictBean> districtList = ((CityBean) cityList.get(j)).getCityList();
                DistrictBean[] distrinctArray = new DistrictBean[districtList.size()];

                for (int k = 0; k < districtList.size(); ++k) {
                    DistrictBean districtModel = (DistrictBean) districtList.get(k);
                    this.mDisMap.put(itemProvince.getName() + cityNames[j].getName() + ((DistrictBean) districtList.get(k)).getName(), districtModel);
                    distrinctArray[k] = districtModel;
                }

                this.mCity_DisMap.put(itemProvince.getName() + cityNames[j].getName(), distrinctArray);
            }

            this.mPro_CityMap.put(itemProvince.getName(), cityNames);
            this.mCityBeanArrayList.add(cityList);
            ArrayList<ArrayList<DistrictBean>> array2DistrictLists = new ArrayList(cityList.size());

            for (int c = 0; c < cityList.size(); ++c) {
                CityBean cityBean = (CityBean) cityList.get(c);
                array2DistrictLists.add(cityBean.getCityList());
            }

            this.mDistrictBeanArrayList.add(array2DistrictLists);
            this.mProvinceBeenArray[p] = itemProvince;
        }

    }

    private void setUpData() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(this.defaultProvinceName) && this.mProvinceBeenArray.length > 0) {
            for (int i = 0; i < this.mProvinceBeenArray.length; ++i) {
                if (this.mProvinceBeenArray[i].getName().contains(this.defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }

        if (-1 != provinceDefault) {
            this.mViewProvince.setSeletion(provinceDefault);
        }

        ArrayList<String> strings = new ArrayList();

        for (int i = 0; i < this.mProvinceBeenArray.length; ++i) {
            strings.add(this.mProvinceBeenArray[i].getName());
        }

        this.mViewProvince.setItems(strings);
        this.updateCities();
        this.updateAreas();
    }

    private void updateCities() {
        int pCurrent = this.mViewProvince.getSeletedIndex();
        this.mProvinceBean = this.mProvinceBeenArray[pCurrent];
        CityBean[] cities = (CityBean[]) this.mPro_CityMap.get(this.mProvinceBean.getName());
        if (cities != null) {
            int cityDefault = -1;
            if (!TextUtils.isEmpty(this.defaultCityName) && cities.length > 0) {
                for (int i = 0; i < cities.length; ++i) {
                    if (this.defaultCityName.contains(cities[i].getName())) {
                        cityDefault = i;
                        break;
                    }
                }
            }

            if (-1 != cityDefault) {
                this.mViewCity.setSeletion(cityDefault);
            } else {
                this.mViewCity.setSeletion(0);
            }

            ArrayList<String> strings = new ArrayList();

            for (int i = 0; i < cities.length; ++i) {
                strings.add(cities[i].getName());
            }

            this.mViewCity.setItems(strings);
            this.updateAreas();
        }
    }

    private void updateAreas() {
        int pCurrent = this.mViewCity.getSeletedIndex();
        this.mCityBean = ((CityBean[]) this.mPro_CityMap.get(this.mProvinceBean.getName()))[pCurrent];
        DistrictBean[] areas = (DistrictBean[]) this.mCity_DisMap.get(this.mProvinceBean.getName() + this.mCityBean.getName());
        if (areas != null) {
            int districtDefault = -1;
            if (!TextUtils.isEmpty(this.defaultDistrict) && areas.length > 0) {
                for (int i = 0; i < areas.length; ++i) {
                    if (this.defaultDistrict.contains(areas[i].getName())) {
                        districtDefault = i;
                        break;
                    }
                }
            }

            ArrayList<String> strings = new ArrayList();

            for (int i = 0; i < areas.length; ++i) {
                strings.add(areas[i].getName());
            }

            this.mViewDistrict.setItems(strings);
            if (-1 != districtDefault) {
                this.mViewDistrict.setSeletion(districtDefault);
                this.mDistrictBean = (DistrictBean) this.mDisMap.get(this.mProvinceBean.getName() + this.mCityBean.getName() + this.defaultDistrict);
            } else {
                this.mViewDistrict.setSeletion(0);
                if (areas.length > 0) {
                    this.mDistrictBean = areas[0];
                }
            }

        }
    }
}
