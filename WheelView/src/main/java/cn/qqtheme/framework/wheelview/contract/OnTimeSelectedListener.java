package cn.qqtheme.framework.wheelview.contract;

/**
 * 时间选择回调
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 19:58
 */
public interface OnTimeSelectedListener {
    /**
     * 已选择的时间
     *
     * @param hour   时
     * @param minute 分
     * @param second 秒
     */
    void onItemSelected(int hour, int minute, int second);
}
