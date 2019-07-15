package cn.qqtheme.framework.wheelview.contract;

import java.util.List;

/**
 * 提供显示的文本及联动的数据
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 11:42
 */
public interface LinkageTextProvider extends TextProvider {
    /**
     * 联动的数据
     *
     * @return 联动的数据
     */
    List<? extends TextProvider> linkageData();
}

