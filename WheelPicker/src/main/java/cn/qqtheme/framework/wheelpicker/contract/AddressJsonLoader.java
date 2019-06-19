package cn.qqtheme.framework.wheelpicker.contract;

/**
 * 地址JSON数据加载器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 16:42
 */
public interface AddressJsonLoader {

    void loadJson(AddressDataLoadListener loadListener, AddressJsonParser jsonParser);

}
