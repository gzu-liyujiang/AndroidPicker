package cn.qqtheme.framework.wheelpicker.interfaces;

import java.util.List;

import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;

/**
 * 地址JSON数据解析器
 *
 * @author liyujiang
 * @date 2019/6/17 16:42
 */
public interface AddressJsonParser {

    List<ProvinceEntity> parseJson(String json);

}
