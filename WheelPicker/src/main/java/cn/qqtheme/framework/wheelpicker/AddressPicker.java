package cn.qqtheme.framework.wheelpicker;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.qqtheme.framework.wheelpicker.annotation.AddressMode;
import cn.qqtheme.framework.wheelpicker.entity.CityEntity;
import cn.qqtheme.framework.wheelpicker.entity.CountyEntity;
import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;
import cn.qqtheme.framework.wheelpicker.contract.AddressDataLoadListener;
import cn.qqtheme.framework.wheelpicker.contract.AddressJsonLoader;
import cn.qqtheme.framework.wheelpicker.contract.AddressJsonParser;
import cn.qqtheme.framework.wheelpicker.contract.OnAddressSelectedListener;
import cn.qqtheme.framework.wheelpicker.impl.AddressAssetsJsonLoader;
import cn.qqtheme.framework.wheelpicker.impl.AddressDataProvider;
import cn.qqtheme.framework.wheelpicker.impl.AddressOrgJsonParser;
import cn.qqtheme.framework.wheelview.contract.LinkageDataProvider;
import cn.qqtheme.framework.wheelview.contract.OnLinkageSelectedListener;

/**
 * 省市区县滚轮选择，参见 https://github.com/gzu-liyujiang/AndroidPicker
 * <p>
 * 数据来源：
 * 1、国家民政局：http://www.mca.gov.cn/article/sj/xzqh/2019
 * 2、国家统计局：http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm
 * 3、台湾维基数据：https://zh.wikipedia.org/wiki/中华人民共和国行政区划代码_(7区)
 * 4、港澳维基数据：https://zh.wikipedia.org/wiki/中华人民共和国行政区划代码_(8区)
 * 5、数据抓取转化：https://github.com/small-dream/China_Province_City
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/15 12:17
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AddressPicker extends LinkagePicker<ProvinceEntity, CityEntity, CountyEntity>
        implements AddressDataLoadListener {

    private boolean isOnlyTwoLevel;
    private AddressJsonLoader jsonLoader;
    private AddressJsonParser jsonParser;

    public AddressPicker(FragmentActivity activity) {
        super(activity);
        setAddressMode(AddressMode.PROVINCE_CITY_COUNTY);
    }

    public void setAddressMode(@AddressMode int addressMode) {
        this.isOnlyTwoLevel = addressMode == AddressMode.PROVINCE_CITY;
    }

    public final boolean isOnlyTwoLevel() {
        return isOnlyTwoLevel;
    }

    public void setJsonLoader(@NonNull AddressJsonLoader jsonLoader) {
        this.jsonLoader = jsonLoader;
    }

    public void setJsonParser(@NonNull AddressJsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    /**
     * @deprecated Use {@link #setJsonLoader(AddressJsonLoader)} instead
     */
    @Deprecated
    @Override
    public void setDataProvider(LinkageDataProvider<ProvinceEntity, CityEntity,
                CountyEntity> dataProvider) {
        super.setDataProvider(dataProvider);
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener onAddressSelectedListener) {
        super.setOnLinkageSelectedListener(onAddressSelectedListener);
    }

    /**
     * @deprecated Use {@link #setOnAddressSelectedListener(OnAddressSelectedListener)} instead
     */
    @Deprecated
    @Override
    public void setOnLinkageSelectedListener(OnLinkageSelectedListener<ProvinceEntity, CityEntity,
                CountyEntity> listener) {
        super.setOnLinkageSelectedListener(listener);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        if (jsonLoader == null) {
            jsonLoader = new AddressAssetsJsonLoader(activity);
        }
        if (jsonParser == null) {
            jsonParser = new AddressOrgJsonParser();
        }
        jsonLoader.loadJson(this, jsonParser);
    }

    @Override
    public void onDataLoadStart() {

    }

    @CallSuper
    @Override
    public void onDataLoadSuccess(List<ProvinceEntity> data) {
        getWheelLayout().setDataProvider(new AddressDataProvider(isOnlyTwoLevel, data));
    }

    @Override
    public void onDataLoadFailure() {
        Toast.makeText(activity, "地址数据加载失败", Toast.LENGTH_SHORT).show();
    }

}
