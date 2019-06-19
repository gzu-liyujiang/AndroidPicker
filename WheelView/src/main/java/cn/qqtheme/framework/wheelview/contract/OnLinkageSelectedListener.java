package cn.qqtheme.framework.wheelview.contract;

/**
 * 联动选择回调
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 18:23
 */
public interface OnLinkageSelectedListener<F extends LinkageTextProvider,
        S extends LinkageTextProvider, T extends TextProvider> {

    void onItemSelected(F first, S second, T third);

}
