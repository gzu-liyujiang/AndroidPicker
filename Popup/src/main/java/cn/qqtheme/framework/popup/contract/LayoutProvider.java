package cn.qqtheme.framework.popup.contract;


import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

/**
 * 自定义布局提供者接口
 */
public interface LayoutProvider {

    /**
     * 提供布局文件
     *
     * @return {@link LayoutRes}
     */
    @LayoutRes
    int provideLayoutRes();

    /**
     * 提供取消的资源ID
     *
     * @return {@link IdRes}
     */
    @IdRes
    int specifyCancelIdRes();

    /**
     * 提供标题的资源ID
     *
     * @return {@link IdRes}
     */
    @IdRes
    int specifyTitleIdRes();

    /**
     * 提供确认的资源ID
     *
     * @return {@link IdRes}
     */
    @IdRes
    int specifyConfirmIdRes();

    /**
     * 提供主体视图的资源ID
     *
     * @return {@link IdRes}
     */
    int specifyBodyIdRes();

}
