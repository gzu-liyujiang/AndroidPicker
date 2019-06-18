package cn.qqtheme.framework.wheelview.interfaces;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * 联动数据提供者
 *
 * @author liyujiang
 * @date 2019/6/17 11:27
 */
public interface LinkageDataProvider<F extends LinkageTextProvider, S extends LinkageTextProvider,
        T extends TextProvider> {

    /**
     * 是否只是二级联动
     *
     * @return 返回true表示仅仅是二级联动
     */
    boolean isOnlyTwoLevel();

    /**
     * 提供第一级数据
     *
     * @return 第一级数据
     */
    @NonNull
    List<F> provideFirstData();

    /**
     * 根据第一级数据联动第二级数据
     *
     * @param firstIndex 第一级数据索引
     * @return 第二级数据
     */
    @NonNull
    List<S> linkageSecondData(int firstIndex);

    /**
     * 根据第一二级数据联动第三级数据
     *
     * @param firstIndex  第一级数据索引
     * @param secondIndex 第二级数据索引
     * @return 第三级数据
     */
    @NonNull
    List<T> linkageThirdData(int firstIndex, int secondIndex);

}