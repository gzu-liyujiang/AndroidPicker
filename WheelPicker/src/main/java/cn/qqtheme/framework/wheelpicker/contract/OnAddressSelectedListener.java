package cn.qqtheme.framework.wheelpicker.contract;

import cn.qqtheme.framework.wheelpicker.entity.CityEntity;
import cn.qqtheme.framework.wheelpicker.entity.CountyEntity;
import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;
import cn.qqtheme.framework.wheelview.contract.OnLinkageSelectedListener;

/**
 * 地址选择回调
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 18:23
 */
public interface OnAddressSelectedListener extends OnLinkageSelectedListener<ProvinceEntity,
        CityEntity, CountyEntity> {

}
