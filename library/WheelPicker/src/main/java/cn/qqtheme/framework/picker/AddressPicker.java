package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.widget.WheelView;

/**
 * 地址选择器（包括省级、地级、县级）。
 * 地址数据见示例项目的“assets/city.json”，来源于国家统计局官网（http://www.stats.gov.cn/tjsj/tjbz/xzqhdm）
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/15
 */
public class AddressPicker extends LinkagePicker {
    private OnAddressPickListener onAddressPickListener;
    /**
     * 只显示地市及区县
     */
    private boolean hideProvince = false;
    /**
     * 只显示省份及地市
     */
    private boolean hideCounty = false;
    /**
     * 省市县数据
     */
    private List<Province> provinceList = new ArrayList<Province>();

    public AddressPicker(Activity activity, ArrayList<Province> data) {
        super(activity);
        parseData(data);
    }

    private void parseData(ArrayList<Province> data) {
        int provinceSize = data.size();
        provinceList.clear();
        provinceList.addAll(data);
        //添加省
        for (int x = 0; x < provinceSize; x++) {
            Province pro = data.get(x);
            firstList.add(pro.getAreaName());
            ArrayList<City> cities = pro.getCities();
            ArrayList<String> xCities = new ArrayList<String>();
            ArrayList<ArrayList<String>> xCounties = new ArrayList<ArrayList<String>>();
            int citySize = cities.size();
            //添加地市
            for (int y = 0; y < citySize; y++) {
                City cit = cities.get(y);
                xCities.add(cit.getAreaName());
                ArrayList<County> counties = cit.getCounties();
                ArrayList<String> yCounties = new ArrayList<String>();
                int countySize = counties.size();
                //添加区县
                if (countySize == 0) {
                    yCounties.add(cit.getAreaName());
                } else {
                    for (int z = 0; z < countySize; z++) {
                        yCounties.add(counties.get(z).getAreaName());
                    }
                }
                xCounties.add(yCounties);
            }
            secondList.add(xCities);
            thirdList.add(xCounties);
        }
    }

    /**
     * Sets selected item.
     *
     * @param province the province
     * @param city     the city
     * @param county   the county
     */
    public void setSelectedItem(String province, String city, String county) {
        super.setSelectedItem(province, city, county);
    }

    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见示例中的“assets/city2.json”
     *
     * @param hideProvince the hide province
     */
    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    /**
     * 隐藏县级行政区，只显示省级和市级。
     * 设置为true的话，hideProvince将强制为false
     * 数据源依然使用“assets/city.json” 仅在逻辑上隐藏县级选择框，实际项目中应该去掉县级数据。
     *
     * @param hideCounty the hide county
     */
    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    /**
     * Sets on address pick listener.
     *
     * @param listener the listener
     */
    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }

    @Deprecated
    @Override
    public void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        throw new UnsupportedOperationException("Please use setOnAddressPickListener instead.");
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        if (hideCounty) {
            hideProvince = false;
        }
        if (firstList.size() == 0) {
            throw new IllegalArgumentException("please initial data at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        final WheelView provinceView = new WheelView(activity);
        final int width = screenWidthPixels / 3;
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(width, WRAP_CONTENT));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineVisible(lineVisible);
        provinceView.setLineColor(lineColor);
        provinceView.setOffset(offset);
        layout.addView(provinceView);
        if (hideProvince) {
            provinceView.setVisibility(View.GONE);
        }
        final WheelView cityView = new WheelView(activity);
        cityView.setLayoutParams(new LinearLayout.LayoutParams(width, WRAP_CONTENT));
        cityView.setTextSize(textSize);
        cityView.setTextColor(textColorNormal, textColorFocus);
        cityView.setLineVisible(lineVisible);
        cityView.setLineColor(lineColor);
        cityView.setOffset(offset);
        layout.addView(cityView);
        final WheelView countyView = new WheelView(activity);
        countyView.setLayoutParams(new LinearLayout.LayoutParams(width, WRAP_CONTENT));
        countyView.setTextSize(textSize);
        countyView.setTextColor(textColorNormal, textColorFocus);
        countyView.setLineVisible(lineVisible);
        countyView.setLineColor(lineColor);
        countyView.setOffset(offset);
        layout.addView(countyView);
        if (hideCounty) {
            countyView.setVisibility(View.GONE);
        }
        provinceView.setItems(firstList, selectedFirstIndex);
        provinceView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedFirstText = item;
                selectedFirstIndex = selectedIndex;
                selectedThirdIndex = 0;
                //根据省份获取地市。若不是用户手动滚动，说明联动需要指定默认项
                cityView.setItems(secondList.get(selectedFirstIndex), isUserScroll ? 0 : selectedSecondIndex);
                //根据地市获取区县
                ArrayList<ArrayList<String>> tmp = thirdList.get(selectedFirstIndex);
                if (tmp.size() > 0) {
                    countyView.setItems(tmp.get(0), isUserScroll ? 0 : selectedThirdIndex);
                } else {
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });
        cityView.setItems(secondList.get(selectedFirstIndex), selectedSecondIndex);
        cityView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedSecondText = item;
                selectedSecondIndex = selectedIndex;
                //根据地市获取区县
                ArrayList<String> tmp = thirdList.get(selectedFirstIndex).get(selectedSecondIndex);
                if (tmp.size() > 0) {
                    countyView.setItems(tmp, isUserScroll ? 0 : selectedThirdIndex);
                } else {
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });
        countyView.setItems(thirdList.get(selectedFirstIndex).get(selectedSecondIndex), selectedThirdIndex);
        countyView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedThirdText = item;
                selectedThirdIndex = selectedIndex;
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
            Province province = provinceList.get(selectedFirstIndex);
            City city = provinceList.get(selectedFirstIndex).getCities().get(selectedSecondIndex);
            County county = null;
            if (!hideCounty) {
                county = provinceList.get(selectedFirstIndex).getCities().get(selectedSecondIndex).getCounties().get(selectedThirdIndex);
            }
            onAddressPickListener.onAddressPicked(province, city, county);
        }
    }

    /**
     * 地址选择回调
     */
    public interface OnAddressPickListener {

        /**
         * 选择地址
         *
         * @param province the province
         * @param city     the city
         * @param county   the county ，if {@hideCounty} is true，this is null
         */
        void onAddressPicked(Province province, City city, County county);

    }

    /**
     * 省市县抽象，本类及其子类不可混淆
     */
    public abstract static class Area {
        private String areaId;
        private String areaName;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        @Override
        public String toString() {
            return "areaId=" + areaId + ",areaName=" + areaName;
        }

    }

    /**
     * 省份
     */
    public static class Province extends Area {
        private ArrayList<City> cities = new ArrayList<City>();

        public ArrayList<City> getCities() {
            return cities;
        }

        public void setCities(ArrayList<City> cities) {
            this.cities = cities;
        }

    }

    /**
     * 地市
     */
    public static class City extends Area {
        private ArrayList<County> counties = new ArrayList<County>();

        public ArrayList<County> getCounties() {
            return counties;
        }

        public void setCounties(ArrayList<County> counties) {
            this.counties = counties;
        }

    }

    /**
     * 区县
     */
    public static class County extends Area {
    }

}
