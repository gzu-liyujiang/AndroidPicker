package cn.qqtheme.framework.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 车牌号码省份简称
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/04/17 00:40
 * Builder:Android Studio
 *
 * @see cn.qqtheme.framework.picker.CarNumberPicker
 */
public class CarNumberProvince implements LinkageFirst<CarNumberCity> {
    private String name;

    public CarNumberProvince(String name) {
        this.name = name;
    }

    @Override
    public Object getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<CarNumberCity> getSeconds() {
        List<CarNumberCity> cities = new ArrayList<>();
        switch (name) {
            case "京":
                for (char i = 'A'; i <= 'M'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.add(new CarNumberCity("Y"));
                break;
            case "津":
                for (char i = 'A'; i <= 'H'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                break;
            case "冀":
                for (char i = 'A'; i <= 'H'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.add(new CarNumberCity("J"));
                cities.add(new CarNumberCity("R"));
                cities.add(new CarNumberCity("S"));
                cities.add(new CarNumberCity("T"));
                break;
            case "晋":
                for (char i = 'A'; i <= 'M'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("G"));
                cities.remove(new CarNumberCity("I"));
                break;
            case "蒙":
                for (char i = 'A'; i <= 'M'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                break;
            case "辽":
                for (char i = 'A'; i <= 'P'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "吉":
                for (char i = 'A'; i <= 'K'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                break;
            case "黑":
                for (char i = 'A'; i <= 'R'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "沪":
                for (char i = 'A'; i <= 'D'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.add(new CarNumberCity("R"));
                break;
            case "苏":
                for (char i = 'A'; i <= 'N'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                break;
            case "浙":
                for (char i = 'A'; i <= 'L'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                break;
            case "皖":
                for (char i = 'A'; i <= 'S'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "闽":
                for (char i = 'A'; i <= 'K'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                break;
            case "赣":
                for (char i = 'A'; i <= 'M'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                break;
            case "鲁":
                for (char i = 'A'; i <= 'V'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                cities.remove(new CarNumberCity("Y"));
                cities.add(new CarNumberCity("Y"));
                break;
            case "豫":
                for (char i = 'A'; i <= 'U'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                cities.remove(new CarNumberCity("Y"));
                break;
            case "鄂":
                for (char i = 'A'; i <= 'S'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "湘":
                for (char i = 'A'; i <= 'N'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                cities.add(new CarNumberCity("U"));
                break;
            case "粤":
                for (char i = 'A'; i <= 'Z'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "桂":
                for (char i = 'A'; i <= 'P'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                cities.add(new CarNumberCity("R"));
                break;
            case "琼":
            case "宁":
                for (char i = 'A'; i <= 'E'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                break;
            case "渝":
                for (char i = 'A'; i <= 'D'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("D"));
                cities.remove(new CarNumberCity("E"));
                break;
            case "川":
                for (char i = 'A'; i <= 'Z'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("G"));
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "贵":
            case "藏":
                for (char i = 'A'; i <= 'J'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                break;
            case "云":
                cities.add(new CarNumberCity("A-V"));//昆明市东川区（原东川市）
                for (char i = 'A'; i <= 'S'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("B"));
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "陕":
                for (char i = 'A'; i <= 'K'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.add(new CarNumberCity("V"));
                break;
            case "甘":
                for (char i = 'A'; i <= 'P'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
            case "青":
                for (char i = 'A'; i <= 'H'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                break;
            case "新":
                for (char i = 'A'; i <= 'R'; i++) {
                    cities.add(new CarNumberCity(String.valueOf(i)));
                }
                cities.remove(new CarNumberCity("I"));
                cities.remove(new CarNumberCity("O"));
                break;
        }
        return cities;
    }

    @Override
    public String toString() {
        return "name=" + name;
    }

}
