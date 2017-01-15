package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 地址选择器（包括省级、地级、县级），地址数据见示例项目assets目录下。
 * “assets/city.json”转换自国家统计局（http://www.stats.gov.cn/tjsj/tjbz/xzqhdm）
 * “assets/area.db”来源于开源项目（https://github.com/chihane/JDAddressSelector）
 *
 * @author 李玉江[QQ:1032694760]
 * @see Province
 * @see City
 * @see County
 * @since 2015/12/15, 2016/12/18
 */
public class AddressPicker extends LinkagePicker {
    private OnAddressPickListener onAddressPickListener;
    private OnWheelListener onWheelListener;
    //只显示地市及区县
    private boolean hideProvince = false;
    //只显示省份及地市
    private boolean hideCounty = false;
    //省市区数据
    private ArrayList<Province> provinces = new ArrayList<Province>();

    public AddressPicker(Activity activity, ArrayList<Province> provinces) {
        super(activity, new AddressProvider(provinces));
        this.provinces = provinces;
    }

    /**
     * 设置默认选中的省市县
     */
    public void setSelectedItem(String province, String city, String county) {
        super.setSelectedItem(province, city, county);
    }

    public Province getSelectedProvince() {
        return provinces.get(selectedFirstIndex);
    }

    public City getSelectedCity() {
        return getSelectedProvince().getCities().get(selectedSecondIndex);
    }

    public County getSelectedCounty() {
        return getSelectedCity().getCounties().get(selectedThirdIndex);
    }

    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见示例中的“assets/city2.json”
     */
    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    /**
     * 隐藏县级行政区，只显示省级和市级。
     * 设置为true的话，hideProvince将强制为false
     * 数据源依然使用“assets/city.json” 仅在逻辑上隐藏县级选择框，实际项目中应该去掉县级数据。
     */
    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    /**
     * 设置滑动监听器
     */
    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }

    /**
     * @deprecated use {@link #setOnAddressPickListener(OnAddressPickListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        throw new UnsupportedOperationException("Please use setOnAddressPickListener instead.");
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        if (null == provider) {
            throw new IllegalArgumentException("please set address provider before make view");
        }
        if (hideCounty) {
            hideProvince = false;
        }
        int[] widths = getColumnWidths(hideProvince || hideCounty);
        int provinceWidth = widths[0];
        int cityWidth = widths[1];
        int countyWidth = widths[2];
        if (hideProvince) {
            provinceWidth = 0;
            cityWidth = widths[0];
            countyWidth = widths[1];
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        final WheelView provinceView = new WheelView(activity);
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(provinceWidth, WRAP_CONTENT));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineConfig(lineConfig);
        provinceView.setOffset(offset);
        provinceView.setCycleDisable(cycleDisable);
        layout.addView(provinceView);
        if (hideProvince) {
            provinceView.setVisibility(View.GONE);
        }

        final WheelView cityView = new WheelView(activity);
        cityView.setLayoutParams(new LinearLayout.LayoutParams(cityWidth, WRAP_CONTENT));
        cityView.setTextSize(textSize);
        cityView.setTextColor(textColorNormal, textColorFocus);
        cityView.setLineConfig(lineConfig);
        cityView.setOffset(offset);
        cityView.setCycleDisable(cycleDisable);
        layout.addView(cityView);

        final WheelView countyView = new WheelView(activity);
        countyView.setLayoutParams(new LinearLayout.LayoutParams(countyWidth, WRAP_CONTENT));
        countyView.setTextSize(textSize);
        countyView.setTextColor(textColorNormal, textColorFocus);
        countyView.setLineConfig(lineConfig);
        countyView.setOffset(offset);
        countyView.setCycleDisable(cycleDisable);
        layout.addView(countyView);
        if (hideCounty) {
            countyView.setVisibility(View.GONE);
        }

        provinceView.setItems(provider.provideFirstData(), selectedFirstIndex);
        provinceView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedFirstItem = item;
                selectedFirstIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onProvinceWheeled(selectedFirstIndex, selectedFirstItem);
                }
                if (!isUserScroll) {
                    return;
                }
                LogUtils.verbose(this, "change cities after province wheeled");
                selectedSecondIndex = 0;//重置地级索引
                selectedThirdIndex = 0;//重置县级索引
                //根据省份获取地市
                List<String> cities = provider.provideSecondData(selectedFirstIndex);
                if (cities.size() > 0) {
                    cityView.setItems(cities, selectedSecondIndex);
                } else {
                    cityView.setItems(new ArrayList<String>());
                }
                //根据地市获取区县
                List<String> counties = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                if (counties.size() > 0) {
                    countyView.setItems(counties, selectedThirdIndex);
                } else {
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });

        cityView.setItems(provider.provideSecondData(selectedFirstIndex), selectedSecondIndex);
        cityView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedSecondItem = item;
                selectedSecondIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onCityWheeled(selectedSecondIndex, selectedSecondItem);
                }
                if (!isUserScroll) {
                    return;
                }
                LogUtils.verbose(this, "change counties after city wheeled");
                selectedThirdIndex = 0;//重置县级索引
                //根据地市获取区县
                List<String> counties = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                if (counties.size() > 0) {
                    //若不是用户手动滚动，说明联动需要指定默认项
                    countyView.setItems(counties, selectedThirdIndex);
                } else {
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });

        countyView.setItems(provider.provideThirdData(selectedFirstIndex, selectedSecondIndex), selectedThirdIndex);
        countyView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedThirdItem = item;
                selectedThirdIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onCountyWheeled(selectedThirdIndex, selectedThirdItem);
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
            Province province = getSelectedProvince();
            City city = getSelectedCity();
            County county = null;
            if (!hideCounty) {
                county = getSelectedCounty();
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
         * @param county   the county ，if {@code hideCounty} is true，this is null
         */
        void onAddressPicked(Province province, City city, County county);

    }

    /**
     * 滑动回调
     */
    public interface OnWheelListener {

        void onProvinceWheeled(int index, String province);

        void onCityWheeled(int index, String city);

        void onCountyWheeled(int index, String county);

    }

    /**
     * 地址提供者
     */
    public static class AddressProvider implements DataProvider {
        private List<String> firstList = new ArrayList<>();
        private List<List<String>> secondList = new ArrayList<>();
        private List<List<List<String>>> thirdList = new ArrayList<>();

        public AddressProvider(List<Province> provinces) {
            parseData(provinces);
        }

        @Override
        public boolean isOnlyTwo() {
            return thirdList.size() == 0;
        }

        @Override
        public List<String> provideFirstData() {
            return firstList;
        }

        @Override
        public List<String> provideSecondData(int firstIndex) {
            return secondList.get(firstIndex);
        }

        @Override
        public List<String> provideThirdData(int firstIndex, int secondIndex) {
            return thirdList.get(firstIndex).get(secondIndex);
        }

        private void parseData(List<Province> data) {
            int provinceSize = data.size();
            //添加省
            for (int x = 0; x < provinceSize; x++) {
                Province pro = data.get(x);
                firstList.add(pro.getAreaName());
                List<City> cities = pro.getCities();
                List<String> xCities = new ArrayList<>();
                List<List<String>> xCounties = new ArrayList<>();
                int citySize = cities.size();
                //添加地市
                for (int y = 0; y < citySize; y++) {
                    City cit = cities.get(y);
                    cit.setProvinceId(pro.getAreaId());
                    xCities.add(cit.getAreaName());
                    List<County> counties = cit.getCounties();
                    ArrayList<String> yCounties = new ArrayList<>();
                    int countySize = counties.size();
                    //添加区县
                    if (countySize == 0) {
                        yCounties.add(cit.getAreaName());
                    } else {
                        for (int z = 0; z < countySize; z++) {
                            County cou = counties.get(z);
                            cou.setCityId(cit.getAreaId());
                            yCounties.add(cou.getAreaName());
                        }
                    }
                    xCounties.add(yCounties);
                }
                secondList.add(xCities);
                thirdList.add(xCounties);
            }
        }

    }

}
