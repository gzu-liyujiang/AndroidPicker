package cn.qqtheme.framework.entity;

/**
 * 区县
 * <br/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016-10-15 19:08
 * Builder:Android Studio
 */
public class County extends Area {
    private String cityId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
