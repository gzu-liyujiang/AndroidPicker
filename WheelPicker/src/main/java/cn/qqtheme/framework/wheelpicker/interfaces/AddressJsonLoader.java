package cn.qqtheme.framework.wheelpicker.interfaces;

/**
 * 地址JSON数据加载器
 *
 * @author liyujiang
 * @date 2019/6/17 16:42
 */
public interface AddressJsonLoader {

    void loadJson(AddressDataLoadListener loadListener, AddressJsonParser jsonParser);

}
