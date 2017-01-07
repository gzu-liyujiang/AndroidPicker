package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 车牌号码选择器。数据参见http://www.360doc.com/content/12/0602/07/3899427_215339300.shtml
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/12/18 10:47
 * Builder:Android Studio
 */
public class CarNumberPicker extends LinkagePicker {

    public CarNumberPicker(Activity activity) {
        super(activity, new CarNumberDataProvider());
    }

    @Override
    protected int[] getColumnWidths(boolean onlyTwoColumn) {
        return new int[]{WRAP_CONTENT, WRAP_CONTENT, 0};
    }

    public static class CarNumberDataProvider implements DataProvider {
        private List<String> provinces = new ArrayList<>();

        public CarNumberDataProvider() {
            provinces = Arrays.asList(
                    "京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪",
                    "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘",
                    "粤", "桂", "琼", "渝", "川", "贵", "云", "藏", "陕",
                    "甘", "青", "宁", "新");
        }

        @Override
        public boolean isOnlyTwo() {
            return true;
        }

        @Override
        public List<String> provideFirstData() {
            return provinces;
        }

        @Override
        public List<String> provideSecondData(int firstIndex) {
            return parseData(provinces.get(firstIndex));
        }

        @Override
        public List<String> provideThirdData(int firstIndex, int secondIndex) {
            return new ArrayList<>();
        }

        @NonNull
        private List<String> parseData(String province) {
            List<String> cities = new ArrayList<>();
            switch (province) {
                case "京":
                    for (char i = 'A'; i <= 'M'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.add("Y");
                    break;
                case "津":
                    for (char i = 'A'; i <= 'H'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    break;
                case "冀":
                    for (char i = 'A'; i <= 'H'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.add("J");
                    cities.add("R");
                    cities.add("S");
                    cities.add("T");
                    break;
                case "晋":
                    for (char i = 'A'; i <= 'M'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("G");
                    cities.remove("I");
                    break;
                case "蒙":
                    for (char i = 'A'; i <= 'M'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    break;
                case "辽":
                    for (char i = 'A'; i <= 'P'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "吉":
                    for (char i = 'A'; i <= 'K'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    break;
                case "黑":
                    for (char i = 'A'; i <= 'R'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "沪":
                    for (char i = 'A'; i <= 'D'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.add("R");
                    break;
                case "苏":
                    for (char i = 'A'; i <= 'N'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    break;
                case "浙":
                    for (char i = 'A'; i <= 'L'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    break;
                case "皖":
                    for (char i = 'A'; i <= 'S'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "闽":
                    for (char i = 'A'; i <= 'K'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    break;
                case "赣":
                    for (char i = 'A'; i <= 'M'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    break;
                case "鲁":
                    for (char i = 'A'; i <= 'V'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    cities.remove("T");
                    cities.add("Y");
                    break;
                case "豫":
                    for (char i = 'A'; i <= 'U'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    cities.remove("T");
                    break;
                case "鄂":
                    for (char i = 'A'; i <= 'S'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "湘":
                    for (char i = 'A'; i <= 'N'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    cities.add("U");
                    break;
                case "粤":
                    for (char i = 'A'; i <= 'Z'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "桂":
                    for (char i = 'A'; i <= 'P'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    cities.add("R");
                    break;
                case "琼":
                case "宁":
                    for (char i = 'A'; i <= 'E'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    break;
                case "渝":
                    for (char i = 'A'; i <= 'D'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("D");
                    cities.remove("E");
                    break;
                case "川":
                    for (char i = 'A'; i <= 'Z'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("G");
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "贵":
                case "藏":
                    for (char i = 'A'; i <= 'J'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    break;
                case "云":
                    cities.add("A-V");//昆明市东川区（原东川市）
                    for (char i = 'A'; i <= 'S'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("B");
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "陕":
                    for (char i = 'A'; i <= 'K'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.add("V");
                    break;
                case "甘":
                    for (char i = 'A'; i <= 'P'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    break;
                case "青":
                    for (char i = 'A'; i <= 'H'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    break;
                case "新":
                    for (char i = 'A'; i <= 'R'; i++) {
                        cities.add(String.valueOf(i));
                    }
                    cities.remove("I");
                    cities.remove("O");
                    break;
            }
            return cities;
        }

    }

}
