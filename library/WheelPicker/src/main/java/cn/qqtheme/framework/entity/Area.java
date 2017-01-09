package cn.qqtheme.framework.entity;

/**
 * 省市县抽象，为了使用FastJson及LiteOrm之类的库，本类及其子类不可混淆
 * <br/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016-10-15 19:06
 * Builder:Android Studio
 */
public abstract class Area extends JavaBean {
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