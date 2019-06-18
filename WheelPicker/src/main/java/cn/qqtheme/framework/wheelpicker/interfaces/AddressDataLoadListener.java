package cn.qqtheme.framework.wheelpicker.interfaces;

import java.util.List;

import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;

/**
 * 地址数据加载监听器
 *
 * @author liyujiang
 * @date 2019/6/17 16:42
 */
public interface AddressDataLoadListener {

    void onDataLoadStart();

    void onDataLoadSuccess(List<ProvinceEntity> data);

    void onDataLoadFailure();

}
