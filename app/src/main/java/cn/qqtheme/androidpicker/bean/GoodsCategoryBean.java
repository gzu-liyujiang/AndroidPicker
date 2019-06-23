package cn.qqtheme.androidpicker.bean;

import java.io.Serializable;

import cn.qqtheme.framework.wheelview.contract.TextProvider;

/**
 * 示例数据实体
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class GoodsCategoryBean implements Serializable, TextProvider {
    private int id;
    private String name;

    public GoodsCategoryBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
