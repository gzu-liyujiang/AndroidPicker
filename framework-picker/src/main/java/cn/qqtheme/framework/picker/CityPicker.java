package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import cn.qqtheme.framework.helper.AssetsUtils;
import cn.qqtheme.framework.helper.LogUtils;

/**
 * 城市选择器。
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/26
 * Created By Android Studio
 */
public class CityPicker {
    private Activity activity;
    private OptionPicker picker;
    private OnCityPickListener onCityPickListener;
    private String currentProvince, currentCity, currentCounty;

    public CityPicker(Activity activity) {
        this.activity = activity;
        this.picker = new OptionPicker(activity);
    }

    public void setSelectedCity(String province, String city, String county) {
        this.currentProvince = province;
        this.currentCity = city;
        this.currentCounty = county;
    }

    public void setOnCityPickListener(OnCityPickListener onCityPickListener) {
        this.onCityPickListener = onCityPickListener;
    }

    public void showAtBottom() {
        new InitAreaTask().execute();
    }

    public interface OnCityPickListener {
        void onCityPicked(String province, String city, String county);
    }

    private class InitAreaTask extends AsyncTask<Void, Void, Boolean> {
        private ArrayList<String> province = new ArrayList<String>();
        private ArrayList<ArrayList<String>> city = new ArrayList<ArrayList<String>>();
        private ArrayList<ArrayList<ArrayList<String>>> county = new ArrayList<ArrayList<ArrayList<String>>>();
        private ProgressDialog dialog;

        public InitAreaTask() {
            dialog = ProgressDialog.show(activity, null, "正在初始化城市数据...", true, true);
        }

        @Override
        protected synchronized Boolean doInBackground(Void... params) {
            ArrayList<Province> datas = new ArrayList<Province>();
            try {
                String data = AssetsUtils.readText(activity, "city.json");
                datas.addAll(JSON.parseArray(data, Province.class));
                int provinceSize = datas.size();
                //添加省
                for (int x = 0; x < provinceSize; x++) {
                    Province pro = datas.get(x);
                    province.add(pro.getAreaName());
                    ArrayList<City> cities = pro.getCities();
                    ArrayList<String> xCities = new ArrayList<String>();
                    ArrayList<ArrayList<String>> xCounties = new ArrayList<ArrayList<String>>();
                    int citySize = cities.size();
                    //添加地市
                    for (int y = 0; y < citySize; y++) {
                        City cit = cities.get(y);
                        LogUtils.debug(cit.toString());
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
                    city.add(xCities);
                    county.add(xCounties);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {
                picker.setOptions(province, city, county);
                int currentProvinceIndex = 0, currentCityIndex = 0, currentCountyIndex = 0;
                for (int i = 0; i < province.size(); i++) {
                    if (province.get(i).contains(currentProvince)) {
                        currentProvinceIndex = i;
                        ArrayList<String> iCity = city.get(i);
                        for (int j = 0; j < iCity.size(); j++) {
                            if (iCity.get(j).contains(currentCity)) {
                                currentCityIndex = j;
                                ArrayList<String> jCounty = county.get(i).get(j);
                                for (int k = 0; k < jCounty.size(); k++) {
                                    if (jCounty.get(k).contains(currentCounty)) {
                                        currentCountyIndex = k;
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                picker.setSelectedOption(currentProvinceIndex, currentCityIndex, currentCountyIndex);
                picker.setOnWheelListener(new WheelPicker.OnWheelListener<int[]>() {
                    @Override
                    public void onSubmit(int[] result) {
                        if (onCityPickListener != null) {
                            String pro = province.get(result[0]);
                            String cit = city.get(result[0]).get(result[1]);
                            String cou = county.get(result[0]).get(result[1]).get(result[2]);
                            onCityPickListener.onCityPicked(pro, cit, cou);
                        }
                    }
                });
                picker.showAtBottom();
            } else {
                Toast.makeText(activity, "城市数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static class Area {
        String areaId;
        String areaName;

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

    public static class Province extends Area {
        ArrayList<City> cities = new ArrayList<City>();

        public ArrayList<City> getCities() {
            return cities;
        }

        public void setCities(ArrayList<City> cities) {
            this.cities = cities;
        }

    }

    public static class City extends Area {
        private ArrayList<County> counties = new ArrayList<County>();

        public ArrayList<County> getCounties() {
            return counties;
        }

        public void setCounties(ArrayList<County> counties) {
            this.counties = counties;
        }

    }

    public static class County extends Area {
    }

}
