package cn.qqtheme.framework.wheelview.contract;

/**
 * 日期选择回调
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 19:57
 */
public interface OnDateSelectedListener {
    /**
     * 已选择的日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    void onItemSelected(int year, int month, int day);
}
