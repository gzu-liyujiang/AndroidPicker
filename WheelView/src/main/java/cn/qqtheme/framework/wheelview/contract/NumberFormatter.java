package cn.qqtheme.framework.wheelview.contract;

/**
 * 数字格式化接口
 *
 * @author liyujiang
 * @date 2019/5/14 19:59
 */
public interface NumberFormatter<T extends Number> {
    /**
     * 数字格式化为字符串
     *
     * @param value 数字，整型或浮点型
     * @return 格式化后最终显示的字符串
     */
    String format(T value);
}
