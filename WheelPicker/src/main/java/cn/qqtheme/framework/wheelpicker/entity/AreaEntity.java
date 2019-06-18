package cn.qqtheme.framework.wheelpicker.entity;

import android.support.annotation.Keep;

import java.io.Serializable;

import cn.qqtheme.framework.wheelview.interfaces.TextProvider;

/**
 * 地址数据实体
 *
 * @author liyujiang
 * @date 2019/6/17 11:47
 */
@Keep
class AreaEntity implements TextProvider, Serializable {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String provideItemText() {
        return name;
    }

}
