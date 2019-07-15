package cn.qqtheme.framework.wheelpicker.contract;

import java.util.List;

import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;

/**
 * 地址JSON数据解析器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 16:42
 */
public interface AddressJsonParser {

    List<ProvinceEntity> parseJson(String json);

}
