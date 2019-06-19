package cn.qqtheme.framework.wheelview.contract;

/**
 * 日期显示文本格式化
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 19:55
 */
public interface DateFormatter {

    String formatYear(int year);

    String formatMonth(int month);

    String formatDay(int day);

}

