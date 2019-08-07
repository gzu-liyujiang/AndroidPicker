package cn.qqtheme.framework.entity;

import java.util.List;

/**
 * 车牌号码城市代号字母
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/04/17 00:42
 * Builder:Android Studio
 *
 * @see cn.qqtheme.framework.picker.CarNumberPicker
 */
public class CarNumberCity implements LinkageSecond<Void> {
    private String name;

    public CarNumberCity(String name) {
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
    public List<Void> getThirds() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CarNumberCity)) {
            return false;
        }
        CarNumberCity obj1 = (CarNumberCity) obj;
        return name.equals(obj1.getName());
    }

    @Override
    public String toString() {
        return "name=" + name;
    }

}