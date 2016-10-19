package cn.qqtheme.framework.entity;

import java.util.ArrayList;

/**
 * 省份
 * <br/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016-10-15 19:06
 * Builder:Android Studio
 */
public class Province extends Area {
    private ArrayList<City> cities = new ArrayList<City>();

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

}