package cn.qqtheme.framework.wheelview.interfaces;

/**
 * 时间选择回调
 *
 * @author liyujiang
 * @date 2019/5/14 19:58
 */
public interface OnTimeSelectedListener {
    /**
     * 已选择的时间
     *
     * @param hour   时
     * @param minute 分
     */
    void onTimeSelected(int hour, int minute);
}
