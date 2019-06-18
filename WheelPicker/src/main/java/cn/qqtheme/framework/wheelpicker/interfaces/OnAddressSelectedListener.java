package cn.qqtheme.framework.wheelpicker.interfaces;

import cn.qqtheme.framework.wheelpicker.entity.CityEntity;
import cn.qqtheme.framework.wheelpicker.entity.CountyEntity;
import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;
import cn.qqtheme.framework.wheelview.interfaces.OnLinkageSelectedListener;

/**
 * 地址选择回调
 *
 * @author liyujiang
 * @date 2019/6/17 18:23
 */
public interface OnAddressSelectedListener extends OnLinkageSelectedListener<ProvinceEntity,
        CityEntity, CountyEntity> {

}
