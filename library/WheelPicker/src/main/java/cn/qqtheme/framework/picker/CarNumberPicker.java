package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.CarNumberProvince;
import cn.qqtheme.framework.entity.CarNumberCity;

/**
 * 车牌号码选择器。数据参见http://www.360doc.com/content/12/0602/07/3899427_215339300.shtml
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/12/18 10:47
 * Builder:Android Studio
 */
public class CarNumberPicker extends LinkagePicker<CarNumberProvince, CarNumberCity, Void> {
    private static final String[] ABBREVIATIONS = {
            "京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪",
            "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘",
            "粤", "桂", "琼", "渝", "川", "贵", "云", "藏", "陕",
            "甘", "青", "宁", "新"};

    public CarNumberPicker(Activity activity) {
        super(activity, new CarNumberDataProvider());
    }

    private static class CarNumberDataProvider implements Provider<CarNumberProvince, CarNumberCity, Void> {
        private List<CarNumberProvince> provinces = new ArrayList<>();

        CarNumberDataProvider() {
            for (String abbreviation : ABBREVIATIONS) {
                this.provinces.add(new CarNumberProvince(abbreviation));
            }
        }

        @Override
        public boolean isOnlyTwo() {
            return true;
        }

        @Override
        @NonNull
        public List<CarNumberProvince> initFirstData() {
            return provinces;
        }

        @Override
        @NonNull
        public List<CarNumberCity> linkageSecondData(int firstIndex) {
            return provinces.get(firstIndex).getSeconds();
        }

        @Override
        @NonNull
        public List<Void> linkageThirdData(int firstIndex, int secondIndex) {
            return new ArrayList<>();
        }

    }

}
