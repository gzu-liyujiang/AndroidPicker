package cn.qqtheme.framework.entity;

/**
 * 用于联动选择器展示的条目
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/04/17 00:31
 * Builder:Android Studio
 *
 * @see cn.qqtheme.framework.picker.LinkagePicker
 */
interface LinkageItem extends WheelItem {

    /**
     * 唯一标识，用于判断两个条目是否相同
     */
    Object getId();

}
