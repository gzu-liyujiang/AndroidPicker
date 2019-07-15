package cn.qqtheme.framework.wheelview.contract;

/**
 * 时间显示文本格式化
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 19:55
 */
public interface TimeFormatter {

    String formatHour(int hour);

    String formatMinute(int minute);

    String formatSecond(int second);

}
